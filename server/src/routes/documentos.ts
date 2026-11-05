import type { FastifyInstance } from 'fastify';
import { documentoSchema, type TipoDocumento } from '@cabp/shared';
import { peticionIncorrecta } from '../errores';
import {
  actualizarDocumento,
  aniosConDocumentos,
  borrarDocumento,
  convertirAFactura,
  crearDocumento,
  listarDocumentos,
  numeroLibre,
  obtenerDocumento,
  proximoNumero,
} from '../services/documentos';
import { cotizacionUsd } from '../services/cotizacion';
import { idParam, queryInt, queryStr, queryTrimestre } from './util';

function tipoQuery(v: string | undefined): TipoDocumento | undefined {
  if (v === undefined) return undefined;
  if (v !== 'presupuesto' && v !== 'factura') throw peticionIncorrecta('Tipo no válido');
  return v;
}

export async function rutasDocumentos(app: FastifyInstance) {
  app.get('/documentos', async (req) =>
    listarDocumentos(app.db, {
      tipo: tipoQuery(queryStr(req, 'tipo')),
      anio: queryInt(req, 'anio'),
      trimestre: queryTrimestre(req),
      clienteId: queryInt(req, 'clienteId'),
      q: queryStr(req, 'q'),
    }),
  );

  app.get('/documentos/anios', async () => aniosConDocumentos(app.db));

  app.get('/documentos/cotizacion-usd', async (req) => {
    const fecha = queryStr(req, 'fecha');
    if (fecha && !/^\d{4}-\d{2}-\d{2}$/.test(fecha)) throw peticionIncorrecta('Fecha no válida');
    return cotizacionUsd(fecha);
  });

  app.get('/documentos/proximo-numero', async (req) => {
    const tipo = tipoQuery(queryStr(req, 'tipo')) ?? 'presupuesto';
    const anio = queryInt(req, 'anio') ?? new Date().getFullYear();
    return proximoNumero(app.db, tipo, anio);
  });

  app.get('/documentos/numero-libre', async (req) => {
    const tipo = tipoQuery(queryStr(req, 'tipo')) ?? 'factura';
    const anio = queryInt(req, 'anio') ?? new Date().getFullYear();
    const numero = queryInt(req, 'numero');
    if (!numero || numero < 1) throw peticionIncorrecta('Número no válido');
    return numeroLibre(app.db, tipo, anio, numero, queryInt(req, 'excluir'));
  });

  app.get('/documentos/:id', async (req) => obtenerDocumento(app.db, idParam(req)));

  app.post('/documentos', async (req, reply) => {
    const d = documentoSchema.parse(req.body);
    const id = await crearDocumento(app.db, d, req.usuario!.id);
    reply.status(201);
    return obtenerDocumento(app.db, id);
  });

  app.put('/documentos/:id', async (req) => {
    const id = idParam(req);
    await actualizarDocumento(app.db, id, documentoSchema.parse(req.body));
    return obtenerDocumento(app.db, id);
  });

  app.delete('/documentos/:id', async (req) => {
    await borrarDocumento(app.db, idParam(req));
    return { ok: true };
  });

  app.post('/documentos/:id/convertir-a-factura', async (req, reply) => {
    const { pedidoNombre } = (req.body ?? {}) as { pedidoNombre?: unknown };
    const id = await convertirAFactura(app.db, idParam(req), req.usuario!.id, typeof pedidoNombre === 'string' ? pedidoNombre.slice(0, 1000) : undefined);
    reply.status(201);
    return obtenerDocumento(app.db, id);
  });
}
