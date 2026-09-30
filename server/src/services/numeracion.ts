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

/** Asegura que el contador está al menos en `numero` (usado por la importación). */
export async function ajustarContador(conn: Db, tipo: TipoDocumento, anio: number, numero: number): Promise<void> {
  await ejecutar(
    conn,
    `INSERT INTO contadores (tipo, anio, ultimo_numero) VALUES (?, ?, ?)
     ON DUPLICATE KEY UPDATE ultimo_numero = GREATEST(ultimo_numero, VALUES(ultimo_numero))`,
    [tipo, anio, numero],
  );
}
