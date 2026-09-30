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
  obtenerDocumento,
  proximoNumero,
} from '../services/documentos';
import { idParam, queryInt, queryStr } from './util';

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
      clienteId: queryInt(req, 'clienteId'),
      q: queryStr(req, 'q'),
    }),
  );

  app.get('/documentos/anios', async () => aniosConDocumentos(app.db));

  app.get('/documentos/proximo-numero', async (req) => {
    const tipo = tipoQuery(queryStr(req, 'tipo')) ?? 'presupuesto';
    const anio = queryInt(req, 'anio') ?? new Date().getFullYear();
    return { codigo: await proximoNumero(app.db, tipo, anio) };
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
    const id = await convertirAFactura(app.db, idParam(req), req.usuario!.id);
    reply.status(201);
    return obtenerDocumento(app.db, id);
  });
}
