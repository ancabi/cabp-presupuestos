/** Aspecto configurable del PDF de presupuestos y facturas (Ajustes → Aspecto del PDF). */

export const FUENTES_PDF = [
  { value: 'helvetica', label: 'Helvetica / Arial', css: "'Helvetica Neue', Helvetica, Arial, sans-serif" },
  { value: 'times', label: 'Times New Roman', css: "'Times New Roman', Times, serif" },
  { value: 'georgia', label: 'Georgia', css: 'Georgia, serif' },
  { value: 'roboto', label: 'Roboto', css: "'Roboto', Arial, sans-serif" },
  { value: 'open-sans', label: 'Open Sans', css: "'Open Sans', Arial, sans-serif" },
  { value: 'lato', label: 'Lato', css: "'Lato', Arial, sans-serif" },
  { value: 'merriweather', label: 'Merriweather', css: "'Merriweather', Georgia, serif" },
] as const;

export type FuentePdf = (typeof FUENTES_PDF)[number]['value'];

export interface AspectoPdf {
  fuente: FuentePdf;
  tamanoLetra: number;
  colorPrincipal: string;
  colorTexto: string;
  tamanoLogo: number;
}

/** Valores con los que se diseñó la plantilla; el tamaño de letra es la base a la que escala todo. */
export const PDF_POR_DEFECTO: AspectoPdf = {
  fuente: 'helvetica',
  tamanoLetra: 8.5,
  colorPrincipal: '#0c8599',
  colorTexto: '#222222',
  tamanoLogo: 44,
};

/**
 * Color del texto (blanco o negro) sobre un fondo del color dado, p. ej. la cabecera de la tabla de líneas.
 * Blanco mientras su contraste sea ≥ 3:1 (WCAG AA para texto en negrita); con fondos claros, negro.
 */
export function textoSobre(fondo: string): '#ffffff' | '#000000' {
  const m = /^#([0-9a-f]{2})([0-9a-f]{2})([0-9a-f]{2})$/i.exec(fondo);
  if (!m) return '#ffffff';
  const [r, g, b] = m.slice(1).map((h) => {
    const c = parseInt(h, 16) / 255;
    return c <= 0.03928 ? c / 12.92 : ((c + 0.055) / 1.055) ** 2.4;
  });
  const l = 0.2126 * r + 0.7152 * g + 0.0722 * b;
  return 1.05 / (l + 0.05) >= 3 ? '#ffffff' : '#000000';
}

/** Variables CSS que consume impresion.css. */
export function variablesPdf(a: AspectoPdf): Record<string, string> {
  const fuente = FUENTES_PDF.find((f) => f.value === a.fuente) ?? FUENTES_PDF[0];
  return {
    '--pdf-fuente': fuente.css,
    '--pdf-escala': String(a.tamanoLetra / PDF_POR_DEFECTO.tamanoLetra),
    '--pdf-color': a.colorPrincipal,
    '--pdf-sobre-color': textoSobre(a.colorPrincipal),
    '--pdf-texto': a.colorTexto,
    '--pdf-logo': `${a.tamanoLogo}px`,
  };
}
