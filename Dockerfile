# --- Compilación ---
FROM node:22-bookworm-slim AS build
WORKDIR /app
COPY package.json package-lock.json tsconfig.base.json ./
COPY shared/package.json shared/
COPY server/package.json server/
COPY web/package.json web/
RUN npm ci
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
COPY package.json package-lock.json ./
COPY shared/package.json shared/
COPY server/package.json server/
COPY web/package.json web/
RUN npm ci --omit=dev -w @cabp/server && npm cache clean --force
COPY --from=build /app/server/dist server/dist
COPY --from=build /app/web/dist web/dist
RUN mkdir -p /data/uploads && chown -R node:node /data
USER node
VOLUME /data/uploads
EXPOSE 3000
CMD ["node", "server/dist/index.js"]
