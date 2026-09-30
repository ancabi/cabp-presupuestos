import type { FastifyInstance } from 'fastify';
import { distribuidorSchema, productoSchema, type Distribuidor, type Producto } from '@cabp/shared';
import { consultar, ejecutar } from '../db/pool';
import { noEncontrado } from '../errores';
import { idParam } from './util';

const mapDist = (f: any): Distribuidor => ({
  id: f.id,
  nombre: f.nombre,
  direccion: f.direccion,
  email: f.email,
  telefono: f.telefono,
  ciudad: f.ciudad,
  provincia: f.provincia,
  pais: f.pais,
  numeroCta: f.numero_cta,
  iban: f.iban,
  swift: f.swift,
});
const mapProd = (f: any): Producto => ({ id: f.id, distribuidorId: f.distribuidor_id, nombre: f.nombre, precio: f.precio });

const COLS = ['nombre', 'direccion', 'email', 'telefono', 'ciudad', 'provincia', 'pais', 'numero_cta', 'iban', 'swift'];
const valores = (d: ReturnType<typeof distribuidorSchema.parse>) => [
  d.nombre, d.direccion, d.email, d.telefono, d.ciudad, d.provincia, d.pais, d.numeroCta, d.iban, d.swift,
];

export async function rutasDistribuidores(app: FastifyInstance) {
  const obtener = async (id: number) => {
    const [f] = await consultar(app.db, 'SELECT * FROM distribuidores WHERE id = ?', [id]);
    if (!f) throw noEncontrado('Distribuidor');
    return mapDist(f);
  };

  app.get('/distribuidores', async () =>
    (await consultar(app.db, 'SELECT * FROM distribuidores ORDER BY nombre')).map(mapDist),
  );
  app.get('/distribuidores/:id', async (req) => obtener(idParam(req)));

  app.post('/distribuidores', async (req, reply) => {
    const d = distribuidorSchema.parse(req.body);
    const r = await ejecutar(app.db, `INSERT INTO distribuidores (${COLS.join(',')}) VALUES (${COLS.map(() => '?').join(',')})`, valores(d));
    reply.status(201);
    return obtener(r.insertId);
  });

  app.put('/distribuidores/:id', async (req) => {
    const id = idParam(req);
    const d = distribuidorSchema.parse(req.body);
    const r = await ejecutar(app.db, `UPDATE distribuidores SET ${COLS.map((c) => `${c} = ?`).join(', ')} WHERE id = ?`, [...valores(d), id]);
    if (!r.affectedRows) throw noEncontrado('Distribuidor');
    return obtener(id);
  });

  // Los documentos conservan el nombre y precio de cada línea, así que borrar es seguro.
  app.delete('/distribuidores/:id', async (req) => {
    const r = await ejecutar(app.db, 'DELETE FROM distribuidores WHERE id = ?', [idParam(req)]);
    if (!r.affectedRows) throw noEncontrado('Distribuidor');
    return { ok: true };
  });

  app.get('/distribuidores/:id/productos', async (req) =>
    (
      await consultar(app.db, 'SELECT * FROM productos WHERE distribuidor_id = ? AND activo = 1 ORDER BY nombre', [idParam(req)])
    ).map(mapProd),
  );

  app.post('/distribuidores/:id/productos', async (req, reply) => {
    const distId = idParam(req);
    await obtener(distId);
    const d = productoSchema.parse(req.body);
    const r = await ejecutar(app.db, 'INSERT INTO productos (distribuidor_id, nombre, precio) VALUES (?,?,?)', [distId, d.nombre, d.precio]);
    reply.status(201);
    return { id: r.insertId, distribuidorId: distId, ...d };
  });

  app.put('/productos/:id', async (req) => {
    const id = idParam(req);
    const d = productoSchema.parse(req.body);
    const r = await ejecutar(app.db, 'UPDATE productos SET nombre = ?, precio = ? WHERE id = ? AND activo = 1', [d.nombre, d.precio, id]);
    if (!r.affectedRows) throw noEncontrado('Producto');
    const [f] = await consultar(app.db, 'SELECT * FROM productos WHERE id = ?', [id]);
    return mapProd(f);
  });

  // Borrado lógico: las líneas de documentos antiguos siguen apuntando al producto.
  app.delete('/productos/:id', async (req) => {
    const r = await ejecutar(app.db, 'UPDATE productos SET activo = 0 WHERE id = ?', [idParam(req)]);
    if (!r.affectedRows) throw noEncontrado('Producto');
    return { ok: true };
  });
}
