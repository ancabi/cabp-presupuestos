import type { Pool } from 'mysql2/promise';
import { consultar, ejecutar } from './pool';
import { migraciones } from './migraciones';

export async function migrar(pool: Pool, log: (m: string) => void = () => {}): Promise<void> {
  await ejecutar(
    pool,
    `CREATE TABLE IF NOT EXISTS migraciones (
       nombre VARCHAR(100) PRIMARY KEY,
       aplicada_en DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
     ) ENGINE=InnoDB`,
  );
  const hechas = new Set(
    (await consultar<{ nombre: string }>(pool, 'SELECT nombre FROM migraciones')).map((f) => f.nombre),
  );
  for (const m of migraciones) {
    if (hechas.has(m.nombre)) continue;
    log(`Aplicando migración ${m.nombre}`);
    // MariaDB no permite DDL transaccional: se aplican las sentencias en orden.
    for (const sql of m.sql) await ejecutar(pool, sql);
    await ejecutar(pool, 'INSERT INTO migraciones (nombre) VALUES (?)', [m.nombre]);
  }
}
