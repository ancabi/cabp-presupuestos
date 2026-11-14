import { numeroDocumento, type TipoDocumento } from '@cabp/shared';
import type { Pool } from 'mysql2/promise';
import { consultar, ejecutar, transaccion, type Db } from '../db/pool';
import { fijarPrimerNumero, recalcularContador } from './numeracion';

export interface DocumentoPrueba {
  id: number;
  codigo: string;
  fecha: string;
  cliente: string;
  totalConIva: number;
  pedidoId: number | null;
  pedidoNombre: string | null;
  numFicheros: number;
}

/**
 * Presupuestos y facturas de un año hechos en esta aplicación (los importados de la versión
 * anterior no tienen usuario creador y no se tocan). Son los candidatos a «documentos de prueba».
 */
export async function documentosDePrueba(db: Db, anio: number): Promise<DocumentoPrueba[]> {
  const filas = await consultar(
    db,
    `SELECT d.id, d.tipo, d.anio, d.numero, d.fecha, d.total_con_iva, d.pedido_id,
            TRIM(CONCAT(c.nombre, ' ', c.apellidos)) AS cliente, p.nombre AS pedido_nombre,
            (SELECT COUNT(*) FROM pedido_ficheros pf WHERE pf.pedido_id = d.pedido_id) AS num_ficheros
       FROM documentos d
       JOIN clientes c ON c.id = d.cliente_id
       LEFT JOIN pedidos p ON p.id = d.pedido_id
      WHERE d.anio = ? AND d.creado_por IS NOT NULL
      ORDER BY d.tipo DESC, d.numero`,
    [anio],
  );
  return filas.map((f) => ({
    id: f.id,
    codigo: numeroDocumento(f.tipo as TipoDocumento, f.anio, f.numero),
    fecha: f.fecha,
    cliente: f.cliente,
    totalConIva: f.total_con_iva,
    pedidoId: f.pedido_id,
    pedidoNombre: f.pedido_nombre,
    numFicheros: Number(f.num_ficheros),
  }));
}

/**
 * Borra esos documentos y los pedidos que se abrieron con sus facturas (los ficheros de esos pedidos
 * los borra quien llama, con los ids devueltos). Deja los contadores del año al día y, si se indica,
 * hace que las facturas del año empiecen en `primeraFactura`.
 */
export async function borrarDocumentosDePrueba(
  pool: Pool,
  anio: number,
  primeraFactura?: number,
): Promise<{ documentos: number; pedidoIds: number[] }> {
  return transaccion(pool, async (conn) => {
    const docs = await documentosDePrueba(conn, anio);
    const ids = docs.map((d) => d.id);
    const pedidoIds = docs.map((d) => d.pedidoId).filter((x): x is number => x != null);
    if (ids.length) await ejecutar(conn, 'DELETE FROM documentos WHERE id IN (?)', [ids]);
    if (pedidoIds.length) await ejecutar(conn, 'DELETE FROM pedidos WHERE id IN (?)', [pedidoIds]);
    await recalcularContador(conn, 'presupuesto', anio);
    if (primeraFactura) await fijarPrimerNumero(conn, 'factura', anio, primeraFactura);
    else await recalcularContador(conn, 'factura', anio);
    return { documentos: ids.length, pedidoIds };
  });
}
