/**
 * Borra los presupuestos y facturas de prueba de un año y, opcionalmente, fija el primer número de factura.
 *   npm run limpiar-pruebas -- --anio 2026 --primera-factura 522              (solo muestra qué haría)
 *   npm run limpiar-pruebas -- --anio 2026 --primera-factura 522 --confirmar  (lo hace)
 * Solo toca documentos hechos en esta aplicación; los importados de la versión anterior se quedan.
 * Con cada factura borrada se borra también su pedido y los ficheros subidos a él.
 */
import fs from 'node:fs';
import path from 'node:path';
import { formatoEuros, formatoFecha } from '@cabp/shared';
import { crearPool, consultar } from '../src/db/pool';
import { migrar } from '../src/db/migrar';
import { rutaUploads } from '../src/config';
import { borrarDocumentosDePrueba, documentosDePrueba } from '../src/services/limpieza';

function salir(mensaje: string): never {
  console.error(mensaje);
  process.exit(1);
}

const args = process.argv.slice(2);
const valor = (nombre: string) => {
  const i = args.indexOf(nombre);
  return i >= 0 ? args[i + 1] : undefined;
};
const anio = Number(valor('--anio') ?? new Date().getFullYear());
const primeraTexto = valor('--primera-factura');
const primera = primeraTexto === undefined ? undefined : Number(primeraTexto);
const confirmar = args.includes('--confirmar');
if (!Number.isInteger(anio) || anio < 2000 || anio > 2100) salir('Año no válido. Uso: --anio 2026');
if (primera !== undefined && (!Number.isInteger(primera) || primera < 1)) salir('--primera-factura debe ser un número entero positivo');

const pool = crearPool(process.env.DATABASE_URL ?? salir('Falta DATABASE_URL'));
await migrar(pool);

const docs = await documentosDePrueba(pool, anio);
console.log(`\nDocumentos de ${anio} hechos en la aplicación (${docs.length}):`);
for (const d of docs) {
  const pedido = d.pedidoId ? `  · pedido «${d.pedidoNombre}»${d.numFicheros ? ` con ${d.numFicheros} fichero(s)` : ''}` : '';
  console.log(`  ${d.codigo}  ${formatoFecha(d.fecha)}  ${d.cliente.padEnd(30)} ${formatoEuros(d.totalConIva).padStart(14)}${pedido}`);
}
if (!docs.length) console.log('  (ninguno)');

const [ultima] = await consultar(pool, "SELECT MAX(numero) AS n FROM documentos WHERE tipo = 'factura' AND anio = ? AND creado_por IS NULL", [anio]);
if (ultima?.n) console.log(`\nSe conservan las facturas importadas de ${anio}; la más alta es la ${ultima.n}.`);
if (primera !== undefined) {
  console.log(`\nLa próxima factura de ${anio} será la ${Math.max(primera, (ultima?.n ?? 0) + 1)}.`);
}

if (!confirmar) {
  console.log('\nNo se ha borrado nada. Revisa la lista y, si es correcta, repite el comando añadiendo --confirmar.');
} else {
  const r = await borrarDocumentosDePrueba(pool, anio, primera);
  for (const id of r.pedidoIds) fs.rmSync(path.join(rutaUploads(), 'pedidos', String(id)), { recursive: true, force: true });
  const [c] = await consultar(pool, "SELECT ultimo_numero FROM contadores WHERE tipo = 'factura' AND anio = ?", [anio]);
  console.log(`\nBorrados ${r.documentos} documento(s) y ${r.pedidoIds.length} pedido(s). Próxima factura de ${anio}: ${(c?.ultimo_numero ?? 0) + 1}.`);
}
await pool.end();
