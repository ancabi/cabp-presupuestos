/**
 * Importa una base de datos SQLite de la versión Java (una por año) a MariaDB.
 *
 *   npm run importar -- --anio 2011 ruta/cabp2011.sqlite [--adjuntos ruta/carpeta] [--iva 18] [--dry-run]
 *
 * - Clientes: se fusionan por DNI (o por nombre + apellidos si no hay DNI).
 * - Distribuidores por nombre y productos por (distribuidor, nombre).
 * - Presupuestos y facturas conservan su número antiguo dentro del año indicado.
 *   Volver a ejecutar el script no duplica documentos: los números ya importados se omiten.
 * - --adjuntos: carpeta donde estaban las carpetas "<id><nombre><apellidos>" de cada cliente.
 */
import fs from 'node:fs';
import fsp from 'node:fs/promises';
import path from 'node:path';
import crypto from 'node:crypto';
import { parseArgs } from 'node:util';
import Database from 'better-sqlite3';
import { documentoSchema, type TipoDocumento } from '@cabp/shared';
import { crearPool, consultar, ejecutar } from '../src/db/pool';
import { rutaUploads } from '../src/config';
import { migrar } from '../src/db/migrar';
import { ajustarContador } from '../src/services/numeracion';
import { insertarDocumento } from '../src/services/documentos';

const { values: args, positionals } = parseArgs({
  allowPositionals: true,
  options: {
    anio: { type: 'string' },
    adjuntos: { type: 'string' },
    iva: { type: 'string', default: '21' },
    'dry-run': { type: 'boolean', default: false },
  },
});

// npm ejecuta el script dentro de server/: las rutas relativas se toman desde donde se lanzó el comando.
const desde = (p: string) => path.resolve(process.env.INIT_CWD ?? process.cwd(), p);
const fichero = positionals[0] && desde(positionals[0]);
const carpetaAdjuntos = args.adjuntos && desde(args.adjuntos);
if (!fichero || !fs.existsSync(fichero)) {
  console.error('Uso: npm run importar -- --anio 2011 ruta/cabp2011.sqlite [--adjuntos carpeta] [--iva 21] [--dry-run]');
  process.exit(1);
}
const anio = Number(args.anio ?? path.basename(fichero).match(/(19|20)\d{2}/)?.[0]);
if (!Number.isInteger(anio) || anio < 1990 || anio > 2100) {
  console.error('No se pudo deducir el año del nombre del fichero: indícalo con --anio');
  process.exit(1);
}
const ivaPorDefecto = Number(args.iva);
const dryRun = args['dry-run'];
const uploadsDir = rutaUploads();

const sq = new Database(fichero, { readonly: true, fileMustExist: true });
const tablas = new Set(
  (sq.prepare("SELECT name FROM sqlite_master WHERE type = 'table'").all() as { name: string }[]).map((t) => t.name),
);
const filas = (tabla: string): Record<string, any>[] =>
  tablas.has(tabla) ? (sq.prepare(`SELECT * FROM "${tabla}"`).all() as Record<string, any>[]) : [];

const str = (v: unknown) => (v == null ? '' : String(v).trim());
const num = (v: unknown, def = 0) => {
  const n = typeof v === 'string' ? Number(v.replace(',', '.')) : Number(v);
  return Number.isFinite(n) ? n : def;
};
const bool = (v: unknown, def: boolean) =>
  v == null || v === '' ? def : v === true || v === 1 || v === '1' || String(v).toLowerCase() === 'true';

function fechaIso(v: unknown): string | null {
  if (v == null || v === '') return null;
  let d: Date;
  if (typeof v === 'number' || /^\d+$/.test(String(v))) {
    const n = Number(v);
    d = new Date(n < 1e11 ? n * 1000 : n); // segundos o milisegundos
  } else {
    d = new Date(String(v));
  }
  return Number.isNaN(d.getTime()) ? null : d.toISOString().slice(0, 10);
}

const resumen: Record<string, number> = {};
const contar = (k: string) => (resumen[k] = (resumen[k] ?? 0) + 1);
const avisos: string[] = [];

const pool = crearPool(process.env.DATABASE_URL!);
await migrar(pool);
const conn = await pool.getConnection();
const copias: { origen: string; clienteId: number }[] = [];

try {
  await conn.beginTransaction();

  // --- Clientes ---
  const mapaClientes = new Map<number, number>();
  const telefonos = filas('telefonos');
  const emails = filas('email');
  for (const c of filas('clientes')) {
    const dni = str(c.dni).toUpperCase();
    const nombre = str(c.nombre) || '(sin nombre)';
    const apellidos = str(c.apellidos);
    let existente: any;
    if (dni) [existente] = await consultar(conn, 'SELECT id FROM clientes WHERE UPPER(dni) = ?', [dni]);
    else
      [existente] = await consultar(conn, "SELECT id FROM clientes WHERE dni = '' AND LOWER(nombre) = LOWER(?) AND LOWER(apellidos) = LOWER(?)", [
        nombre,
        apellidos,
      ]);
    let id: number;
    if (existente) {
      id = existente.id;
      contar('clientes fusionados');
    } else {
      const r = await ejecutar(
        conn,
        'INSERT INTO clientes (dni, nombre, apellidos, direccion, ciudad, provincia, empresa, notas) VALUES (?,?,?,?,?,?,?,?)',
        [dni, nombre, apellidos, str(c.direccion), str(c.ciudad), str(c.provincia), str(c.empresa), str(c.notas)],
      );
      id = r.insertId;
      contar('clientes nuevos');
    }
    mapaClientes.set(c.idCliente, id);
    for (const t of telefonos.filter((t) => t.idCliente === c.idCliente)) {
      await ejecutar(conn, 'INSERT IGNORE INTO cliente_telefonos (cliente_id, telefono) VALUES (?, ?)', [id, str(t.telefono)]);
    }
    for (const e of emails.filter((e) => e.idCliente === c.idCliente)) {
      await ejecutar(conn, 'INSERT IGNORE INTO cliente_emails (cliente_id, email) VALUES (?, ?)', [id, str(e.email).toLowerCase()]);
    }
    if (carpetaAdjuntos) {
      const candidatos = [
        `${c.idCliente}${str(c.nombre).replace(/\s/g, '')}${str(c.apellidos).replace(/\s/g, '')}`,
        `${c.idCliente}${str(c.nombre)}${str(c.apellidos)}`,
      ];
      const dir = candidatos.map((n) => path.join(carpetaAdjuntos, n)).find((p) => fs.existsSync(p));
      if (dir) {
        for (const f of fs.readdirSync(dir)) copias.push({ origen: path.join(dir, f), clienteId: id });
      }
    }
  }

  // --- Distribuidores y productos ---
  const mapaDist = new Map<number, number>();
  for (const d of filas('distribuidores')) {
    const nombre = str(d.nombre) || '(sin nombre)';
    const [ex] = await consultar(conn, 'SELECT id FROM distribuidores WHERE LOWER(nombre) = LOWER(?)', [nombre]);
    if (ex) {
      mapaDist.set(d.idDistribuidor, ex.id);
      contar('distribuidores fusionados');
      continue;
    }
    const r = await ejecutar(
      conn,
      'INSERT INTO distribuidores (nombre, direccion, email, telefono, ciudad, provincia, pais, numero_cta, iban, swift) VALUES (?,?,?,?,?,?,?,?,?,?)',
      [nombre, str(d.direccion), str(d.email), str(d.telefono), str(d.ciudad), str(d.provincia), str(d.pais), str(d.numeroCta), str(d.iban), str(d.swif)],
    );
    mapaDist.set(d.idDistribuidor, r.insertId);
    contar('distribuidores nuevos');
  }
  const mapaProd = new Map<number, number>();
  for (const p of filas('productos')) {
    const distId = mapaDist.get(p.idDistribuidor);
    if (!distId) {
      contar('productos omitidos (sin distribuidor)');
      continue;
    }
    const [ex] = await consultar(conn, 'SELECT id FROM productos WHERE distribuidor_id = ? AND LOWER(nombre) = LOWER(?) AND activo = 1', [
      distId,
      str(p.nombre),
    ]);
    if (ex) {
      mapaProd.set(p.idProducto, ex.id);
      contar('productos fusionados');
      continue;
    }
    const r = await ejecutar(conn, 'INSERT INTO productos (distribuidor_id, nombre, precio) VALUES (?,?,?)', [distId, str(p.nombre), num(p.precio)]);
    mapaProd.set(p.idProducto, r.insertId);
    contar('productos nuevos');
  }

  // --- Presupuestos y facturas ---
  const origenes: [TipoDocumento, string, string, string][] = [
    ['presupuesto', 'presupuestos', 'idPresupuesto', 'lineaPresupuesto'],
    ['factura', 'facturas', 'idFactura', 'lineaFactura'],
  ];
  for (const [tipo, tabla, pk, tablaLineas] of origenes) {
    const lineas = filas(tablaLineas);
    let maximo = 0;
    for (const o of filas(tabla)) {
      const numero = Number(o[pk]);
      if (!Number.isInteger(numero) || numero <= 0) {
        avisos.push(`${tabla}: fila sin ${pk} válido, se omite`);
        contar(`${tabla} omitidos`);
        continue;
      }
      maximo = Math.max(maximo, numero);
      const [ya] = await consultar(conn, 'SELECT id FROM documentos WHERE tipo = ? AND anio = ? AND numero = ?', [tipo, anio, numero]);
      if (ya) {
        contar(`${tabla} ya importados`);
        continue;
      }
      const clienteId = mapaClientes.get(o.idCliente);
      if (!clienteId) {
        avisos.push(`${tipo} ${numero}: cliente ${o.idCliente} inexistente, se omite`);
        contar(`${tabla} omitidos`);
        continue;
      }
      let fecha = fechaIso(o.fecha) ?? `${anio}-01-01`;
      if (Number(fecha.slice(0, 4)) !== anio) {
        avisos.push(`${tipo} ${numero}: fecha ${fecha} fuera de ${anio}, se ajusta`);
        fecha = Number(fecha.slice(0, 4)) < anio ? `${anio}-01-01` : `${anio}-12-31`;
      }
      const ivaBruto = o.iva == null || typeof o.iva === 'string' ? NaN : num(o.iva, NaN);
      const ivaPorcentaje = Number.isFinite(ivaBruto) && ivaBruto > 0 ? (ivaBruto <= 1 ? ivaBruto * 100 : ivaBruto) : ivaPorDefecto;
      const datos = documentoSchema.parse({
        tipo,
        fecha,
        clienteId,
        distribuidorId: mapaDist.get(o.idDistribuidor) ?? null,
        lineas: lineas
          .filter((l) => l[pk] === o[pk])
          .map((l) => ({
            productoId: mapaProd.get(l.idProducto) ?? null,
            nombreProducto: str(l.nomProducto) || '(producto)',
            cantidad: num(l.cantidad, 1),
            precio: num(l.precio),
          })),
        ganancia: num(o.ganancia),
        restaurante: num(o.restaurante),
        pasaje: num(o.pasaje),
        combustible: num(o.combustible),
        otros: num(o.otros),
        hotel: num(o.hotel),
        transporte: num(o.transporte),
        kilometros: num(o.kilometros),
        numViajes: Math.trunc(num(o.totViajes, 2)),
        precioGasolina: num(o.precioGasolina, 1),
        aplicaGanancia: bool(o.isGanancia, true),
        aplicaIva: bool(o.isCanarias, true),
        ivaPorcentaje: Math.round(ivaPorcentaje * 100) / 100,
        totalManualActivo: bool(o.isTotalManual, false),
        totalManual: num(o.totalManual),
        porcentajeReparto: Math.min(100, Math.max(0, Math.trunc(num(o.porcentaje, 50)))),
        textoConcepto: str(o.textoLinea),
        textoFormaPago: str(o.textoFormaPago),
        textoExplicativo: str(o.textoExplicativo),
        calcTipo: bool(o.stepper, false) ? 'stepper' : 'pitagoras',
        valorA: num(o.valorA),
        valorB: num(o.valorB),
        valorC: num(o.valorC),
        valorAux: num(o.valorAux),
      });
      await insertarDocumento(conn, datos, { numero });
      contar(`${tabla} importados`);
    }
    if (maximo > 0) await ajustarContador(conn, tipo, anio, maximo);
  }

  if (dryRun) {
    await conn.rollback();
  } else {
    await conn.commit();
    for (const { origen, clienteId } of copias) {
      const ext = path.extname(origen).toLowerCase().slice(1);
      const mime = { jpg: 'image/jpeg', jpeg: 'image/jpeg', png: 'image/png', gif: 'image/gif', webp: 'image/webp', pdf: 'application/pdf' }[ext];
      if (!mime || !fs.statSync(origen).isFile()) {
        contar('adjuntos omitidos (tipo no admitido)');
        continue;
      }
      const nombre = path.basename(origen);
      const [ya] = await consultar(pool, 'SELECT id FROM adjuntos WHERE cliente_id = ? AND nombre_original = ?', [clienteId, nombre]);
      if (ya) {
        contar('adjuntos ya importados');
        continue;
      }
      const destinoDir = path.join(uploadsDir, String(clienteId));
      await fsp.mkdir(destinoDir, { recursive: true });
      const nuevo = `${crypto.randomUUID()}.${ext === 'jpeg' ? 'jpg' : ext}`;
      await fsp.copyFile(origen, path.join(destinoDir, nuevo));
      const { size } = await fsp.stat(origen);
      await ejecutar(pool, 'INSERT INTO adjuntos (cliente_id, tipo, nombre_original, fichero, mime, tamano) VALUES (?,?,?,?,?,?)', [
        clienteId,
        mime === 'application/pdf' ? 'pdf' : 'imagen',
        nombre,
        nuevo,
        mime,
        size,
      ]);
      contar('adjuntos importados');
    }
  }
} catch (e) {
  await conn.rollback();
  throw e;
} finally {
  conn.release();
  await pool.end();
  sq.close();
}

console.log(`\nImportación de ${path.basename(fichero)} (año ${anio})${dryRun ? ' — SIMULACIÓN, no se ha guardado nada' : ''}`);
for (const [k, v] of Object.entries(resumen)) console.log(`  ${k}: ${v}`);
if (dryRun && copias.length) console.log(`  adjuntos que se copiarían: ${copias.length}`);
if (avisos.length) {
  console.log('\nAvisos:');
  for (const a of avisos) console.log(`  - ${a}`);
}
