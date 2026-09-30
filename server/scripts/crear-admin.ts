/**
 * Crea (o reactiva y cambia la contraseña de) un usuario administrador.
 *   npm run crear-admin -- email@dominio.com "Nombre"
 * La contraseña se pide por consola, o se toma de ADMIN_PASSWORD.
 */
import readline from 'node:readline/promises';
import { usuarioCrearSchema } from '@cabp/shared';
import { crearPool, consultar, ejecutar } from '../src/db/pool';
import { migrar } from '../src/db/migrar';
import { hashPassword } from '../src/auth/sesiones';

const [email, nombre = 'Administrador'] = process.argv.slice(2);
if (!email) {
  console.error('Uso: npm run crear-admin -- email "Nombre"');
  process.exit(1);
}

let password = process.env.ADMIN_PASSWORD;
if (!password) {
  const rl = readline.createInterface({ input: process.stdin, output: process.stdout });
  password = await rl.question('Contraseña (mínimo 8 caracteres): ');
  rl.close();
}

const d = usuarioCrearSchema.parse({ email, nombre, password, rol: 'admin' });
const pool = crearPool(process.env.DATABASE_URL!);
await migrar(pool);
const [existe] = await consultar(pool, 'SELECT id FROM usuarios WHERE email = ?', [d.email]);
const h = await hashPassword(d.password);
if (existe) {
  await ejecutar(pool, "UPDATE usuarios SET password_hash = ?, rol = 'admin', activo = 1 WHERE id = ?", [h, existe.id]);
  console.log(`Usuario ${d.email} actualizado como administrador.`);
} else {
  await ejecutar(pool, "INSERT INTO usuarios (email, nombre, password_hash, rol) VALUES (?, ?, ?, 'admin')", [d.email, d.nombre, h]);
  console.log(`Administrador ${d.email} creado.`);
}
await pool.end();
