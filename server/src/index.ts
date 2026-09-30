import fs from 'node:fs';
import { leerConfig } from './config';
import { crearPool } from './db/pool';
import { migrar } from './db/migrar';
import { crearApp } from './app';

const config = leerConfig();
const pool = crearPool(config.databaseUrl);
fs.mkdirSync(config.uploadsDir, { recursive: true });

await migrar(pool, (m) => console.log(m));
const app = await crearApp(config, pool, { logger: true });

const cerrar = async () => {
  await app.close();
  await pool.end();
  process.exit(0);
};
process.on('SIGINT', cerrar);
process.on('SIGTERM', cerrar);

await app.listen({ port: config.port, host: '0.0.0.0' });
