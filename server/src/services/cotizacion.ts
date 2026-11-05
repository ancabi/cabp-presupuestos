import { hoyIso } from '@cabp/shared';
import { HttpError } from '../errores';

/**
 * Cotización euro → dólar del Banco Central Europeo, a través de Frankfurter (gratuita y sin clave).
 * Para una fecha pasada devuelve la del último día hábil hasta esa fecha; para hoy o el futuro, la más reciente.
 */
const API = 'https://api.frankfurter.dev/v1';

export interface Cotizacion {
  /** Dólares por euro. */
  tipoCambio: number;
  /** Día de la cotización publicada (puede ser anterior a la pedida: fines de semana y festivos). */
  fecha: string;
}

const cache = new Map<string, { valor: Cotizacion; hasta: number }>();
const UNA_HORA = 60 * 60 * 1000;

/** Lo mínimo de `fetch` que se usa; los tests pasan uno simulado. */
type Pedir = (url: string, init?: RequestInit) => Promise<Pick<Response, 'ok' | 'status' | 'json'>>;

export async function cotizacionUsd(fecha?: string, pedir: Pedir = fetch): Promise<Cotizacion> {
  const dia = fecha && fecha < hoyIso() ? fecha : 'latest';
  const guardada = cache.get(dia);
  if (guardada && guardada.hasta > Date.now()) return guardada.valor;
  let datos: { date?: string; rates?: { USD?: number } };
  try {
    const r = await pedir(`${API}/${dia}?base=EUR&symbols=USD`, { signal: AbortSignal.timeout(5000) });
    if (!r.ok) throw new Error(`HTTP ${r.status}`);
    datos = (await r.json()) as typeof datos;
  } catch {
    throw new HttpError(502, 'No se pudo obtener la cotización del dólar. Escríbela a mano.');
  }
  const tipoCambio = datos.rates?.USD;
  if (typeof tipoCambio !== 'number' || !(tipoCambio > 0) || !datos.date) {
    throw new HttpError(502, 'La cotización del dólar recibida no es válida. Escríbela a mano.');
  }
  const valor = { tipoCambio, fecha: datos.date };
  // Las de días pasados no cambian; la más reciente se refresca cada hora.
  cache.set(dia, { valor, hasta: dia === 'latest' ? Date.now() + UNA_HORA : Infinity });
  return valor;
}
