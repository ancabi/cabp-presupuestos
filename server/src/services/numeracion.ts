import type { TipoDocumento } from '@cabp/shared';
import { consultar, ejecutar, type Db } from '../db/pool';

/**
 * Reserva el siguiente número de un tipo de documento para un año.
 * Debe llamarse dentro de una transacción: la fila del contador queda bloqueada
 * hasta el commit, así que dos peticiones simultáneas nunca obtienen el mismo número.
 */
export async function siguienteNumero(conn: Db, tipo: TipoDocumento, anio: number): Promise<number> {
  await ejecutar(
    conn,
    `INSERT INTO contadores (tipo, anio, ultimo_numero) VALUES (?, ?, LAST_INSERT_ID(1))
     ON DUPLICATE KEY UPDATE ultimo_numero = LAST_INSERT_ID(ultimo_numero + 1)`,
    [tipo, anio],
  );
  const [f] = await consultar(conn, 'SELECT LAST_INSERT_ID() AS n');
  return Number(f.n);
}

/**
 * Deja el contador en el número más alto usado ese año, tras asignar o cambiar un número a mano.
 * Así el siguiente número automático nunca choca con uno puesto a mano, y si se renumera la
 * última factura el contador baja con ella (borrarDocumento compara contra el contador).
 * Nunca baja del primer número del año menos uno (ver fijarPrimerNumero).
 */
export async function recalcularContador(conn: Db, tipo: TipoDocumento, anio: number): Promise<void> {
  const [f] = await consultar(conn, 'SELECT COALESCE(MAX(numero), 0) AS n FROM documentos WHERE tipo = ? AND anio = ?', [tipo, anio]);
  await ejecutar(
    conn,
    `INSERT INTO contadores (tipo, anio, ultimo_numero) VALUES (?, ?, ?)
     ON DUPLICATE KEY UPDATE ultimo_numero = GREATEST(VALUES(ultimo_numero), primer_numero - 1)`,
    [tipo, anio, Number(f.n)],
  );
}

/**
 * Hace que la numeración de ese año empiece en `primero` (p. ej. facturas desde la 522 porque las
 * anteriores se hicieron con otro programa). Si ya hay números más altos, se sigue a partir de ellos.
 */
export async function fijarPrimerNumero(conn: Db, tipo: TipoDocumento, anio: number, primero: number): Promise<void> {
  await ejecutar(
    conn,
    `INSERT INTO contadores (tipo, anio, ultimo_numero, primer_numero) VALUES (?, ?, 0, ?)
     ON DUPLICATE KEY UPDATE primer_numero = VALUES(primer_numero)`,
    [tipo, anio, primero],
  );
  await recalcularContador(conn, tipo, anio);
}

/** Asegura que el contador está al menos en `numero` (usado por la importación). */
export async function ajustarContador(conn: Db, tipo: TipoDocumento, anio: number, numero: number): Promise<void> {
  await ejecutar(
    conn,
    `INSERT INTO contadores (tipo, anio, ultimo_numero) VALUES (?, ?, ?)
     ON DUPLICATE KEY UPDATE ultimo_numero = GREATEST(ultimo_numero, VALUES(ultimo_numero))`,
    [tipo, anio, numero],
  );
}
