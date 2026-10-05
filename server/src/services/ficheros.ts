import crypto from 'node:crypto';
import fs from 'node:fs';
import fsp from 'node:fs/promises';
import path from 'node:path';
import { pipeline } from 'node:stream/promises';
import type { FastifyReply } from 'fastify';
import type { MultipartFile } from '@fastify/multipart';
import type { Fila } from '../db/pool';
import { noEncontrado, peticionIncorrecta } from '../errores';

/** Tipos de fichero que se aceptan en adjuntos de clientes y en pedidos. */
export const TIPOS_PERMITIDOS: Record<string, { ext: string; tipo: 'imagen' | 'pdf' }> = {
  'image/jpeg': { ext: 'jpg', tipo: 'imagen' },
  'image/png': { ext: 'png', tipo: 'imagen' },
  'image/gif': { ext: 'gif', tipo: 'imagen' },
  'image/webp': { ext: 'webp', tipo: 'imagen' },
  'application/pdf': { ext: 'pdf', tipo: 'pdf' },
};

/** Guarda una parte multipart en `dir` con nombre aleatorio. Rechaza tipos no permitidos y ficheros demasiado grandes. */
export async function guardarSubida(parte: MultipartFile, dir: string) {
  const info = TIPOS_PERMITIDOS[parte.mimetype];
  if (!info) {
    parte.file.resume();
    throw peticionIncorrecta(`Tipo de fichero no permitido: ${parte.filename}`);
  }
  await fsp.mkdir(dir, { recursive: true });
  const fichero = `${crypto.randomUUID()}.${info.ext}`;
  const destino = path.join(dir, fichero);
  await pipeline(parte.file, fs.createWriteStream(destino));
  if (parte.file.truncated) {
    await fsp.rm(destino, { force: true });
    throw peticionIncorrecta(`El fichero ${parte.filename} supera el tamaño máximo`);
  }
  const { size } = await fsp.stat(destino);
  return {
    tipo: info.tipo,
    fichero,
    mime: parte.mimetype,
    tamano: size,
    nombreOriginal: parte.filename.slice(0, 255),
  };
}

/** Envía un fichero guardado, en línea o como descarga (`?descargar=1`). */
export function enviarFichero(
  reply: FastifyReply,
  ruta: string,
  f: Fila, // fila con mime y nombre_original
  descargar: boolean,
) {
  if (!fs.existsSync(ruta)) throw noEncontrado('Fichero');
  const nombre = encodeURIComponent(f.nombre_original);
  reply
    .header('Content-Type', f.mime)
    .header('Content-Disposition', `${descargar ? 'attachment' : 'inline'}; filename*=UTF-8''${nombre}`)
    .header('X-Content-Type-Options', 'nosniff')
    .header('Cache-Control', 'private, max-age=3600');
  return reply.send(fs.createReadStream(ruta));
}
