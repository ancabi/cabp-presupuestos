import crypto from 'node:crypto';
import { hash, verify } from '@node-rs/argon2';
import type { FastifyReply, FastifyRequest } from 'fastify';
import type { Usuario } from '@cabp/shared';
import { consultar, ejecutar, type Db } from '../db/pool';
import { HttpError } from '../errores';

export const COOKIE_SESION = 'cabp_sesion';
const DURACION_MS = 7 * 24 * 60 * 60 * 1000;

const sha256 = (t: string) => crypto.createHash('sha256').update(t).digest('hex');

export const hashPassword = (p: string) => hash(p);
export const comprobarPassword = (h: string, p: string) => verify(h, p).catch(() => false);

export async function crearSesion(db: Db, usuarioId: number): Promise<{ token: string; expira: Date }> {
  const token = crypto.randomBytes(32).toString('base64url');
  const expira = new Date(Date.now() + DURACION_MS);
  await ejecutar(db, 'INSERT INTO sesiones (token_hash, usuario_id, expira_en) VALUES (?, ?, ?)', [
    sha256(token),
    usuarioId,
    expira,
  ]);
  // Limpieza oportunista de sesiones caducadas.
  await ejecutar(db, 'DELETE FROM sesiones WHERE expira_en < UTC_TIMESTAMP()');
  return { token, expira };
}

export async function borrarSesion(db: Db, token: string): Promise<void> {
  await ejecutar(db, 'DELETE FROM sesiones WHERE token_hash = ?', [sha256(token)]);
}

export async function usuarioDeSesion(db: Db, token: string): Promise<Usuario | null> {
  const filas = await consultar(
    db,
    `SELECT u.id, u.email, u.nombre, u.rol, u.activo
       FROM sesiones s JOIN usuarios u ON u.id = s.usuario_id
      WHERE s.token_hash = ? AND s.expira_en > UTC_TIMESTAMP() AND u.activo = 1`,
    [sha256(token)],
  );
  const f = filas[0];
  if (!f) return null;
  return { id: f.id, email: f.email, nombre: f.nombre, rol: f.rol, activo: !!f.activo };
}

export function ponerCookie(reply: FastifyReply, token: string, expira: Date, segura: boolean) {
  reply.setCookie(COOKIE_SESION, token, {
    path: '/',
    httpOnly: true,
    sameSite: 'lax',
    secure: segura,
    expires: expira,
    signed: true,
  });
}

export function tokenDeCookie(req: FastifyRequest): string | null {
  const raw = req.cookies[COOKIE_SESION];
  if (!raw) return null;
  const r = req.unsignCookie(raw);
  return r.valid ? r.value : null;
}

export function exigirAdmin(req: FastifyRequest) {
  if (req.usuario?.rol !== 'admin') throw new HttpError(403, 'Solo para administradores');
}
