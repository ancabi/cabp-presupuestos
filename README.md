# CABP · Presupuestos y facturas

Aplicación web para gestionar clientes, distribuidores y sus productos, presupuestos y facturas, e imágenes y PDFs de cada cliente.

Es la reescritura de la aplicación Java Swing original (proyecto fin de grado, 2011). El código antiguo se conserva en [`cabpTrunk/`](cabpTrunk/) como referencia.

## Novedades respecto a la versión Java

- **Una sola base de datos para todos los años.** Presupuestos y facturas se numeran por año: `P-2026/0001`, `F-2026/0001`. La numeración vuelve a 1 cada 1 de enero. Clientes, distribuidores y productos se comparten entre años. Arriba a la derecha se elige el **ejercicio** que se quiere ver.
- **MariaDB** en lugar de SQLite, para poder publicar la aplicación. Tiene **login de usuarios** con roles (administrador / usuario).
- **PDF sin librerías.** Cada documento tiene una vista A4 imprimible y el navegador genera el PDF con «Imprimir → Guardar como PDF». Imprime todas las líneas, el IVA real y los datos de la empresa configurados en *Ajustes*.
- **Numeración segura.** El número se asigna al guardar, dentro de una transacción, así que nunca hay números repetidos. Una factura solo se puede borrar si es la última del año, para no dejar huecos.
- **Presupuesto → factura.** La conversión enlaza ambos documentos e impide convertir dos veces.
- **Totales calculados en el servidor.** Se usa la misma función que en la pantalla ([`shared/src/calculos.ts`](shared/src/calculos.ts)) y se trabaja en céntimos, sin errores de redondeo.
- **Errores corregidos de la versión anterior:**
  - al editar se perdía la fecha;
  - los emails de clientes nuevos no se guardaban;
  - la dirección del distribuidor no se actualizaba;
  - los valores de la calculadora de escalera no se guardaban;
  - al buscar distribuidores por posición en la lista se cogía el equivocado.

## Tecnología

| Parte | Tecnología |
|---|---|
| Backend | Node.js 20+, Fastify, MariaDB (mysql2), zod, argon2 |
| Frontend | React 18 + Vite + TypeScript, Mantine UI, TanStack Query, React Router |
| Común | `shared/`: cálculos, validaciones y tipos usados por el frontend y el backend |
| Tests | Vitest (cálculos + API contra MariaDB) |

```
shared/   lógica común (cálculos de totales, esquemas de validación, tipos)
server/   API REST (/api), migraciones, scripts (crear-admin, importar)
web/      interfaz React
cabpTrunk/ versión Java original
```

## Desarrollo local

Requisitos: Node.js 20 o superior y un MariaDB (o MySQL 8) accesible.

```bash
# 1. Base de datos
sudo mariadb -e "CREATE DATABASE cabp CHARACTER SET utf8mb4;
  CREATE USER 'cabp'@'localhost' IDENTIFIED BY 'cabp';
  GRANT ALL ON cabp.* TO 'cabp'@'localhost';"

# 2. Configuración y dependencias
cp .env.example .env        # y edita los valores
npm install

# 3. Primer usuario administrador (crea las tablas si no existen)
npm run crear-admin -- tu@email.com "Tu nombre"

# 4. Arrancar: API en :3000 y web en http://localhost:5173
npm run dev
```

Otros comandos:

```bash
npm test            # tests (necesitan la BD cabp_test, ver abajo)
npm run typecheck   # comprobación de tipos de todo el proyecto
npm run build       # compila web/dist y server/dist
npm start           # sirve la API y la web compilada en :3000
```

Los tests de la API usan una base de datos aparte, que **se borra en cada ejecución**. Por defecto es `mysql://cabp:cabp@localhost:3306/cabp_test`; se cambia con `TEST_DATABASE_URL`.

```bash
sudo mariadb -e "CREATE DATABASE cabp_test CHARACTER SET utf8mb4; GRANT ALL ON cabp_test.* TO 'cabp'@'localhost';"
```

## Importar los SQLite antiguos (uno por año)

Se importa cada fichero indicando su año. Si el nombre contiene el año (`cabp2011.sqlite`), se deduce solo.

```bash
npm run importar -- ruta/cabp2011.sqlite --dry-run                 # simulación, no guarda nada
npm run importar -- ruta/cabp2011.sqlite --adjuntos ruta/carpeta   # importación real
npm run importar -- --anio 2012 ruta/otra.sqlite --iva 18
```

Qué hace la importación:

- **Clientes:** se fusionan por DNI, o por nombre y apellidos si no tienen DNI. Así, un cliente que aparece en varios años queda como un único cliente.
- **Distribuidores y productos:** se fusionan por nombre.
- **Presupuestos y facturas:** conservan su número antiguo dentro de su año. El contador del año queda en el máximo importado, de modo que el siguiente documento continúa la serie.
- **`--adjuntos`:** carpeta donde estaban las carpetas `<id><Nombre><Apellidos>` que creaba la versión Java. Sus imágenes y PDFs se copian a la ficha del cliente.
- **`--iva`:** IVA que se aplica a los documentos antiguos que no lo tenían guardado. Por defecto es 21.
- **Repetir la importación** es seguro: los documentos ya importados se omiten.
- **Si un documento no es válido**, la importación se cancela entera y el error indica cuál es (p. ej. `factura 6 (año 2012) no se puede importar: …`). Las facturas de devolución con importes negativos se importan sin problema.

## Publicación con Docker

```bash
cp .env.example .env
# Rellena en .env: DB_PASSWORD, DB_ROOT_PASSWORD y SESSION_SECRET (openssl rand -hex 32)
docker compose up -d --build
docker compose exec app node server/dist/scripts/crear-admin.js tu@email.com "Tu nombre"
```

- La aplicación queda en `127.0.0.1:3000`. Publícala detrás de un proxy inverso con HTTPS, por ejemplo este `Caddyfile`:

  ```
  presupuestos.tudominio.es {
      reverse_proxy 127.0.0.1:3000
  }
  ```

- Para importar los SQLite antiguos en el servidor:

  ```bash
  docker compose cp cabp2011.sqlite app:/tmp/
  docker compose exec app node server/dist/scripts/importar-sqlite.js /tmp/cabp2011.sqlite
  ```

- Las migraciones de la base de datos se aplican solas al arrancar.

### Copias de seguridad

Hay que guardar dos cosas: la base de datos y la carpeta de adjuntos.

```bash
docker compose exec db mariadb-dump -u root -p"$DB_ROOT_PASSWORD" cabp > cabp-$(date +%F).sql
docker compose cp app:/data/uploads ./uploads-$(date +%F)
```

## Seguridad

- **Contraseñas:** se guardan con hash argon2.
- **Sesiones:**
  - se guardan en la base de datos;
  - la cookie es `httpOnly`, `SameSite=Lax`, va firmada y es `secure` en producción;
  - duran 7 días;
  - al cambiar la contraseña o desactivar un usuario, se cierran sus sesiones.
- **Login:** limitado a 10 intentos cada 5 minutos por IP.
- **CSRF:** toda petición de escritura debe llevar la cabecera `X-Requested-With: cabp`.
- **Adjuntos:**
  - solo se aceptan imágenes y PDF, de hasta 20 MB;
  - se guardan con un nombre aleatorio;
  - solo se sirven a usuarios con sesión iniciada.
- **Permisos:** solo los administradores pueden cambiar los ajustes y gestionar usuarios.
