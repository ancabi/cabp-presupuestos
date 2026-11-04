---
target: the factura page
total_score: 24
max_score: 40
na_heuristics: 
p0_count: 1
p1_count: 2
target_identity: "file:/home/user/cabp-presupuestos/web/src/pages/DocumentoEditorPage.tsx"
target_fingerprint: "sha256:0282956ec941c728cc636cd2511e9de75e795389c7b341a090c4c9af5a31bc47"
target_path: /home/user/cabp-presupuestos/web/src/pages/DocumentoEditorPage.tsx
timestamp: 2026-11-04T18-52-58Z
slug: web-src-pages-documentoeditorpage-tsx
---
# Critique: editor de factura (web/src/pages/DocumentoEditorPage.tsx)
Method: dual-agent (A: design review · B: detector + browser)

| # | Heurística | Nota | Problema clave |
|---|---|---|---|
| 1 | Visibilidad del estado | 3 | El título muestra "(nuevo · F-2026/0001)" aunque el número ya esté ocupado |
| 2 | Lenguaje del usuario | 3 | "IVA" fijo en una empresa canaria (IGIC); "texto principal del factura" |
| 3 | Control y libertad | 1 | Navegar por el menú con cambios sin guardar los pierde sin avisar |
| 4 | Consistencia | 2 | "3.200 €" en el input vs "3200,00 €" en Importe; Transporte dentro de Gastos pero aparte en Totales |
| 5 | Prevención de errores | 2 | Sin comprobación del número antes de guardar; borrar ofrecido en todas las facturas |
| 6 | Reconocimiento | 3 | "+20% sobre ganancia" sin explicar |
| 7 | Flexibilidad | 2 | Sin Ctrl+S; "Línea libre" no lleva el foco a la línea nueva |
| 8 | Estética minimalista | 3 | Nueve tarjetas con el mismo peso; bloques poco usados siempre abiertos |
| 9 | Recuperación de errores | 2 | Errores solo en toast que tapa Guardar, sin marcar el campo |
| 10 | Ayuda | 3 | Buenas descripciones; la regla de borrado solo aparece al intentarlo |
| **Total** | | **24/40** | Aceptable |

Priority issues:
1. [P0] Se pierden cambios al navegar dentro de la app (useBlocker + confirmación).
2. [P1] Errores solo como toast sobre la barra de acciones (error en el campo, foco, comprobación en vivo del número).
3. [P1] Contraste WCAG AA: teal #12b886 en Guardar/enlaces 2.6:1, descripciones 3.3:1, placeholders 2.1:1 (tema: primaryShade 8, gray.7).
4. [P2] Botón "Pasar a combustible" recortado (desbordamiento 33 px); "Nº de factura" solo en una fila con descripción que corta.
5. [P2] Móvil: desbordamiento horizontal (543 px en 390 px), barra de Líneas no envuelve, TOTAL al 80 % de la página.
Detector: CLI 0 hallazgos; navegador 10 (factura 1) / 12 (nueva): low-contrast, text-overflow, skipped-heading h2→h4, stragglers (falsos positivos menores).
