import crypto from 'node:crypto';
import fs from 'node:fs';
import fsp from 'node:fs/promises';
import path from 'node:path';
import { pipeline } from 'node:stream/promises';
import type { FastifyInstance } from 'fastify';
import type { Adjunto } from '@cabp/shared';
import { consultar, ejecutar } from '../db/pool';
import { noEncontrado, peticionIncorrecta } from '../errores';
import { idParam } from './util';

const TIPOS: Record<string, { ext: string; tipo: 'imagen' | 'pdf' }> = {
  'image/jpeg': { ext: 'jpg', tipo: 'imagen' },
  'image/png': { ext: 'png', tipo: 'imagen' },
  'image/gif': { ext: 'gif', tipo: 'imagen' },
  'image/webp': { ext: 'webp', tipo: 'imagen' },
  'application/pdf': { ext: 'pdf', tipo: 'pdf' },
};

const mapear = (f: any): Adjunto => ({
  id: f.id,
  clienteId: f.cliente_id,
  tipo: f.tipo,
  nombreOriginal: f.nombre_original,
  mime: f.mime,
  tamano: f.tamano,
  subidoEn: f.subido_en,
});

export async function rutasAdjuntos(app: FastifyInstance) {
  const rutaFichero = (clienteId: number, fichero: string) =>
    path.join(app.config.uploadsDir, String(clienteId), path.basename(fichero));

  app.get('/clientes/:id/adjuntos', async (req) =>
    (await consultar(app.db, 'SELECT * FROM adjuntos WHERE cliente_id = ? ORDER BY subido_en DESC, id DESC', [idParam(req)])).map(
      mapear,
    ),
  );

  app.post('/clientes/:id/adjuntos', async (req, reply) => {
    const clienteId = idParam(req);
    const [c] = await consultar(app.db, 'SELECT id FROM clientes WHERE id = ?', [clienteId]);
    if (!c) throw noEncontrado('Cliente');
    const dir = path.join(app.config.uploadsDir, String(clienteId));
    await fsp.mkdir(dir, { recursive: true });

    const creados: Adjunto[] = [];
    for await (const parte of req.files()) {
      const info = TIPOS[parte.mimetype];
      if (!info) {
        parte.file.resume();
        throw peticionIncorrecta(`Tipo de fichero no permitido: ${parte.filename}`);
      }
      const fichero = `${crypto.randomUUID()}.${info.ext}`;
      const destino = path.join(dir, fichero);
      await pipeline(parte.file, fs.createWriteStream(destino));
      if (parte.file.truncated) {
        await fsp.rm(destino, { force: true });
        throw peticionIncorrecta(`El fichero ${parte.filename} supera el tamaño máximo`);
      }
      const { size } = await fsp.stat(destino);
      const r = await ejecutar(
        app.db,
        'INSERT INTO adjuntos (cliente_id, tipo, nombre_original, fichero, mime, tamano) VALUES (?,?,?,?,?,?)',
        [clienteId, info.tipo, parte.filename.slice(0, 255), fichero, parte.mimetype, size],
      );
      const [f] = await consultar(app.db, 'SELECT * FROM adjuntos WHERE id = ?', [r.insertId]);
      creados.push(mapear(f));
    }
    reply.status(201);
    return creados;
  });

  app.get('/adjuntos/:id/fichero', async (req, reply) => {
    const [f] = await consultar(app.db, 'SELECT * FROM adjuntos WHERE id = ?', [idParam(req)]);
    if (!f) throw noEncontrado('Adjunto');
    const ruta = rutaFichero(f.cliente_id, f.fichero);
    if (!fs.existsSync(ruta)) throw noEncontrado('Fichero');
    const descargar = (req.query as Record<string, string>).descargar === '1';
    const nombre = encodeURIComponent(f.nombre_original);
    reply
      .header('Content-Type', f.mime)
      .header('Content-Disposition', `${descargar ? 'attachment' : 'inline'}; filename*=UTF-8''${nombre}`)
      .header('X-Content-Type-Options', 'nosniff')
      .header('Cache-Control', 'private, max-age=3600');
    return reply.send(fs.createReadStream(ruta));
  });

  app.delete('/adjuntos/:id', async (req) => {
    const [f] = await consultar(app.db, 'SELECT * FROM adjuntos WHERE id = ?', [idParam(req)]);
    if (!f) throw noEncontrado('Adjunto');
    await ejecutar(app.db, 'DELETE FROM adjuntos WHERE id = ?', [f.id]);
    await fsp.rm(rutaFichero(f.cliente_id, f.fichero), { force: true });
    return { ok: true };
  });
}
