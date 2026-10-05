# --- Dependencias de producción ---
# Se instalan aquí, con herramientas de compilación, por si algún módulo nativo
# (better-sqlite3, argon2) no encuentra binario precompilado para la plataforma
# del servidor (p. ej. ARM64) o no puede descargarlo y tiene que compilarse.
FROM node:22-bookworm-slim AS deps
RUN apt-get update \
 && apt-get install -y --no-install-recommends python3 make g++ ca-certificates \
 && rm -rf /var/lib/apt/lists/*
WORKDIR /app
COPY package.json package-lock.json ./
COPY shared/package.json shared/
COPY server/package.json server/
COPY web/package.json web/
RUN npm ci --omit=dev -w @cabp/server --no-audit --no-fund

# --- Compilación (web + servidor) ---
FROM deps AS build
COPY tsconfig.base.json ./
RUN npm ci --no-audit --no-fund
COPY shared shared
COPY server server
COPY web web
RUN npm run build

# --- Ejecución ---
FROM node:22-bookworm-slim
ENV NODE_ENV=production \
    PORT=3000 \
    UPLOADS_DIR=/data/uploads
WORKDIR /app
COPY --from=deps /app ./
COPY --from=build /app/server/dist server/dist
COPY --from=build /app/web/dist web/dist
COPY docker-entrypoint.sh ./
# /app/uploads también, por si el volumen se monta ahí (UPLOADS_DIR=/app/uploads).
RUN chmod 755 docker-entrypoint.sh \
 && mkdir -p /data/uploads /app/uploads \
 && chown -R node:node /data /app/uploads
VOLUME /data/uploads
EXPOSE 3000
# El entrypoint arranca como root, da permisos sobre UPLOADS_DIR y ejecuta la app como «node».
ENTRYPOINT ["/app/docker-entrypoint.sh"]
CMD ["node", "server/dist/index.js"]
