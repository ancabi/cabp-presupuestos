import { crearPool } from '../src/db/pool';
import { migrar } from '../src/db/migrar';

const url = process.env.DATABASE_URL;
if (!url) throw new Error('Falta DATABASE_URL');
const pool = crearPool(url);
await migrar(pool, console.log);
console.log('Base de datos al día.');
await pool.end();
