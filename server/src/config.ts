import fs from 'node:fs';
import path from 'node:path';
import { fileURLToPath } from 'node:url';

/** Raíz del repositorio (carpeta que contiene server/ y web/), sea cual sea el punto de entrada. */
function raizProyecto(): string {
  let dir = path.dirname(fileURLToPath(import.meta.url));
  for (let i = 0; i < 5; i++) {
    if (fs.existsSync(path.join(dir, 'server')) && fs.existsSync(path.join(dir, 'shared'))) return dir;
    dir = path.dirname(dir);
  }
  return process.cwd();
}

/** Las rutas relativas de las variables de entorno se interpretan desde la raíz del proyecto. */
export function rutaProyecto(p: string): string {
  return path.resolve(raizProyecto(), p);
}

export const rutaUploads = () => rutaProyecto(process.env.UPLOADS_DIR ?? './uploads');

function requerida(nombre: string): string {
  const v = process.env[nombre];
  if (!v) throw new Error(`Falta la variable de entorno ${nombre}`);
  return v;
}

export interface Config {
  databaseUrl: string;
  sessionSecret: string;
  uploadsDir: string;
  port: number;
  produccion: boolean;
  webDist: string | null;
}

export function leerConfig(): Config {
  const produccion = process.env.NODE_ENV === 'production';
  const secret = requerida('SESSION_SECRET');
  if (produccion && secret.length < 32) throw new Error('SESSION_SECRET debe tener al menos 32 caracteres');
  return {
    databaseUrl: requerida('DATABASE_URL'),
    sessionSecret: secret,
    uploadsDir: rutaUploads(),
    port: Number(process.env.PORT ?? 3000),
    produccion,
    webDist: rutaProyecto(process.env.WEB_DIST ?? './web/dist'),
  };
}
