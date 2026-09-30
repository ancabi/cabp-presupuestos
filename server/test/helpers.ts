import fs from 'node:fs';
import os from 'node:os';
import path from 'node:path';
import type { FastifyInstance } from 'fastify';
import type { Pool } from 'mysql2/promise';
import { crearApp } from '../src/app';
import { crearPool, consultar, ejecutar } from '../src/db/pool';
import { migrar } from '../src/db/migrar';
import { hashPassword } from '../src/auth/sesiones';

export const TEST_DB = process.env.TEST_DATABASE_URL ?? 'mysql://cabp:cabp@localhost:3306/cabp_test';

export async function prepararApp(): Promise<{ app: FastifyInstance; pool: Pool; cookie: string; uploads: string }> {
  const pool = crearPool(TEST_DB);
  await ejecutar(pool, 'SET FOREIGN_KEY_CHECKS = 0');
  for (const t of await consultar(pool, 'SHOW TABLES')) await ejecutar(pool, `DROP TABLE \`${Object.values(t)[0]}\``);
  await ejecutar(pool, 'SET FOREIGN_KEY_CHECKS = 1');
  await migrar(pool);
  await ejecutar(pool, "INSERT INTO usuarios (email, nombre, password_hash, rol) VALUES ('admin@test.es', 'Admin', ?, 'admin')", [
    await hashPassword('secreto123'),
  ]);
  const uploads = fs.mkdtempSync(path.join(os.tmpdir(), 'cabp-uploads-'));
  const app = await crearApp(
    { databaseUrl: TEST_DB, sessionSecret: 'x'.repeat(40), uploadsDir: uploads, port: 0, produccion: false, webDist: null },
    pool,
  );
  const r = await app.inject({
    method: 'POST',
    url: '/api/auth/login',
    headers: { 'x-requested-with': 'cabp' },
    payload: { email: 'admin@test.es', password: 'secreto123' },
  });
  if (r.statusCode !== 200) throw new Error('login falló: ' + r.body);
  const c = r.cookies[0]!;
  return { app, pool, cookie: `${c.name}=${c.value}`, uploads };
}

export function cliente(app: FastifyInstance, cookie: string) {
  const pedir = async (method: string, url: string, payload?: unknown) => {
    const r = await app.inject({
      method: method as any,
      url,
      payload: payload as any,
      headers: { cookie, 'x-requested-with': 'cabp' },
    });
    return { status: r.statusCode, body: r.body ? JSON.parse(r.body) : null };
  };
  return {
    get: (u: string) => pedir('GET', u),
    post: (u: string, p?: unknown) => pedir('POST', u, p ?? {}),
    put: (u: string, p: unknown) => pedir('PUT', u, p),
    del: (u: string) => pedir('DELETE', u),
  };
}
