import type { FastifyInstance } from 'fastify';
import { cambiarPasswordSchema, loginSchema } from '@cabp/shared';
import { consultar, ejecutar } from '../db/pool';
import { HttpError } from '../errores';
import {
  COOKIE_SESION,
  borrarSesion,
  comprobarPassword,
  crearSesion,
  hashPassword,
  ponerCookie,
  tokenDeCookie,
} from '../auth/sesiones';

export async function rutasAuth(app: FastifyInstance) {
  app.post(
    '/auth/login',
    { config: { publica: true, rateLimit: { max: 10, timeWindow: '5 minutes' } } },
    async (req, reply) => {
      const { email, password } = loginSchema.parse(req.body);
      const [u] = await consultar(app.db, 'SELECT * FROM usuarios WHERE email = ? AND activo = 1', [email]);
      if (!u || !(await comprobarPassword(u.password_hash, password))) {
        throw new HttpError(401, 'Email o contraseña incorrectos');
      }
      const { token, expira } = await crearSesion(app.db, u.id);
      // Cookie "Secure" solo si la petición llegó por HTTPS (trustProxy lee X-Forwarded-Proto del proxy).
      const https = req.protocol === 'https';
      if (app.config.produccion && !https) {
        req.log.warn('Login por HTTP: configura HTTPS en el proxy para no enviar contraseñas sin cifrar');
      }
      ponerCookie(reply, token, expira, https);
      return { id: u.id, email: u.email, nombre: u.nombre, rol: u.rol, activo: true };
    },
  );

  app.post('/auth/logout', { config: { publica: true } }, async (req, reply) => {
    const token = tokenDeCookie(req);
    if (token) await borrarSesion(app.db, token);
    reply.clearCookie(COOKIE_SESION, { path: '/' });
    return { ok: true };
  });

  app.get('/auth/me', async (req) => req.usuario);

  app.post('/auth/password', async (req) => {
    const { actual, nueva } = cambiarPasswordSchema.parse(req.body);
    const [u] = await consultar(app.db, 'SELECT password_hash FROM usuarios WHERE id = ?', [req.usuario!.id]);
    if (!u || !(await comprobarPassword(u.password_hash, actual))) {
      throw new HttpError(400, 'La contraseña actual no es correcta');
    }
    await ejecutar(app.db, 'UPDATE usuarios SET password_hash = ? WHERE id = ?', [
      await hashPassword(nueva),
      req.usuario!.id,
    ]);
    return { ok: true };
  });
}
