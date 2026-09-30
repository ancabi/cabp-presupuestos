import fs from 'node:fs/promises';
import path from 'node:path';
import type { FastifyInstance } from 'fastify';
import { clienteSchema } from '@cabp/shared';
import { consultar, ejecutar, transaccion } from '../db/pool';
import { conflicto, noEncontrado } from '../errores';
import { actualizarCliente, crearCliente, listarClientes, obtenerCliente } from '../services/clientes';
import { idParam, queryStr } from './util';

export async function rutasClientes(app: FastifyInstance) {
  app.get('/clientes', async (req) => listarClientes(app.db, queryStr(req, 'q')));

  app.get('/clientes/:id', async (req) => {
    const c = await obtenerCliente(app.db, idParam(req));
    if (!c) throw noEncontrado('Cliente');
    return c;
  });

  app.post('/clientes', async (req, reply) => {
    const d = clienteSchema.parse(req.body);
    const id = await transaccion(app.db, (conn) => crearCliente(conn, d));
    reply.status(201);
    return obtenerCliente(app.db, id);
  });

  app.put('/clientes/:id', async (req) => {
    const id = idParam(req);
    const d = clienteSchema.parse(req.body);
    const ok = await transaccion(app.db, (conn) => actualizarCliente(conn, id, d));
    if (!ok) throw noEncontrado('Cliente');
    return obtenerCliente(app.db, id);
  });

  app.delete('/clientes/:id', async (req) => {
    const id = idParam(req);
    const [docs] = await consultar(app.db, 'SELECT COUNT(*) AS n FROM documentos WHERE cliente_id = ?', [id]);
    if (docs.n > 0) {
      throw conflicto(`El cliente tiene ${docs.n} presupuesto(s)/factura(s). Bórralos antes de eliminar el cliente.`);
    }
    const r = await ejecutar(app.db, 'DELETE FROM clientes WHERE id = ?', [id]);
    if (!r.affectedRows) throw noEncontrado('Cliente');
    await fs.rm(path.join(app.config.uploadsDir, String(id)), { recursive: true, force: true });
    return { ok: true };
  });
}
