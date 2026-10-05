import fsp from 'node:fs/promises';
import path from 'node:path';
import type { FastifyInstance } from 'fastify';
import type { Adjunto } from '@cabp/shared';
import { consultar, ejecutar } from '../db/pool';
import { noEncontrado } from '../errores';
import { enviarFichero, guardarSubida } from '../services/ficheros';
import { idParam } from './util';

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

    const creados: Adjunto[] = [];
    for await (const parte of req.files()) {
      const g = await guardarSubida(parte, dir);
      const r = await ejecutar(
        app.db,
        'INSERT INTO adjuntos (cliente_id, tipo, nombre_original, fichero, mime, tamano) VALUES (?,?,?,?,?,?)',
        [clienteId, g.tipo, g.nombreOriginal, g.fichero, g.mime, g.tamano],
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
    const descargar = (req.query as Record<string, string>).descargar === '1';
    return enviarFichero(reply, rutaFichero(f.cliente_id, f.fichero), f, descargar);
  });

  app.delete('/adjuntos/:id', async (req) => {
    const [f] = await consultar(app.db, 'SELECT * FROM adjuntos WHERE id = ?', [idParam(req)]);
    if (!f) throw noEncontrado('Adjunto');
    await ejecutar(app.db, 'DELETE FROM adjuntos WHERE id = ?', [f.id]);
    await fsp.rm(rutaFichero(f.cliente_id, f.fichero), { force: true });
    return { ok: true };
  });
}
