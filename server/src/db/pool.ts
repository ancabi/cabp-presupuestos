import mysql, { type Pool, type PoolConnection, type ResultSetHeader, type RowDataPacket } from 'mysql2/promise';

export type Db = Pool | PoolConnection;
export type Fila = RowDataPacket & Record<string, any>;

export function crearPool(url: string): Pool {
  return mysql.createPool({
    uri: url,
    connectionLimit: 10,
    dateStrings: true,
    decimalNumbers: true,
    supportBigNumbers: true,
    charset: 'utf8mb4',
    timezone: 'Z',
  });
}

export async function consultar<T = Fila>(db: Db, sql: string, params: unknown[] = []): Promise<T[]> {
  const [filas] = await db.query<Fila[]>(sql, params);
  return filas as unknown as T[];
}

export async function ejecutar(db: Db, sql: string, params: unknown[] = []): Promise<ResultSetHeader> {
  const [res] = await db.query<ResultSetHeader>(sql, params);
  return res;
}

export async function transaccion<T>(pool: Pool, fn: (conn: PoolConnection) => Promise<T>): Promise<T> {
  const conn = await pool.getConnection();
  try {
    await conn.beginTransaction();
    const res = await fn(conn);
    await conn.commit();
    return res;
  } catch (e) {
    await conn.rollback();
    throw e;
  } finally {
    conn.release();
  }
}
