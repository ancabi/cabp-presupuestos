import fs from 'node:fs';
import path from 'node:path';
import Fastify, { type FastifyInstance } from 'fastify';
import cookie from '@fastify/cookie';
import helmet from '@fastify/helmet';
import multipart from '@fastify/multipart';
import rateLimit from '@fastify/rate-limit';
import fastifyStatic from '@fastify/static';
import type { Pool } from 'mysql2/promise';
import { ZodError } from 'zod';
import type { Usuario } from '@cabp/shared';
import type { Config } from './config';
import { HttpError } from './errores';
import { tokenDeCookie, usuarioDeSesion } from './auth/sesiones';
import { rutasAuth } from './routes/auth';
import { rutasClientes } from './routes/clientes';
import { rutasAdjuntos } from './routes/adjuntos';
import { rutasDistribuidores } from './routes/distribuidores';
import { rutasDocumentos } from './routes/documentos';
import { rutasAjustes } from './routes/ajustes';
import { rutasUsuarios } from './routes/usuarios';

declare module 'fastify' {
  interface FastifyInstance {
    db: Pool;
    config: Config;
  }
  interface FastifyRequest {
    usuario: Usuario | null;
  }
}

export const MAX_SUBIDA = 20 * 1024 * 1024;

export async function crearApp(config: Config, db: Pool, opciones: { logger?: boolean } = {}): Promise<FastifyInstance> {
  const app = Fastify({ logger: opciones.logger ?? false, trustProxy: true });
  app.decorate('db', db);
  app.decorate('config', config);
  app.decorateRequest('usuario', null);

  await app.register(helmet, {
    contentSecurityPolicy: {
      directives: {
        defaultSrc: ["'self'"],
        imgSrc: ["'self'", 'data:', 'blob:', 'https:'],
        styleSrc: ["'self'", "'unsafe-inline'"],
        scriptSrc: ["'self'"],
        objectSrc: ["'none'"],
        frameSrc: ["'self'"],
        upgradeInsecureRequests: config.produccion ? [] : null,
      },
    },
  });
  await app.register(cookie, { secret: config.sessionSecret });
  await app.register(rateLimit, { global: false });
  await app.register(multipart, { limits: { fileSize: MAX_SUBIDA, files: 10 } });

  app.setErrorHandler((err, req, reply) => {
    if (err instanceof ZodError) {
      const msg = err.issues.map((i) => (i.path.length ? `${i.path.join('.')}: ${i.message}` : i.message)).join('; ');
      return reply.status(400).send({ error: msg });
    }
    if (err instanceof HttpError) return reply.status(err.statusCode).send({ error: err.message });
    const code = (err as { code?: string }).code;
    if (code === 'ER_ROW_IS_REFERENCED_2' || code === 'ER_ROW_IS_REFERENCED') {
      return reply.status(409).send({ error: 'No se puede borrar: tiene datos relacionados' });
    }
    const e = err as { statusCode?: number; message?: string };
    if (e.statusCode && e.statusCode < 500) return reply.status(e.statusCode).send({ error: e.message });
    req.log.error(err);
    return reply.status(500).send({ error: 'Error interno del servidor' });
  });

  await app.register(
    async (api) => {
      // Autenticación y protección CSRF para toda la API.
      api.addHook('onRequest', async (req) => {
        const token = tokenDeCookie(req);
        req.usuario = token ? await usuarioDeSesion(db, token) : null;
        const publica = req.routeOptions.config && (req.routeOptions.config as { publica?: boolean }).publica;
        if (!publica && !req.usuario) throw new HttpError(401, 'No autenticado');
        if (!['GET', 'HEAD', 'OPTIONS'].includes(req.method) && req.headers['x-requested-with'] !== 'cabp') {
          throw new HttpError(403, 'Petición no permitida');
        }
      });
      await api.register(rutasAuth);
      await api.register(rutasClientes);
      await api.register(rutasAdjuntos);
      await api.register(rutasDistribuidores);
      await api.register(rutasDocumentos);
      await api.register(rutasAjustes);
      await api.register(rutasUsuarios);
      api.setNotFoundHandler((_req, reply) => reply.status(404).send({ error: 'Ruta no encontrada' }));
    },
    { prefix: '/api' },
  );

  // Frontend compilado (SPA): cualquier ruta que no sea /api devuelve index.html.
  if (config.webDist && fs.existsSync(path.join(config.webDist, 'index.html'))) {
    await app.register(fastifyStatic, { root: config.webDist, wildcard: false });
    app.setNotFoundHandler((req, reply) => {
      if (req.method === 'GET' && !req.url.startsWith('/api')) return reply.sendFile('index.html');
      return reply.status(404).send({ error: 'Ruta no encontrada' });
    });
  }

  return app;
}
