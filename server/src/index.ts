import fs from 'node:fs';
import { leerConfig } from './config';
import { crearPool } from './db/pool';
import { migrar } from './db/migrar';
import { crearApp } from './app';

const config = leerConfig();
const pool = crearPool(config.databaseUrl);
fs.mkdirSync(config.uploadsDir, { recursive: true });
try {
  fs.accessSync(config.uploadsDir, fs.constants.W_OK);
} catch {
  const uid = process.getuid?.() ?? '?';
  console.error(
    `No se puede escribir en la carpeta de adjuntos ${config.uploadsDir} (usuario del proceso: uid ${uid}).\n` +
      `Dale permisos con: chown -R ${uid}:${uid} ${config.uploadsDir}  (o la carpeta del volumen en el servidor),\n` +
      'o revisa UPLOADS_DIR y la ruta donde está montado el volumen.',
  );
  process.exit(1);
}

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
