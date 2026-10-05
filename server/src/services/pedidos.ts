import { numeroDocumento, type Pedido, type PedidoFichero, type PedidoResumen } from '@cabp/shared';
import { consultar, ejecutar, type Db } from '../db/pool';
import { noEncontrado } from '../errores';

export async function crearPedido(conn: Db, p: { nombre: string; clienteId: number; anio: number }): Promise<number> {
  const r = await ejecutar(conn, 'INSERT INTO pedidos (nombre, cliente_id, anio) VALUES (?, ?, ?)', [p.nombre, p.clienteId, p.anio]);
  return r.insertId;
}

/** Marca el pedido como modificado ahora (subir/borrar ficheros, renombrar, editar su factura). */
export async function tocarPedido(conn: Db, id: number): Promise<void> {
  await ejecutar(conn, 'UPDATE pedidos SET actualizado_en = CURRENT_TIMESTAMP WHERE id = ?', [id]);
}

const SELECT_RESUMEN = `
  SELECT p.*, TRIM(CONCAT(c.nombre, ' ', c.apellidos)) AS cliente_nombre,
         d.id AS factura_id, d.anio AS factura_anio, d.numero AS factura_numero,
         d.fecha AS factura_fecha, d.total_con_iva AS factura_total,
         (SELECT COUNT(*) FROM pedido_ficheros pf WHERE pf.pedido_id = p.id) AS num_ficheros
    FROM pedidos p
    JOIN clientes c ON c.id = p.cliente_id
    LEFT JOIN documentos d ON d.pedido_id = p.id`;

const mapearResumen = (f: any): PedidoResumen => ({
  id: f.id,
  nombre: f.nombre,
  anio: f.anio,
  clienteId: f.cliente_id,
  clienteNombre: f.cliente_nombre,
  facturaId: f.factura_id ?? null,
  facturaCodigo: f.factura_id ? numeroDocumento('factura', f.factura_anio, f.factura_numero) : null,
  creadoEn: f.creado_en,
  actualizadoEn: f.actualizado_en,
  numFicheros: Number(f.num_ficheros),
});

export const mapearFichero = (f: any): PedidoFichero => ({
  id: f.id,
  pedidoId: f.pedido_id,
  tipoId: f.tipo_id,
  tipoNombre: f.tipo_nombre,
  nombreOriginal: f.nombre_original,
  mime: f.mime,
  tamano: f.tamano,
  subidoEn: f.subido_en,
});

export async function listarPedidos(db: Db, filtro: { anio?: number; q?: string }): Promise<PedidoResumen[]> {
  const where: string[] = [];
  const params: unknown[] = [];
  if (filtro.anio) (where.push('p.anio = ?'), params.push(filtro.anio));
  if (filtro.q) {
    const like = `%${filtro.q.replace(/[\\%_]/g, (c) => '\\' + c)}%`;
    where.push("(p.nombre LIKE ? OR CONCAT(c.nombre, ' ', c.apellidos) LIKE ? OR c.empresa LIKE ?)");
    params.push(like, like, like);
  }
  const filas = await consultar(
    db,
    `${SELECT_RESUMEN} ${where.length ? 'WHERE ' + where.join(' AND ') : ''} ORDER BY p.actualizado_en DESC, p.id DESC`,
    params,
  );
  return filas.map(mapearResumen);
}

export async function obtenerPedido(db: Db, id: number): Promise<Pedido> {
  const [f] = await consultar(db, `${SELECT_RESUMEN} WHERE p.id = ?`, [id]);
  if (!f) throw noEncontrado('Pedido');
  const ficheros = await consultar(
    db,
    `SELECT pf.*, t.nombre AS tipo_nombre FROM pedido_ficheros pf JOIN tipos_fichero t ON t.id = pf.tipo_id
      WHERE pf.pedido_id = ? ORDER BY pf.subido_en DESC, pf.id DESC`,
    [id],
  );
  const r = mapearResumen(f);
  return {
    ...r,
    factura: f.factura_id
      ? { id: f.factura_id, codigo: r.facturaCodigo!, fecha: f.factura_fecha, totalConIva: f.factura_total }
      : null,
    ficheros: ficheros.map(mapearFichero),
  };
}
