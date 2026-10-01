import { afterAll, beforeAll, describe, expect, it } from 'vitest';
import type { FastifyInstance } from 'fastify';
import type { Pool } from 'mysql2/promise';
import { cliente, prepararApp } from './helpers';

let app: FastifyInstance;
let pool: Pool;
let api: ReturnType<typeof cliente>;
let cookie: string;

beforeAll(async () => {
  const p = await prepararApp();
  app = p.app;
  pool = p.pool;
  cookie = p.cookie;
  api = cliente(app, p.cookie);
});
afterAll(async () => {
  await app?.close();
  await pool?.end();
});

const docBase = (clienteId: number, fecha: string, extra: Record<string, unknown> = {}) => ({
  tipo: 'presupuesto',
  fecha,
  clienteId,
  ivaPorcentaje: 21,
  lineas: [{ productoId: null, nombreProducto: 'Silla salvaescaleras', cantidad: 1, precio: 1000 }],
  ...extra,
});

describe('autenticación', () => {
  it('rechaza peticiones sin sesión', async () => {
    const r = await app.inject({ method: 'GET', url: '/api/clientes' });
    expect(r.statusCode).toBe(401);
  });

  it('rechaza contraseña incorrecta', async () => {
    const r = await app.inject({
      method: 'POST',
      url: '/api/auth/login',
      headers: { 'x-requested-with': 'cabp' },
      payload: { email: 'admin@test.es', password: 'mala' },
    });
    expect(r.statusCode).toBe(401);
  });

  it('exige la cabecera anti-CSRF en peticiones de escritura', async () => {
    const r = await app.inject({ method: 'POST', url: '/api/clientes', payload: { nombre: 'x' } });
    expect([401, 403]).toContain(r.statusCode);
  });

  it('devuelve el usuario actual', async () => {
    const r = await api.get('/api/auth/me');
    expect(r.body.email).toBe('admin@test.es');
  });
});

describe('clientes', () => {
  it('crea, busca, edita y guarda teléfonos y emails', async () => {
    const c = await api.post('/api/clientes', {
      nombre: 'Ana',
      apellidos: 'García López',
      dni: '12345678Z',
      telefonos: ['600111222', '600111222', '952000000'],
      emails: ['Ana@Example.com'],
    });
    expect(c.status).toBe(201);
    expect(c.body.telefonos).toEqual(['600111222', '952000000']);
    expect(c.body.emails).toEqual(['ana@example.com']);

    const b = await api.get('/api/clientes?q=garc');
    expect(b.body.map((x: any) => x.id)).toContain(c.body.id);

    const e = await api.put(`/api/clientes/${c.body.id}`, { ...c.body, ciudad: 'Málaga', telefonos: ['611000000'] });
    expect(e.body.ciudad).toBe('Málaga');
    expect(e.body.telefonos).toEqual(['611000000']);
  });

  it('guarda y busca por código postal', async () => {
    const c = await api.post('/api/clientes', { nombre: 'Postal', ciudad: 'Benalmádena', codigoPostal: ' 29631 ' });
    expect(c.status).toBe(201);
    expect(c.body.codigoPostal).toBe('29631');
    const b = await api.get('/api/clientes?q=29631');
    expect(b.body.map((x: any) => x.id)).toEqual([c.body.id]);
    const e = await api.put(`/api/clientes/${c.body.id}`, { ...c.body, codigoPostal: '29630' });
    expect(e.body.codigoPostal).toBe('29630');
    expect((await api.post('/api/clientes', { nombre: 'Malo', codigoPostal: '29<6' })).status).toBe(400);
  });

  it('valida el nombre obligatorio', async () => {
    const r = await api.post('/api/clientes', { nombre: '' });
    expect(r.status).toBe(400);
  });
});

describe('documentos y numeración por año', () => {
  let clienteId: number;
  beforeAll(async () => {
    clienteId = (await api.post('/api/clientes', { nombre: 'Numeración' })).body.id;
  });

  it('numera correlativamente por año y reinicia en cada año', async () => {
    const a = await api.post('/api/documentos', docBase(clienteId, '2026-03-01'));
    const b = await api.post('/api/documentos', docBase(clienteId, '2026-05-01'));
    const c = await api.post('/api/documentos', docBase(clienteId, '2027-01-02'));
    expect(a.body.codigo).toBe('P-2026/0001');
    expect(b.body.codigo).toBe('P-2026/0002');
    expect(c.body.codigo).toBe('P-2027/0001');
    const f = await api.post('/api/documentos', docBase(clienteId, '2026-06-01', { tipo: 'factura' }));
    expect(f.body.codigo).toBe('F-2026/0001');
  });

  it('no repite números con peticiones simultáneas', async () => {
    const rs = await Promise.all(
      Array.from({ length: 10 }, () => api.post('/api/documentos', docBase(clienteId, '2030-02-02'))),
    );
    const numeros = rs.map((r) => r.body.numero).sort((x, y) => x - y);
    expect(numeros).toEqual([1, 2, 3, 4, 5, 6, 7, 8, 9, 10]);
  });

  it('calcula los totales en el servidor', async () => {
    const r = await api.post(
      '/api/documentos',
      docBase(clienteId, '2026-07-01', { ganancia: 100, hotel: 50, totalSinIva: 1, totalConIva: 1 }),
    );
    // 1000 + 50 + 100 + 20 (20% de ganancia) = 1170; IVA 21% = 245.70
    expect(r.body.totalSinIva).toBe(1170);
    expect(r.body.totalIva).toBe(245.7);
    expect(r.body.totalConIva).toBe(1415.7);
    expect(r.body.lineas).toHaveLength(1);
  });

  it('admite facturas de devolución con importes negativos', async () => {
    const r = await api.post(
      '/api/documentos',
      docBase(clienteId, '2026-11-01', { tipo: 'factura', lineas: [], totalManualActivo: true, totalManual: -500 }),
    );
    expect(r.status).toBe(201);
    expect(r.body.totalManual).toBe(-500);
    expect(r.body.totalConIva).toBe(-605);
    const conLinea = await api.post(
      '/api/documentos',
      docBase(clienteId, '2026-11-02', {
        tipo: 'factura',
        lineas: [{ productoId: null, nombreProducto: 'Devolución', cantidad: -1, precio: 1000 }],
      }),
    );
    expect(conLinea.status).toBe(201);
    expect(conLinea.body.totalConIva).toBe(-1210);
  });

  it('sigue rechazando negativos donde no tienen sentido', async () => {
    const r = await api.post('/api/documentos', docBase(clienteId, '2026-11-03', { kilometros: -1 }));
    expect(r.status).toBe(400);
  });

  it('no permite mover un documento a otro año al editar', async () => {
    const d = (await api.post('/api/documentos', docBase(clienteId, '2026-08-01'))).body;
    const r = await api.put(`/api/documentos/${d.id}`, docBase(clienteId, '2025-08-01'));
    expect(r.status).toBe(400);
    const ok = await api.put(`/api/documentos/${d.id}`, docBase(clienteId, '2026-09-15', { textoConcepto: 'x' }));
    expect(ok.status).toBe(200);
    expect(ok.body.fecha).toBe('2026-09-15');
    expect(ok.body.numero).toBe(d.numero);
  });

  it('convierte un presupuesto en factura una sola vez', async () => {
    const p = (await api.post('/api/documentos', docBase(clienteId, '2026-10-01'))).body;
    const f = await api.post(`/api/documentos/${p.id}/convertir-a-factura`);
    expect(f.status).toBe(201);
    expect(f.body.tipo).toBe('factura');
    expect(f.body.presupuestoOrigenId).toBe(p.id);
    expect(f.body.lineas).toHaveLength(1);
    expect(f.body.totalConIva).toBe(p.totalConIva);
    const otra = await api.post(`/api/documentos/${p.id}/convertir-a-factura`);
    expect(otra.status).toBe(409);
    const pres = await api.get(`/api/documentos/${p.id}`);
    expect(pres.body.facturaId).toBe(f.body.id);
  });

  it('solo deja borrar la última factura del año', async () => {
    const f1 = (await api.post('/api/documentos', docBase(clienteId, '2031-01-01', { tipo: 'factura' }))).body;
    const f2 = (await api.post('/api/documentos', docBase(clienteId, '2031-01-02', { tipo: 'factura' }))).body;
    expect((await api.del(`/api/documentos/${f1.id}`)).status).toBe(409);
    expect((await api.del(`/api/documentos/${f2.id}`)).status).toBe(200);
    const f3 = (await api.post('/api/documentos', docBase(clienteId, '2031-01-03', { tipo: 'factura' }))).body;
    expect(f3.numero).toBe(2);
  });

  it('filtra el listado por año y tipo', async () => {
    const r = await api.get('/api/documentos?anio=2027&tipo=presupuesto');
    expect(r.body).toHaveLength(1);
    const anios = await api.get('/api/documentos/anios');
    expect(anios.body).toEqual(expect.arrayContaining([2031, 2030, 2027, 2026]));
  });

  it('no deja borrar un cliente con documentos', async () => {
    expect((await api.del(`/api/clientes/${clienteId}`)).status).toBe(409);
  });
});

describe('distribuidores y productos', () => {
  it('CRUD completo y borrado lógico de productos', async () => {
    const d = await api.post('/api/distribuidores', { nombre: 'Stepper', direccion: 'C/ Uno' });
    expect(d.status).toBe(201);
    const e = await api.put(`/api/distribuidores/${d.body.id}`, { ...d.body, direccion: 'C/ Dos', ciudad: 'Madrid' });
    expect(e.body.direccion).toBe('C/ Dos');
    expect(e.body.ciudad).toBe('Madrid');
    const p = await api.post(`/api/distribuidores/${d.body.id}/productos`, { nombre: 'Guía', precio: 12.5 });
    expect(p.body.precio).toBe(12.5);
    await api.del(`/api/productos/${p.body.id}`);
    expect((await api.get(`/api/distribuidores/${d.body.id}/productos`)).body).toHaveLength(0);
  });
});

describe('adjuntos', () => {
  const subir = (clienteId: number, nombre: string, tipo: string, datos: string) => {
    const limite = '----cabp';
    const cuerpo =
      `--${limite}\r\nContent-Disposition: form-data; name="f"; filename="${nombre}"\r\nContent-Type: ${tipo}\r\n\r\n` +
      datos +
      `\r\n--${limite}--\r\n`;
    return app.inject({
      method: 'POST',
      url: `/api/clientes/${clienteId}/adjuntos`,
      payload: cuerpo,
      headers: { 'content-type': `multipart/form-data; boundary=${limite}`, 'x-requested-with': 'cabp', cookie },
    });
  };

  it('sube, descarga y borra un fichero', async () => {
    const c = (await api.post('/api/clientes', { nombre: 'Con adjuntos' })).body;
    const r = await subir(c.id, 'foto.png', 'image/png', 'PNGDATA');
    expect(r.statusCode).toBe(201);
    const [adj] = JSON.parse(r.body);
    expect(adj.tipo).toBe('imagen');
    expect(adj.nombreOriginal).toBe('foto.png');

    const lista = await api.get(`/api/clientes/${c.id}/adjuntos`);
    expect(lista.body).toHaveLength(1);

    const f = await app.inject({ method: 'GET', url: `/api/adjuntos/${adj.id}/fichero`, headers: { cookie } });
    expect(f.statusCode).toBe(200);
    expect(f.body).toBe('PNGDATA');
    expect(f.headers['content-type']).toBe('image/png');

    const sinSesion = await app.inject({ method: 'GET', url: `/api/adjuntos/${adj.id}/fichero` });
    expect(sinSesion.statusCode).toBe(401);

    expect((await api.del(`/api/adjuntos/${adj.id}`)).status).toBe(200);
    expect((await api.get(`/api/clientes/${c.id}/adjuntos`)).body).toHaveLength(0);
  });

  it('rechaza tipos no permitidos', async () => {
    const c = (await api.post('/api/clientes', { nombre: 'Malicioso' })).body;
    const r = await subir(c.id, 'x.html', 'text/html', '<script>alert(1)</script>');
    expect(r.statusCode).toBe(400);
  });
});

describe('publicación detrás de un proxy', () => {
  it('no fuerza https en la CSP y marca la cookie Secure solo con HTTPS', async () => {
    const { crearApp } = await import('../src/app');
    const prod = await crearApp(
      { databaseUrl: '', sessionSecret: 'x'.repeat(40), uploadsDir: '/tmp', port: 0, produccion: true, webDist: null },
      pool,
    );
    const login = (proto?: string) =>
      prod.inject({
        method: 'POST',
        url: '/api/auth/login',
        headers: { 'x-requested-with': 'cabp', ...(proto ? { 'x-forwarded-proto': proto } : {}) },
        payload: { email: 'admin@test.es', password: 'secreto123' },
      });
    const http = await login();
    expect(http.statusCode).toBe(200);
    expect(http.headers['content-security-policy']).not.toContain('upgrade-insecure-requests');
    expect(String(http.headers['set-cookie'])).not.toMatch(/Secure/i);
    const https = await login('https');
    expect(String(https.headers['set-cookie'])).toMatch(/Secure/i);
    await prod.close();
  });
});
