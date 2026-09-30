import {
  anioDeFecha,
  calcularTotales,
  hoyIso,
  numeroDocumento,
  type Documento,
  type DocumentoInput,
  type DocumentoResumen,
  type TipoDocumento,
} from '@cabp/shared';
import type { Pool } from 'mysql2/promise';
import { consultar, ejecutar, transaccion, type Db } from '../db/pool';
import { conflicto, noEncontrado, peticionIncorrecta } from '../errores';
import { obtenerCliente } from './clientes';
import { siguienteNumero } from './numeracion';

/** Correspondencia campo de la API -> columna. */
const CAMPOS: [keyof DocumentoInput, string][] = [
  ['fecha', 'fecha'],
  ['clienteId', 'cliente_id'],
  ['distribuidorId', 'distribuidor_id'],
  ['ganancia', 'ganancia'],
  ['restaurante', 'restaurante'],
  ['pasaje', 'pasaje'],
  ['combustible', 'combustible'],
  ['otros', 'otros'],
  ['hotel', 'hotel'],
  ['transporte', 'transporte'],
  ['kilometros', 'kilometros'],
  ['numViajes', 'num_viajes'],
  ['precioGasolina', 'precio_gasolina'],
  ['aplicaGanancia', 'aplica_ganancia'],
  ['aplicaIva', 'aplica_iva'],
  ['ivaPorcentaje', 'iva_porcentaje'],
  ['totalManualActivo', 'total_manual_activo'],
  ['totalManual', 'total_manual'],
  ['porcentajeReparto', 'porcentaje_reparto'],
  ['textoConcepto', 'texto_concepto'],
  ['textoFormaPago', 'texto_forma_pago'],
  ['textoExplicativo', 'texto_explicativo'],
  ['calcTipo', 'calc_tipo'],
  ['valorA', 'valor_a'],
  ['valorB', 'valor_b'],
  ['valorC', 'valor_c'],
  ['valorAux', 'valor_aux'],
];
const BOOLEANOS = new Set(['aplicaGanancia', 'aplicaIva', 'totalManualActivo']);

function valoresConTotales(d: DocumentoInput) {
  const t = calcularTotales(d);
  const cols = [...CAMPOS.map(([, c]) => c), 'total_sin_iva', 'total_iva', 'total_con_iva'];
  const vals = [...CAMPOS.map(([k]) => d[k]), t.totalSinIva, t.totalIva, t.totalConIva];
  return { cols, vals };
}

async function guardarLineas(conn: Db, documentoId: number, d: DocumentoInput) {
  await ejecutar(conn, 'DELETE FROM documento_lineas WHERE documento_id = ?', [documentoId]);
  if (!d.lineas.length) return;
  // Si el producto ya no existe se guarda la línea sin enlace.
  const ids = d.lineas.map((l) => l.productoId).filter((x): x is number => x != null);
  const existentes = new Set(
    ids.length ? (await consultar(conn, 'SELECT id FROM productos WHERE id IN (?)', [ids])).map((f) => f.id) : [],
  );
  await ejecutar(
    conn,
    'INSERT INTO documento_lineas (documento_id, orden, producto_id, nombre_producto, cantidad, precio) VALUES ?',
    [
      d.lineas.map((l, i) => [
        documentoId,
        i,
        l.productoId && existentes.has(l.productoId) ? l.productoId : null,
        l.nombreProducto,
        l.cantidad,
        l.precio,
      ]),
    ],
  );
}

async function comprobarReferencias(conn: Db, d: DocumentoInput) {
  const [c] = await consultar(conn, 'SELECT id FROM clientes WHERE id = ?', [d.clienteId]);
  if (!c) throw peticionIncorrecta('El cliente no existe');
  if (d.distribuidorId) {
    const [di] = await consultar(conn, 'SELECT id FROM distribuidores WHERE id = ?', [d.distribuidorId]);
    if (!di) throw peticionIncorrecta('El distribuidor no existe');
  }
}

export async function insertarDocumento(
  conn: Db,
  d: DocumentoInput,
  extra: { numero?: number; presupuestoOrigenId?: number | null; usuarioId?: number | null } = {},
): Promise<number> {
  const anio = anioDeFecha(d.fecha);
  const numero = extra.numero ?? (await siguienteNumero(conn, d.tipo, anio));
  const { cols, vals } = valoresConTotales(d);
  const r = await ejecutar(
    conn,
    `INSERT INTO documentos (tipo, anio, numero, presupuesto_origen_id, creado_por, ${cols.join(', ')})
     VALUES (?, ?, ?, ?, ?, ${cols.map(() => '?').join(', ')})`,
    [d.tipo, anio, numero, extra.presupuestoOrigenId ?? null, extra.usuarioId ?? null, ...vals],
  );
  await guardarLineas(conn, r.insertId, d);
  return r.insertId;
}

export async function crearDocumento(pool: Pool, d: DocumentoInput, usuarioId: number): Promise<number> {
  return transaccion(pool, async (conn) => {
    await comprobarReferencias(conn, d);
    return insertarDocumento(conn, d, { usuarioId });
  });
}

export async function actualizarDocumento(pool: Pool, id: number, d: DocumentoInput): Promise<void> {
  await transaccion(pool, async (conn) => {
    const [actual] = await consultar(conn, 'SELECT tipo, anio FROM documentos WHERE id = ? FOR UPDATE', [id]);
    if (!actual) throw noEncontrado('Documento');
    if (actual.tipo !== d.tipo) throw peticionIncorrecta('No se puede cambiar el tipo de documento');
    if (anioDeFecha(d.fecha) !== actual.anio) {
      throw peticionIncorrecta(`La fecha debe ser del ejercicio ${actual.anio}: el número ya está asignado a ese año`);
    }
    await comprobarReferencias(conn, d);
    const { cols, vals } = valoresConTotales(d);
    await ejecutar(conn, `UPDATE documentos SET ${cols.map((c) => `${c} = ?`).join(', ')} WHERE id = ?`, [...vals, id]);
    await guardarLineas(conn, id, d);
  });
}

function mapear(f: any): Omit<Documento, 'lineas'> {
  const doc: any = {
    id: f.id,
    tipo: f.tipo,
    anio: f.anio,
    numero: f.numero,
    codigo: numeroDocumento(f.tipo, f.anio, f.numero),
    presupuestoOrigenId: f.presupuesto_origen_id,
    facturaId: f.factura_id ?? null,
    totalSinIva: f.total_sin_iva,
    totalIva: f.total_iva,
    totalConIva: f.total_con_iva,
  };
  for (const [k, c] of CAMPOS) doc[k] = BOOLEANOS.has(k) ? !!f[c] : f[c];
  return doc;
}

export async function obtenerDocumento(db: Db, id: number): Promise<Documento> {
  const [f] = await consultar(
    db,
    `SELECT d.*, f.id AS factura_id, di.nombre AS distribuidor_nombre
       FROM documentos d
       LEFT JOIN documentos f ON f.presupuesto_origen_id = d.id
       LEFT JOIN distribuidores di ON di.id = d.distribuidor_id
      WHERE d.id = ?`,
    [id],
  );
  if (!f) throw noEncontrado('Documento');
  const lineas = (
    await consultar(db, 'SELECT * FROM documento_lineas WHERE documento_id = ? ORDER BY orden, id', [id])
  ).map((l) => ({
    id: l.id,
    productoId: l.producto_id,
    nombreProducto: l.nombre_producto,
    cantidad: l.cantidad,
    precio: l.precio,
  }));
  const cliente = await obtenerCliente(db, f.cliente_id);
  return {
    ...mapear(f),
    lineas,
    cliente: cliente ?? undefined,
    distribuidor: f.distribuidor_id ? { id: f.distribuidor_id, nombre: f.distribuidor_nombre } : null,
  } as Documento;
}

export async function listarDocumentos(
  db: Db,
  filtro: { tipo?: TipoDocumento; anio?: number; clienteId?: number; q?: string },
): Promise<DocumentoResumen[]> {
  const where: string[] = [];
  const params: unknown[] = [];
  if (filtro.tipo) (where.push('d.tipo = ?'), params.push(filtro.tipo));
  if (filtro.anio) (where.push('d.anio = ?'), params.push(filtro.anio));
  if (filtro.clienteId) (where.push('d.cliente_id = ?'), params.push(filtro.clienteId));
  if (filtro.q) {
    const like = `%${filtro.q.replace(/[\\%_]/g, (c) => '\\' + c)}%`;
    where.push("(CONCAT(c.nombre, ' ', c.apellidos) LIKE ? OR c.dni LIKE ? OR d.texto_concepto LIKE ? OR d.numero = ?)");
    params.push(like, like, like, Number(filtro.q) || -1);
  }
  const filas = await consultar(
    db,
    `SELECT d.id, d.tipo, d.anio, d.numero, d.fecha, d.cliente_id, d.total_con_iva,
            TRIM(CONCAT(c.nombre, ' ', c.apellidos)) AS cliente_nombre, di.nombre AS distribuidor_nombre,
            f.id AS factura_id
       FROM documentos d
       JOIN clientes c ON c.id = d.cliente_id
       LEFT JOIN distribuidores di ON di.id = d.distribuidor_id
       LEFT JOIN documentos f ON f.presupuesto_origen_id = d.id
      ${where.length ? 'WHERE ' + where.join(' AND ') : ''}
      ORDER BY d.anio DESC, d.numero DESC`,
    params,
  );
  return filas.map((f) => ({
    id: f.id,
    tipo: f.tipo,
    anio: f.anio,
    numero: f.numero,
    codigo: numeroDocumento(f.tipo, f.anio, f.numero),
    fecha: f.fecha,
    clienteId: f.cliente_id,
    clienteNombre: f.cliente_nombre,
    distribuidorNombre: f.distribuidor_nombre,
    totalConIva: f.total_con_iva,
    facturaId: f.factura_id,
  }));
}

export async function aniosConDocumentos(db: Db): Promise<number[]> {
  const filas = await consultar(db, 'SELECT DISTINCT anio FROM documentos ORDER BY anio DESC');
  return filas.map((f) => f.anio);
}

/** Crea una factura (fecha de hoy, numeración del año actual) a partir de un presupuesto. */
export async function convertirAFactura(pool: Pool, presupuestoId: number, usuarioId: number): Promise<number> {
  return transaccion(pool, async (conn) => {
    const [p] = await consultar(conn, 'SELECT tipo FROM documentos WHERE id = ? FOR UPDATE', [presupuestoId]);
    if (!p) throw noEncontrado('Presupuesto');
    if (p.tipo !== 'presupuesto') throw peticionIncorrecta('Solo se pueden convertir presupuestos');
    const [ya] = await consultar(conn, 'SELECT id FROM documentos WHERE presupuesto_origen_id = ?', [presupuestoId]);
    if (ya) throw conflicto('Este presupuesto ya se convirtió en factura');
    const doc = await obtenerDocumento(conn, presupuestoId);
    const { id: _id, anio: _a, numero: _n, codigo: _c, presupuestoOrigenId: _p, facturaId: _f, cliente: _cl, distribuidor: _d, totalSinIva: _1, totalIva: _2, totalConIva: _3, ...resto } = doc;
    const datos: DocumentoInput = {
      ...resto,
      tipo: 'factura',
      fecha: hoyIso(),
      lineas: doc.lineas.map(({ id: _lid, ...l }) => l),
    };
    return insertarDocumento(conn, datos, { presupuestoOrigenId: presupuestoId, usuarioId });
  });
}

/**
 * Presupuestos: se pueden borrar siempre.
 * Facturas: solo la última de su año, para no dejar huecos en la numeración; el contador retrocede.
 */
export async function borrarDocumento(pool: Pool, id: number): Promise<void> {
  await transaccion(pool, async (conn) => {
    const [d] = await consultar(conn, 'SELECT tipo, anio, numero FROM documentos WHERE id = ? FOR UPDATE', [id]);
    if (!d) throw noEncontrado('Documento');
    if (d.tipo === 'factura') {
      const [c] = await consultar(conn, 'SELECT ultimo_numero FROM contadores WHERE tipo = ? AND anio = ? FOR UPDATE', [
        'factura',
        d.anio,
      ]);
      if (!c || c.ultimo_numero !== d.numero) {
        throw conflicto('Solo se puede borrar la última factura del año para no dejar huecos en la numeración');
      }
      await ejecutar(conn, 'UPDATE contadores SET ultimo_numero = ultimo_numero - 1 WHERE tipo = ? AND anio = ?', [
        'factura',
        d.anio,
      ]);
    }
    await ejecutar(conn, 'DELETE FROM documentos WHERE id = ?', [id]);
  });
}

/** Para el formulario de "nuevo": el número que previsiblemente se asignará. */
export async function proximoNumero(db: Db, tipo: TipoDocumento, anio: number): Promise<string> {
  const [c] = await consultar(db, 'SELECT ultimo_numero FROM contadores WHERE tipo = ? AND anio = ?', [tipo, anio]);
  return numeroDocumento(tipo, anio, (c?.ultimo_numero ?? 0) + 1);
}
