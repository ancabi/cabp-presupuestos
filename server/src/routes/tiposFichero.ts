import type { FastifyInstance } from 'fastify';
import { tipoFicheroSchema, type TipoFichero } from '@cabp/shared';
import { exigirAdmin } from '../auth/sesiones';
import { consultar, ejecutar } from '../db/pool';
import { conflicto, noEncontrado } from '../errores';
import { idParam } from './util';

/**
 * Tipos de los ficheros de pedidos. Se pueden crear y renombrar, pero no borrar:
 * dejaría ficheros con un tipo huérfano.
 */
export async function rutasTiposFichero(app: FastifyInstance) {
  const listar = async (): Promise<TipoFichero[]> =>
    (await consultar(app.db, 'SELECT id, nombre, sistema FROM tipos_fichero ORDER BY sistema DESC, nombre')).map((t) => ({
      id: t.id,
      nombre: t.nombre,
      sistema: !!t.sistema,
    }));

  const duplicado = (e: unknown) => {
    if ((e as { code?: string }).code === 'ER_DUP_ENTRY') throw conflicto('Ya existe un tipo con ese nombre');
    throw e;
  };

  app.get('/tipos-fichero', listar);

  app.post('/tipos-fichero', async (req, reply) => {
    exigirAdmin(req);
    const { nombre } = tipoFicheroSchema.parse(req.body);
    await ejecutar(app.db, 'INSERT INTO tipos_fichero (nombre) VALUES (?)', [nombre]).catch(duplicado);
    reply.status(201);
    return listar();
  });

  app.put('/tipos-fichero/:id', async (req) => {
    exigirAdmin(req);
    const { nombre } = tipoFicheroSchema.parse(req.body);
    const r = await ejecutar(app.db, 'UPDATE tipos_fichero SET nombre = ? WHERE id = ?', [nombre, idParam(req)]).catch(duplicado);
    if (!r.affectedRows) throw noEncontrado('Tipo');
    return listar();
  });
}
