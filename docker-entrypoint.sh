#!/bin/sh
# Arranca como root solo para dejar la carpeta de adjuntos escribible por el
# usuario «node» (los volúmenes de Docker/Coolify suelen montarse con dueño root)
# y después ejecuta la aplicación como «node».
set -e

dir="${UPLOADS_DIR:-/data/uploads}"
# Las rutas relativas se interpretan desde la raíz de la aplicación, igual que en el servidor.
case "$dir" in
  /*) ;;
  *) dir="/app/$dir" ;;
esac

if [ "$(id -u)" = "0" ]; then
  mkdir -p "$dir"
  # Solo se recorre la carpeta entera si el dueño no es el correcto (p. ej. un volumen recién creado).
  if [ "$(stat -c %U "$dir")" != "node" ]; then
    echo "Ajustando permisos de $dir para el usuario node"
    chown -R node:node "$dir"
  fi
  exec setpriv --reuid=node --regid=node --init-groups "$@"
fi

exec "$@"
