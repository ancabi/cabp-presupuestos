/**
 * Cambia la contraseña de un usuario que ya existe (no toca su rol ni si está activo).
 *   npm run cambiar-password -- email@dominio.com
 * La contraseña se pide por consola (sin mostrarla, dos veces), o se toma de NUEVA_PASSWORD.
 * Cierra todas las sesiones abiertas del usuario.
 */
import readline from 'node:readline';
import { usuarioCrearSchema } from '@cabp/shared';
import { crearPool, consultar, ejecutar } from '../src/db/pool';
import { migrar } from '../src/db/migrar';
import { hashPassword } from '../src/auth/sesiones';

function salir(mensaje: string): never {
  console.error(mensaje);
  process.exit(1);
}

/** Pregunta por consola sin mostrar lo que se escribe. */
function preguntarOculto(pregunta: string): Promise<string> {
  return new Promise((resolve) => {
    const rl = readline.createInterface({ input: process.stdin, output: process.stdout, terminal: true });
    let mostrando = true;
    const escribir = (rl as unknown as { _writeToOutput: (s: string) => void })._writeToOutput.bind(rl);
    (rl as unknown as { _writeToOutput: (s: string) => void })._writeToOutput = (s: string) => {
      if (mostrando) escribir(s);
    };
    rl.question(pregunta, (respuesta) => {
      rl.close();
      process.stdout.write('\n');
      resolve(respuesta);
    });
    mostrando = false;
  });
}

const [emailArg] = process.argv.slice(2);
if (!emailArg) salir('Uso: npm run cambiar-password -- email@dominio.com');
const email = usuarioCrearSchema.shape.email.safeParse(emailArg);
if (!email.success) salir(`Email no válido: ${emailArg}`);

const pool = crearPool(process.env.DATABASE_URL!);
try {
  await migrar(pool);
  const [usuario] = await consultar(pool, 'SELECT id, nombre, activo FROM usuarios WHERE email = ?', [email.data]);
  if (!usuario) salir(`No existe ningún usuario con el email ${email.data}.`);

  let password = process.env.NUEVA_PASSWORD;
  if (!password) {
    if (!process.stdin.isTTY) salir('Sin terminal interactiva: indica la contraseña en NUEVA_PASSWORD.');
    password = await preguntarOculto(`Nueva contraseña para ${email.data} (mínimo 8 caracteres): `);
    const repetida = await preguntarOculto('Repite la contraseña: ');
    if (password !== repetida) salir('Las contraseñas no coinciden. No se ha cambiado nada.');
  }
  const valida = usuarioCrearSchema.shape.password.safeParse(password);
  if (!valida.success) salir(valida.error.issues[0]?.message ?? 'Contraseña no válida');

  await ejecutar(pool, 'UPDATE usuarios SET password_hash = ? WHERE id = ?', [await hashPassword(valida.data), usuario.id]);
  await ejecutar(pool, 'DELETE FROM sesiones WHERE usuario_id = ?', [usuario.id]);
  console.log(`Contraseña de ${usuario.nombre} (${email.data}) cambiada. Se han cerrado sus sesiones abiertas.`);
  if (!usuario.activo) console.log('Aviso: este usuario está desactivado; no podrá entrar hasta que un administrador lo active.');
} finally {
  await pool.end();
}
