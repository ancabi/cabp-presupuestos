export type TipoDocumento = 'presupuesto' | 'factura';

const PREFIJO: Record<TipoDocumento, string> = { presupuesto: 'P', factura: 'F' };

/** Número visible de un documento, p. ej. F-2026/0001 */
export function numeroDocumento(tipo: TipoDocumento, anio: number, numero: number): string {
  return `${PREFIJO[tipo]}-${anio}/${String(numero).padStart(4, '0')}`;
}

const euro = new Intl.NumberFormat('es-ES', {
  style: 'currency',
  currency: 'EUR',
  minimumFractionDigits: 2,
  maximumFractionDigits: 2,
});

export function formatoEuros(n: number): string {
  return euro.format(Number(n) || 0);
}

const dolar = new Intl.NumberFormat('es-ES', {
  style: 'currency',
  currency: 'USD',
  minimumFractionDigits: 2,
  maximumFractionDigits: 2,
});

/** 1234.5 -> "1.234,50 US$" */
export function formatoDolares(n: number): string {
  return dolar.format(Number(n) || 0);
}

/** Total en dólares a partir del total en euros y la cotización (US$ por €), redondeado a céntimos. */
export function aDolares(euros: number, tipoCambio: number): number {
  return Math.round(euros * tipoCambio * 100) / 100;
}

/** 'YYYY-MM-DD' -> 'DD/MM/YYYY' */
export function formatoFecha(iso: string): string {
  const [y, m, d] = iso.slice(0, 10).split('-');
  return `${d}/${m}/${y}`;
}

export function anioDeFecha(iso: string): number {
  return Number(iso.slice(0, 4));
}

export function hoyIso(): string {
  const d = new Date();
  const mm = String(d.getMonth() + 1).padStart(2, '0');
  const dd = String(d.getDate()).padStart(2, '0');
  return `${d.getFullYear()}-${mm}-${dd}`;
}
