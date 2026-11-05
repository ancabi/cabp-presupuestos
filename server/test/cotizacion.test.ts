import { describe, expect, it, vi } from 'vitest';
import { cotizacionUsd } from '../src/services/cotizacion';

const respuesta = (cuerpo: unknown, ok = true) =>
  vi.fn(async (_url: string) => ({ ok, status: ok ? 200 : 500, json: async () => cuerpo }));

describe('cotización del dólar', () => {
  it('pide la del día del documento y la guarda en caché', async () => {
    const pedir = respuesta({ date: '2025-03-14', rates: { USD: 1.0879 } });
    expect(await cotizacionUsd('2025-03-16', pedir)).toEqual({ tipoCambio: 1.0879, fecha: '2025-03-14' });
    expect(pedir.mock.calls[0]![0]).toContain('/2025-03-16?base=EUR&symbols=USD');
    await cotizacionUsd('2025-03-16', pedir);
    expect(pedir).toHaveBeenCalledTimes(1);
  });

  it('usa la más reciente para hoy o fechas futuras', async () => {
    const pedir = respuesta({ date: '2026-11-04', rates: { USD: 1.1 } });
    await cotizacionUsd('2999-01-01', pedir);
    expect(pedir.mock.calls[0]![0]).toContain('/latest?');
  });

  it('da un error claro si la API falla o responde algo raro', async () => {
    await expect(cotizacionUsd('2020-01-02', respuesta({}, false))).rejects.toMatchObject({ statusCode: 502 });
    await expect(cotizacionUsd('2020-01-03', respuesta({ date: '2020-01-03', rates: {} }))).rejects.toMatchObject({ statusCode: 502 });
    const caida = vi.fn(async (_url: string) => {
      throw new Error('sin red');
    });
    await expect(cotizacionUsd('2020-01-06', caida)).rejects.toMatchObject({ statusCode: 502 });
  });
});
