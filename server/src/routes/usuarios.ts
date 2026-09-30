import type { FastifyInstance } from 'fastify';
import { usuarioCrearSchema, usuarioEditarSchema } from '@cabp/shared';
import { consultar, ejecutar } from '../db/pool';
import { exigirAdmin, hashPassword } from '../auth/sesiones';
import { conflicto, noEncontrado, peticionIncorrecta } from '../errores';
import { idParam } from './util';

const mapear = (f: any) => ({ id: f.id, email: f.email, nombre: f.nombre, rol: f.rol, activo: !!f.activo });

export async function rutasUsuarios(app: FastifyInstance) {
  app.addHook('preHandler', async (req) => exigirAdmin(req));

  app.get('/usuarios', async () =>
    (await consultar(app.db, 'SELECT id, email, nombre, rol, activo FROM usuarios ORDER BY nombre')).map(mapear),
  );

  app.post('/usuarios', async (req, reply) => {
    const d = usuarioCrearSchema.parse(req.body);
    const [existe] = await consultar(app.db, 'SELECT id FROM usuarios WHERE email = ?', [d.email]);
    if (existe) throw conflicto('Ya existe un usuario con ese email');
    const r = await ejecutar(app.db, 'INSERT INTO usuarios (email, nombre, password_hash, rol) VALUES (?,?,?,?)', [
      d.email,
      d.nombre,
      await hashPassword(d.password),
      d.rol,
    ]);
    reply.status(201);
    return { id: r.insertId, email: d.email, nombre: d.nombre, rol: d.rol, activo: true };
  });

  app.put('/usuarios/:id', async (req) => {
    const id = idParam(req);
    const d = usuarioEditarSchema.parse(req.body);
    if (id === req.usuario!.id && (d.rol !== 'admin' || !d.activo)) {
      throw peticionIncorrecta('No puedes quitarte el rol de administrador ni desactivarte a ti mismo');
    }
    const r = await ejecutar(app.db, 'UPDATE usuarios SET nombre = ?, rol = ?, activo = ? WHERE id = ?', [
      d.nombre,
      d.rol,
      d.activo,
      id,
    ]);
    if (!r.affectedRows) throw noEncontrado('Usuario');
    if (d.password) {
      await ejecutar(app.db, 'UPDATE usuarios SET password_hash = ? WHERE id = ?', [await hashPassword(d.password), id]);
    }
    if (d.password || !d.activo) await ejecutar(app.db, 'DELETE FROM sesiones WHERE usuario_id = ?', [id]);
    const [f] = await consultar(app.db, 'SELECT * FROM usuarios WHERE id = ?', [id]);
    return mapear(f);
  });

  app.delete('/usuarios/:id', async (req) => {
    const id = idParam(req);
    if (id === req.usuario!.id) throw peticionIncorrecta('No puedes borrarte a ti mismo');
    const r = await ejecutar(app.db, 'DELETE FROM usuarios WHERE id = ?', [id]);
    if (!r.affectedRows) throw noEncontrado('Usuario');
    return { ok: true };
  });
}
