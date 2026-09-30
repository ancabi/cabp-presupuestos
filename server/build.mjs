import { build } from 'esbuild';

const comun = {
  bundle: true,
  platform: 'node',
  format: 'esm',
  target: 'node20',
  packages: 'external',
  // @cabp/shared es código TypeScript del monorepo: se incluye en el bundle.
  plugins: [
    {
      name: 'incluir-shared',
      setup(b) {
        b.onResolve({ filter: /^@cabp\/shared$/ }, () => ({
          path: new URL('../shared/src/index.ts', import.meta.url).pathname,
        }));
      },
    },
  ],
  banner: { js: "import { createRequire } from 'module'; const require = createRequire(import.meta.url);" },
};

await build({ ...comun, entryPoints: ['src/index.ts'], outfile: 'dist/index.js' });
await build({
  ...comun,
  entryPoints: ['scripts/migrar.ts', 'scripts/crear-admin.ts', 'scripts/importar-sqlite.ts'],
  outdir: 'dist/scripts',
});
console.log('Servidor compilado en dist/');
