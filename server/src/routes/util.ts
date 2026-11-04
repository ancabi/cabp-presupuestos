import type { FastifyRequest } from 'fastify';
import { peticionIncorrecta } from '../errores';

export function idParam(req: FastifyRequest, nombre = 'id'): number {
  const v = Number((req.params as Record<string, string>)[nombre]);
  if (!Number.isInteger(v) || v <= 0) throw peticionIncorrecta('Identificador no válido');
  return v;
}

export function queryStr(req: FastifyRequest, nombre: string): string | undefined {
  const v = (req.query as Record<string, unknown>)[nombre];
  return typeof v === 'string' && v !== '' ? v : undefined;
}

/** Trimestre (1-4) del parámetro `trimestre`, o undefined si no viene. */
export function queryTrimestre(req: FastifyRequest): number | undefined {
  const t = queryInt(req, 'trimestre');
  if (t !== undefined && (t < 1 || t > 4)) throw peticionIncorrecta('Parámetro trimestre no válido');
  return t;
}

export function queryInt(req: FastifyRequest, nombre: string): number | undefined {
  const v = queryStr(req, nombre);
  if (v === undefined) return undefined;
  const n = Number(v);
  if (!Number.isInteger(n)) throw peticionIncorrecta(`Parámetro ${nombre} no válido`);
  return n;
}
