import { describe, expect, it } from 'vitest';
import { calcularEscalera, calcularTotales, calcularViaje, type DatosCalculo } from './calculos';
import { numeroDocumento } from './formato';
import { textoSobre, variablesPdf, PDF_POR_DEFECTO } from './pdf';

const base: DatosCalculo = {
  lineas: [],
  ganancia: 0,
  restaurante: 0,
  pasaje: 0,
  combustible: 0,
  otros: 0,
  hotel: 0,
  transporte: 0,
  aplicaGanancia: true,
  aplicaIva: true,
  ivaPorcentaje: 21,
  totalManualActivo: false,
  totalManual: 0,
  porcentajeReparto: 50,
};

describe('calcularTotales', () => {
  it('suma líneas, gastos, ganancia con su recargo del 20% e IVA', () => {
    const t = calcularTotales({
      ...base,
      lineas: [
        { cantidad: 2, precio: 1000 },
        { cantidad: 1, precio: 250.5 },
      ],
      hotel: 100,
      pasaje: 50,
      otros: 10,
      restaurante: 40,
      combustible: 60,
      ganancia: 500,
      transporte: 200,
    });
    expect(t.subtotalLineas).toBe(2250.5);
    expect(t.totalGastos).toBe(260);
    expect(t.impuestoGanancia).toBe(100);
    // 2250.5 + 260 + 500 + 200 + 100
    expect(t.totalSinIva).toBe(3310.5);
    expect(t.totalIva).toBe(695.21); // 3310.5 * 0.21 = 695.205 -> 695.21
    expect(t.totalConIva).toBe(4005.71);
    expect(t.reparto[0] + t.reparto[1]).toBeCloseTo(4005.71, 2);
  });

  it('sin ganancia ni IVA', () => {
    const t = calcularTotales({
      ...base,
      lineas: [{ cantidad: 3, precio: 0.1 }],
      ganancia: 100,
      aplicaGanancia: false,
      aplicaIva: false,
    });
    expect(t.subtotalLineas).toBe(0.3);
    expect(t.impuestoGanancia).toBe(0);
    expect(t.totalSinIva).toBe(100.3);
    expect(t.totalIva).toBe(0);
    expect(t.totalConIva).toBe(100.3);
  });

  it('el total manual sustituye a la suma de líneas', () => {
    const t = calcularTotales({
      ...base,
      lineas: [{ cantidad: 1, precio: 999 }],
      totalManualActivo: true,
      totalManual: 1000,
      ivaPorcentaje: 7,
      porcentajeReparto: 40,
    });
    expect(t.neto).toBe(1000);
    expect(t.totalConIva).toBe(1070);
    expect(t.reparto).toEqual([428, 642]);
  });
});

describe('importes negativos (abonos)', () => {
  it('total manual negativo', () => {
    const t = calcularTotales({ ...base, totalManualActivo: true, totalManual: -1000 });
    expect(t.totalSinIva).toBe(-1000);
    expect(t.totalIva).toBe(-210);
    expect(t.totalConIva).toBe(-1210);
  });

  it('un abono es el espejo exacto de su factura, también con medios céntimos', () => {
    // 0,025 € * 21 % = 0,00525 -> redondeos en el límite del medio céntimo
    for (const linea of [{ cantidad: 1, precio: 10.5 }, { cantidad: 3, precio: 0.05 }, { cantidad: 1, precio: 2.5 }]) {
      for (const iva of [21, 10, 7]) {
        const factura = calcularTotales({ ...base, ivaPorcentaje: iva, ganancia: 0.05, lineas: [linea] });
        const abono = calcularTotales({
          ...base,
          ivaPorcentaje: iva,
          ganancia: -0.05,
          lineas: [{ ...linea, cantidad: -linea.cantidad }],
        });
        expect(abono.totalIva).toBe(-factura.totalIva);
        expect(abono.totalConIva).toBe(-factura.totalConIva);
      }
    }
  });
});

describe('calculadoras', () => {
  it('viaje = km * viajes * 0.07 * precio', () => {
    expect(calcularViaje(100, 2, 1.5)).toBe(21);
  });

  it('pitágoras', () => {
    const r = calcularEscalera('pitagoras', 30, 40, 10, 1.2);
    expect(r.hipotenusa).toBe(500);
    expect(r.metros).toBe(5);
    expect(r.total).toBe(6.2);
  });

  it('stepper', () => {
    expect(calcularEscalera('stepper', 350, 50, 400, 0).hipotenusa).toBe(500);
  });
});

describe('numeroDocumento', () => {
  it('formatea con prefijo, año y 4 cifras', () => {
    expect(numeroDocumento('factura', 2026, 7)).toBe('F-2026/0007');
    expect(numeroDocumento('presupuesto', 2011, 12345)).toBe('P-2011/12345');
  });
});

describe('aspecto del PDF', () => {
  it('elige texto blanco o negro según el color de fondo', () => {
    expect(textoSobre('#0c8599')).toBe('#ffffff');
    expect(textoSobre('#8b1e3f')).toBe('#ffffff');
    expect(textoSobre('#ffd43b')).toBe('#000000');
    expect(textoSobre('#e9ecef')).toBe('#000000');
  });
  it('escala respecto al tamaño de diseño (8,5 pt)', () => {
    expect(variablesPdf(PDF_POR_DEFECTO)['--pdf-escala']).toBe('1');
    expect(variablesPdf({ ...PDF_POR_DEFECTO, tamanoLetra: 17 })['--pdf-escala']).toBe('2');
  });
});
