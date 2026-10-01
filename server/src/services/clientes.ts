import type { Cliente, ClienteInput } from '@cabp/shared';
import { consultar, ejecutar, type Db } from '../db/pool';

function mapear(f: any, telefonos: string[], emails: string[]): Cliente {
  return {
    id: f.id,
    dni: f.dni,
    nombre: f.nombre,
    apellidos: f.apellidos,
    direccion: f.direccion,
    codigoPostal: f.codigo_postal,
    ciudad: f.ciudad,
    provincia: f.provincia,
    empresa: f.empresa,
    notas: f.notas,
    telefonos,
    emails,
  };
}

async function contactos(db: Db, ids: number[]) {
  const tel = new Map<number, string[]>();
  const em = new Map<number, string[]>();
  if (ids.length) {
    for (const f of await consultar(db, 'SELECT cliente_id, telefono FROM cliente_telefonos WHERE cliente_id IN (?) ORDER BY id', [ids])) {
      tel.set(f.cliente_id, [...(tel.get(f.cliente_id) ?? []), f.telefono]);
    }
    for (const f of await consultar(db, 'SELECT cliente_id, email FROM cliente_emails WHERE cliente_id IN (?) ORDER BY id', [ids])) {
      em.set(f.cliente_id, [...(em.get(f.cliente_id) ?? []), f.email]);
    }
  }
  return { tel, em };
}

export async function listarClientes(db: Db, q?: string): Promise<Cliente[]> {
  let sql = 'SELECT * FROM clientes';
  const params: unknown[] = [];
  if (q) {
    const like = `%${q.replace(/[\\%_]/g, (c) => '\\' + c)}%`;
    sql += ` WHERE dni LIKE ? OR nombre LIKE ? OR apellidos LIKE ? OR CONCAT(nombre, ' ', apellidos) LIKE ?
             OR codigo_postal LIKE ? OR ciudad LIKE ? OR provincia LIKE ? OR empresa LIKE ?
             OR id IN (SELECT cliente_id FROM cliente_telefonos WHERE telefono LIKE ?)
             OR id IN (SELECT cliente_id FROM cliente_emails WHERE email LIKE ?)`;
    params.push(...Array(10).fill(like));
  }
  sql += ' ORDER BY id DESC';
  const filas = await consultar(db, sql, params);
  const { tel, em } = await contactos(db, filas.map((f) => f.id));
  return filas.map((f) => mapear(f, tel.get(f.id) ?? [], em.get(f.id) ?? []));
}

export async function obtenerCliente(db: Db, id: number): Promise<Cliente | null> {
  const [f] = await consultar(db, 'SELECT * FROM clientes WHERE id = ?', [id]);
  if (!f) return null;
  const { tel, em } = await contactos(db, [id]);
  return mapear(f, tel.get(id) ?? [], em.get(id) ?? []);
}

async function guardarContactos(db: Db, id: number, d: Pick<ClienteInput, 'telefonos' | 'emails'>) {
  await ejecutar(db, 'DELETE FROM cliente_telefonos WHERE cliente_id = ?', [id]);
  await ejecutar(db, 'DELETE FROM cliente_emails WHERE cliente_id = ?', [id]);
  if (d.telefonos.length) {
    await ejecutar(db, 'INSERT IGNORE INTO cliente_telefonos (cliente_id, telefono) VALUES ?', [d.telefonos.map((t) => [id, t])]);
  }
  if (d.emails.length) {
    await ejecutar(db, 'INSERT IGNORE INTO cliente_emails (cliente_id, email) VALUES ?', [d.emails.map((e) => [id, e])]);
  }
}

/** Campo de la API -> columna de la tabla clientes. */
const COLUMNAS: [keyof ClienteInput, string][] = [
  ['dni', 'dni'],
  ['nombre', 'nombre'],
  ['apellidos', 'apellidos'],
  ['direccion', 'direccion'],
  ['codigoPostal', 'codigo_postal'],
  ['ciudad', 'ciudad'],
  ['provincia', 'provincia'],
  ['empresa', 'empresa'],
  ['notas', 'notas'],
];

export async function crearCliente(db: Db, d: ClienteInput): Promise<number> {
  const r = await ejecutar(
    db,
    `INSERT INTO clientes (${COLUMNAS.map(([, c]) => c).join(', ')}) VALUES (${COLUMNAS.map(() => '?').join(', ')})`,
    COLUMNAS.map(([k]) => d[k]),
  );
  await guardarContactos(db, r.insertId, d);
  return r.insertId;
}

export async function actualizarCliente(db: Db, id: number, d: ClienteInput): Promise<boolean> {
  const r = await ejecutar(
    db,
    `UPDATE clientes SET ${COLUMNAS.map(([, c]) => `${c} = ?`).join(', ')} WHERE id = ?`,
    [...COLUMNAS.map(([k]) => d[k]), id],
  );
  if (!r.affectedRows) return false;
  await guardarContactos(db, id, d);
  return true;
}
