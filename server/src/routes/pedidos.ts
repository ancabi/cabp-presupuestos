import fsp from 'node:fs/promises';
import path from 'node:path';
import type { FastifyInstance } from 'fastify';
import { pedidoSchema, type PedidoFichero } from '@cabp/shared';
import { z } from 'zod';
import { consultar, ejecutar, transaccion, type Fila } from '../db/pool';
import { conflicto, noEncontrado, peticionIncorrecta } from '../errores';
import { enviarFichero, guardarSubida } from '../services/ficheros';
import { crearPedido, listarPedidos, mapearFichero, obtenerPedido, tocarPedido } from '../services/pedidos';
import { obtenerDocumento } from '../services/documentos';
import { idParam, queryInt, queryStr } from './util';

const cambioTipoSchema = z.object({ tipoId: z.number().int().positive() });

export async function rutasPedidos(app: FastifyInstance) {
  const dirPedido = (pedidoId: number) => path.join(app.config.uploadsDir, 'pedidos', String(pedidoId));
  const rutaFichero = (f: Fila) => path.join(dirPedido(f.pedido_id), path.basename(f.fichero));

  const existeTipo = async (tipoId: number) => {
    const [t] = await consultar(app.db, 'SELECT id FROM tipos_fichero WHERE id = ?', [tipoId]);
    if (!t) throw peticionIncorrecta('El tipo de fichero no existe');
  };
  const leerFichero = async (id: number) => {
    const [f] = await consultar(app.db, 'SELECT * FROM pedido_ficheros WHERE id = ?', [id]);
    if (!f) throw noEncontrado('Fichero');
    return f;
  };

  app.get('/pedidos', async (req) => listarPedidos(app.db, { anio: queryInt(req, 'anio'), q: queryStr(req, 'q') }));

  app.get('/pedidos/:id', async (req) => obtenerPedido(app.db, idParam(req)));

  app.put('/pedidos/:id', async (req) => {
    const id = idParam(req);
    const { nombre } = pedidoSchema.parse(req.body);
    const r = await ejecutar(app.db, 'UPDATE pedidos SET nombre = ?, actualizado_en = CURRENT_TIMESTAMP WHERE id = ?', [nombre, id]);
    if (!r.affectedRows) throw noEncontrado('Pedido');
    return obtenerPedido(app.db, id);
  });

  app.post('/pedidos/:id/ficheros', async (req, reply) => {
    const pedidoId = idParam(req);
    const [p] = await consultar(app.db, 'SELECT id FROM pedidos WHERE id = ?', [pedidoId]);
    if (!p) throw noEncontrado('Pedido');
    const tipoId = queryInt(req, 'tipoId');
    if (!tipoId) throw peticionIncorrecta('Indica el tipo de fichero');
    await existeTipo(tipoId);

    const creados: PedidoFichero[] = [];
    for await (const parte of req.files()) {
      const g = await guardarSubida(parte, dirPedido(pedidoId));
      const r = await ejecutar(
        app.db,
        `INSERT INTO pedido_ficheros (pedido_id, tipo_id, nombre_original, fichero, mime, tamano, subido_por)
         VALUES (?,?,?,?,?,?,?)`,
        [pedidoId, tipoId, g.nombreOriginal, g.fichero, g.mime, g.tamano, req.usuario!.id],
      );
      const [f] = await consultar(
        app.db,
        'SELECT pf.*, t.nombre AS tipo_nombre FROM pedido_ficheros pf JOIN tipos_fichero t ON t.id = pf.tipo_id WHERE pf.id = ?',
        [r.insertId],
      );
      creados.push(mapearFichero(f));
    }
    await tocarPedido(app.db, pedidoId);
    reply.status(201);
    return creados;
  });

  app.patch('/pedido-ficheros/:id', async (req) => {
    const f = await leerFichero(idParam(req));
    const { tipoId } = cambioTipoSchema.parse(req.body);
    await existeTipo(tipoId);
    await ejecutar(app.db, 'UPDATE pedido_ficheros SET tipo_id = ? WHERE id = ?', [tipoId, f.id]);
    await tocarPedido(app.db, f.pedido_id);
    return { ok: true };
  });

  app.get('/pedido-ficheros/:id/fichero', async (req, reply) => {
    const f = await leerFichero(idParam(req));
    return enviarFichero(reply, rutaFichero(f), f, (req.query as Record<string, string>).descargar === '1');
  });

  app.delete('/pedido-ficheros/:id', async (req) => {
    const f = await leerFichero(idParam(req));
    await ejecutar(app.db, 'DELETE FROM pedido_ficheros WHERE id = ?', [f.id]);
    await tocarPedido(app.db, f.pedido_id);
    await fsp.rm(rutaFichero(f), { force: true });
    return { ok: true };
  });

  /** Abre el pedido de una factura que no lo tiene (facturas anteriores a los pedidos o importadas). */
  app.post('/documentos/:id/pedido', async (req, reply) => {
    const id = idParam(req);
    const { nombre } = pedidoSchema.parse(req.body);
    await transaccion(app.db, async (conn) => {
      const [d] = await consultar(conn, 'SELECT tipo, anio, cliente_id, pedido_id FROM documentos WHERE id = ? FOR UPDATE', [id]);
      if (!d) throw noEncontrado('Documento');
      if (d.tipo !== 'factura') throw peticionIncorrecta('Solo las facturas tienen pedido');
      if (d.pedido_id) throw conflicto('Esta factura ya tiene pedido');
      const pedidoId = await crearPedido(conn, { nombre, clienteId: d.cliente_id, anio: d.anio });
      await ejecutar(conn, 'UPDATE documentos SET pedido_id = ? WHERE id = ?', [pedidoId, id]);
    });
    reply.status(201);
    return obtenerDocumento(app.db, id);
  });
}
