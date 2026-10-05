import fs from 'node:fs';
import path from 'node:path';
import { afterAll, beforeAll, describe, expect, it } from 'vitest';
import type { FastifyInstance } from 'fastify';
import type { Pool } from 'mysql2/promise';
import { cliente, prepararApp } from './helpers';

let app: FastifyInstance;
let pool: Pool;
let api: ReturnType<typeof cliente>;
let cookie: string;
let uploads: string;

beforeAll(async () => {
  const p = await prepararApp();
  app = p.app;
  pool = p.pool;
  cookie = p.cookie;
  uploads = p.uploads;
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
    const f = await api.post('/api/documentos', docBase(clienteId, '2026-06-01', { tipo: 'factura', pedidoNombre: 'Pedido de prueba' }));
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
      docBase(clienteId, '2026-11-01', { tipo: 'factura', pedidoNombre: 'Pedido de prueba', lineas: [], totalManualActivo: true, totalManual: -500 }),
    );
    expect(r.status).toBe(201);
    expect(r.body.totalManual).toBe(-500);
    expect(r.body.totalConIva).toBe(-605);
    const conLinea = await api.post(
      '/api/documentos',
      docBase(clienteId, '2026-11-02', {
        tipo: 'factura',
        pedidoNombre: 'Devolución',
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

  it('guarda las dos columnas de importación y las copia al convertir a factura', async () => {
    const col1 = 'Pago Anticipado: 100 %.\n\nNCM (posición arancelaria): 8428.90.90.900Z.';
    const col2 = 'Advance Payment: 100 %.\n\nNCM (tariff item): 8428.90.90.900Z.';
    const p = await api.post(
      '/api/documentos',
      docBase(clienteId, '2026-12-01', { textoImportacion1: col1, textoImportacion2: col2 }),
    );
    expect(p.status).toBe(201);
    expect(p.body.textoImportacion1).toBe(col1);
    expect(p.body.textoImportacion2).toBe(col2);
    const f = await api.post(`/api/documentos/${p.body.id}/convertir-a-factura`, { pedidoNombre: 'Pedido convertido' });
    expect(f.body.textoImportacion1).toBe(col1);
    expect(f.body.textoImportacion2).toBe(col2);
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
    const f = await api.post(`/api/documentos/${p.id}/convertir-a-factura`, { pedidoNombre: 'Pedido convertido' });
    expect(f.status).toBe(201);
    expect(f.body.tipo).toBe('factura');
    expect(f.body.presupuestoOrigenId).toBe(p.id);
    expect(f.body.lineas).toHaveLength(1);
    expect(f.body.totalConIva).toBe(p.totalConIva);
    const otra = await api.post(`/api/documentos/${p.id}/convertir-a-factura`, { pedidoNombre: 'Pedido convertido' });
    expect(otra.status).toBe(409);
    const pres = await api.get(`/api/documentos/${p.id}`);
    expect(pres.body.facturaId).toBe(f.body.id);
  });

  it('solo deja borrar la última factura del año', async () => {
    const f1 = (await api.post('/api/documentos', docBase(clienteId, '2031-01-01', { tipo: 'factura', pedidoNombre: 'Pedido de prueba' }))).body;
    const f2 = (await api.post('/api/documentos', docBase(clienteId, '2031-01-02', { tipo: 'factura', pedidoNombre: 'Pedido de prueba' }))).body;
    expect((await api.del(`/api/documentos/${f1.id}`)).status).toBe(409);
    expect((await api.del(`/api/documentos/${f2.id}`)).status).toBe(200);
    const f3 = (await api.post('/api/documentos', docBase(clienteId, '2031-01-03', { tipo: 'factura', pedidoNombre: 'Pedido de prueba' }))).body;
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

describe('ajustes: aspecto del PDF', () => {
  it('usa los valores por defecto si los ajustes guardados no tienen "pdf"', async () => {
    const actuales = (await api.get('/api/ajustes')).body;
    const { pdf: _pdf, ...sinPdf } = actuales;
    await pool.query("REPLACE INTO ajustes (clave, valor) VALUES ('general', ?)", [JSON.stringify(sinPdf)]);
    const r = await api.get('/api/ajustes');
    expect(r.body.pdf).toEqual({ fuente: 'helvetica', tamanoLetra: 8.5, colorPrincipal: '#0c8599', colorTexto: '#222222', tamanoLogo: 44 });
  });

  it('guarda fuente, tamaño, colores y logo, y valida los valores', async () => {
    const actuales = (await api.get('/api/ajustes')).body;
    const pdf = { fuente: 'merriweather', tamanoLetra: 10, colorPrincipal: '#8b1e3f', colorTexto: '#111111', tamanoLogo: 80 };
    expect((await api.put('/api/ajustes', { ...actuales, pdf })).status).toBe(200);
    expect((await api.get('/api/ajustes')).body.pdf).toEqual(pdf);
    for (const malo of [{ colorPrincipal: 'rojo' }, { tamanoLetra: 30 }, { tamanoLogo: 5 }, { fuente: 'comic-sans' }]) {
      expect((await api.put('/api/ajustes', { ...actuales, pdf: { ...pdf, ...malo } })).status).toBe(400);
    }
  });
});

describe('pedidos', () => {
  let clienteId: number;
  let otroClienteId: number;
  const subir = (pedidoId: number, tipoId: number | string, nombre: string, tipo: string, datos: string) => {
    const limite = '----cabp';
    const cuerpo =
      `--${limite}\r\nContent-Disposition: form-data; name="f"; filename="${nombre}"\r\nContent-Type: ${tipo}\r\n\r\n` +
      datos +
      `\r\n--${limite}--\r\n`;
    return app.inject({
      method: 'POST',
      url: `/api/pedidos/${pedidoId}/ficheros?tipoId=${tipoId}`,
      payload: cuerpo,
      headers: { 'content-type': `multipart/form-data; boundary=${limite}`, 'x-requested-with': 'cabp', cookie },
    });
  };
  const esperar = () => new Promise((r) => setTimeout(r, 1100));

  beforeAll(async () => {
    clienteId = (await api.post('/api/clientes', { nombre: 'Rosa', apellidos: 'Pedidera' })).body.id;
    otroClienteId = (await api.post('/api/clientes', { nombre: 'Tomás', apellidos: 'Otro' })).body.id;
  });

  it('exige nombre de pedido al crear una factura y lo crea con su cliente y año', async () => {
    const sin = await api.post('/api/documentos', docBase(clienteId, '2040-02-01', { tipo: 'factura' }));
    expect(sin.status).toBe(400);
    expect(sin.body.error).toMatch(/pedido/);

    const nombre = 'Silla salvaescaleras curva para la escalera de la casa de la playa, con dos paradas intermedias y mando a distancia';
    const f = await api.post('/api/documentos', docBase(clienteId, '2040-02-01', { tipo: 'factura', pedidoNombre: `  ${nombre}  ` }));
    expect(f.status).toBe(201);
    expect(f.body.pedido.nombre).toBe(nombre);

    const p = await api.get(`/api/pedidos/${f.body.pedido.id}`);
    expect(p.body).toMatchObject({ nombre, anio: 2040, clienteId, clienteNombre: 'Rosa Pedidera', facturaId: f.body.id });
    expect(p.body.factura.codigo).toBe(f.body.codigo);
    expect(p.body.ficheros).toEqual([]);
  });

  it('los presupuestos no crean pedido; al convertirlos en factura sí', async () => {
    const pr = await api.post('/api/documentos', docBase(clienteId, '2040-03-01', { pedidoNombre: 'ignorado' }));
    expect(pr.body.pedido).toBeNull();
    expect((await api.get('/api/pedidos?q=ignorado')).body).toHaveLength(0);
    expect((await api.post(`/api/documentos/${pr.body.id}/convertir-a-factura`, {})).status).toBe(400);
    const f = await api.post(`/api/documentos/${pr.body.id}/convertir-a-factura`, { pedidoNombre: 'Plataforma vertical' });
    expect(f.status).toBe(201);
    expect(f.body.pedido.nombre).toBe('Plataforma vertical');
  });

  it('crea el pedido de una factura antigua una sola vez', async () => {
    const [{ insertId }] = (await pool.query(
      `INSERT INTO documentos (tipo, anio, numero, fecha, cliente_id, iva_porcentaje, texto_concepto, texto_forma_pago, texto_explicativo)
       VALUES ('factura', 2039, 99, '2039-05-05', ?, 21, '', '', '')`,
      [clienteId],
    )) as any;
    expect((await api.get(`/api/documentos/${insertId}`)).body.pedido).toBeNull();
    const r = await api.post(`/api/documentos/${insertId}/pedido`, { nombre: 'Pedido antiguo' });
    expect(r.status).toBe(201);
    expect(r.body.pedido.nombre).toBe('Pedido antiguo');
    expect((await api.get(`/api/pedidos/${r.body.pedido.id}`)).body.anio).toBe(2039);
    expect((await api.post(`/api/documentos/${insertId}/pedido`, { nombre: 'Otro' })).status).toBe(409);
    const pr = (await api.post('/api/documentos', docBase(clienteId, '2039-06-01'))).body;
    expect((await api.post(`/api/documentos/${pr.id}/pedido`, { nombre: 'X' })).status).toBe(400);
  });

  it('lista por año y filtra por nombre del pedido o del cliente', async () => {
    await api.post('/api/documentos', docBase(otroClienteId, '2040-04-01', { tipo: 'factura', pedidoNombre: 'Elevador de piscina' }));
    const anio = (await api.get('/api/pedidos?anio=2040')).body;
    expect(anio.map((p: any) => p.nombre)).toHaveLength(2);
    expect(anio.every((p: any) => p.anio === 2040)).toBe(true);
    expect((await api.get('/api/pedidos?anio=2040&q=piscina')).body.map((p: any) => p.nombre)).toEqual(['Elevador de piscina']);
    expect((await api.get('/api/pedidos?anio=2040&q=tomás otro')).body.map((p: any) => p.nombre)).toEqual(['Elevador de piscina']);
    expect((await api.get('/api/pedidos?anio=2040&q=pedidera')).body.map((p: any) => p.nombre)).toEqual([
      expect.stringMatching(/^Silla salvaescaleras/),
    ]);
    // Convertir un presupuesto crea la factura (y su pedido) en el año actual.
    expect((await api.get(`/api/pedidos?anio=${new Date().getFullYear()}&q=plataforma`)).body).toHaveLength(1);
    expect((await api.get('/api/pedidos?q=100%')).body).toHaveLength(0);
  });

  it('gestiona tipos de fichero: crear y renombrar, sin borrar', async () => {
    const tipos = (await api.get('/api/tipos-fichero')).body;
    expect(tipos[0]).toMatchObject({ nombre: 'Factura cliente', sistema: true });
    const tras = await api.post('/api/tipos-fichero', { nombre: 'Documento aduana' });
    expect(tras.status).toBe(201);
    const nuevo = tras.body.find((t: any) => t.nombre === 'Documento aduana');
    expect((await api.post('/api/tipos-fichero', { nombre: 'Documento aduana' })).status).toBe(409);
    expect((await api.post('/api/tipos-fichero', { nombre: '  ' })).status).toBe(400);
    const ren = await api.put(`/api/tipos-fichero/${nuevo.id}`, { nombre: 'DUA aduana' });
    expect(ren.body.map((t: any) => t.nombre)).toContain('DUA aduana');
    expect((await api.put(`/api/tipos-fichero/${nuevo.id}`, { nombre: 'Factura cliente' })).status).toBe(409);
    expect((await api.del(`/api/tipos-fichero/${nuevo.id}`)).status).toBe(404);

    await api.post('/api/usuarios', { email: 'normal@test.es', nombre: 'Normal', password: 'secreto123' });
    const login = await app.inject({
      method: 'POST',
      url: '/api/auth/login',
      headers: { 'x-requested-with': 'cabp' },
      payload: { email: 'normal@test.es', password: 'secreto123' },
    });
    const c = login.cookies[0]!;
    const normal = cliente(app, `${c.name}=${c.value}`);
    expect((await normal.get('/api/tipos-fichero')).status).toBe(200);
    expect((await normal.post('/api/tipos-fichero', { nombre: 'Intruso' })).status).toBe(403);
    expect((await normal.put(`/api/tipos-fichero/${nuevo.id}`, { nombre: 'Intruso' })).status).toBe(403);
  });

  it('sube, cambia de tipo, descarga y borra ficheros del pedido', async () => {
    const f = (await api.post('/api/documentos', docBase(clienteId, '2040-05-01', { tipo: 'factura', pedidoNombre: 'Con ficheros' }))).body;
    const pedidoId = f.pedido.id;
    const tipos = (await api.get('/api/tipos-fichero')).body;
    const factura = tipos.find((t: any) => t.sistema);
    const dua = tipos.find((t: any) => t.nombre === 'DUA aduana');
    const antes = (await api.get(`/api/pedidos/${pedidoId}`)).body.actualizadoEn;

    expect((await subir(pedidoId, 9999, 'a.pdf', 'application/pdf', '%PDF')).statusCode).toBe(400);
    expect((await subir(pedidoId, '', 'a.pdf', 'application/pdf', '%PDF')).statusCode).toBe(400);
    expect((await subir(pedidoId, dua.id, 'a.txt', 'text/plain', 'hola')).statusCode).toBe(400);

    await esperar();
    const r = await subir(pedidoId, factura.id, 'factura firmada.pdf', 'application/pdf', '%PDF-firmada');
    expect(r.statusCode).toBe(201);
    const [fi] = JSON.parse(r.body);
    expect(fi).toMatchObject({ tipoId: factura.id, tipoNombre: 'Factura cliente', nombreOriginal: 'factura firmada.pdf' });

    const p = (await api.get(`/api/pedidos/${pedidoId}`)).body;
    expect(p.ficheros).toHaveLength(1);
    expect(p.numFicheros).toBe(1);
    expect(p.actualizadoEn > antes).toBe(true);
    expect(p.creadoEn).toBe(antes);

    const cambio = await app.inject({
      method: 'PATCH',
      url: `/api/pedido-ficheros/${fi.id}`,
      payload: { tipoId: dua.id },
      headers: { cookie, 'x-requested-with': 'cabp' },
    });
    expect(cambio.statusCode).toBe(200);
    expect((await api.get(`/api/pedidos/${pedidoId}`)).body.ficheros[0].tipoNombre).toBe('DUA aduana');

    const d = await app.inject({ method: 'GET', url: `/api/pedido-ficheros/${fi.id}/fichero?descargar=1`, headers: { cookie } });
    expect(d.body).toBe('%PDF-firmada');
    expect(d.headers['content-disposition']).toMatch(/^attachment/);

    // Un tipo en uso tampoco se puede borrar a mano en la base de datos.
    await expect(pool.query('DELETE FROM tipos_fichero WHERE id = ?', [dua.id])).rejects.toThrow();

    const ruta = path.join(uploads, 'pedidos', String(pedidoId));
    expect(fs.readdirSync(ruta)).toHaveLength(1);
    expect((await api.del(`/api/pedido-ficheros/${fi.id}`)).status).toBe(200);
    expect(fs.readdirSync(ruta)).toHaveLength(0);
    expect((await api.get(`/api/pedidos/${pedidoId}`)).body.ficheros).toHaveLength(0);
  });

  it('renombrar el pedido o editar su factura lo marca como modificado; el cliente se sincroniza', async () => {
    const f = (await api.post('/api/documentos', docBase(clienteId, '2040-06-01', { tipo: 'factura', pedidoNombre: 'Original' }))).body;
    const pedidoId = f.pedido.id;
    expect((await api.put(`/api/pedidos/${pedidoId}`, { nombre: '' })).status).toBe(400);
    const p0 = (await api.get(`/api/pedidos/${pedidoId}`)).body;
    await esperar();
    const ren = await api.put(`/api/pedidos/${pedidoId}`, { nombre: 'Renombrado' });
    expect(ren.body.nombre).toBe('Renombrado');
    expect(ren.body.actualizadoEn > p0.actualizadoEn).toBe(true);

    await esperar();
    await api.put(`/api/documentos/${f.id}`, docBase(otroClienteId, '2040-06-02', { tipo: 'factura' }));
    const p2 = (await api.get(`/api/pedidos/${pedidoId}`)).body;
    expect(p2.clienteId).toBe(otroClienteId);
    expect(p2.actualizadoEn > ren.body.actualizadoEn).toBe(true);
    expect(p2.nombre).toBe('Renombrado');
  });
});
