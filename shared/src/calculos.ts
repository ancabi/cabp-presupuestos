/**
 * Cálculo de totales de presupuestos y facturas.
 *
 * Portado de PanelPresupuesto.calcularTotales() (versión Java). Todas las
 * operaciones se hacen en céntimos enteros para evitar errores de coma flotante;
 * los importes de entrada y salida están en euros.
 */

/** Recargo que se aplica sobre la ganancia cuando "aplicaGanancia" está activo. */
export const IMPUESTO_GANANCIA = 0.2;
/** Consumo por defecto (litros a los 100 km) de la calculadora de viajes. */
export const CONSUMO_POR_DEFECTO = 7;

export interface LineaCalculo {
  cantidad: number;
  precio: number;
}

export interface DatosCalculo {
  lineas: LineaCalculo[];
  ganancia: number;
  restaurante: number;
  pasaje: number;
  combustible: number;
  otros: number;
  hotel: number;
  transporte: number;
  aplicaGanancia: boolean;
  aplicaIva: boolean;
  ivaPorcentaje: number;
  totalManualActivo: boolean;
  totalManual: number;
  porcentajeReparto: number;
}

export interface Totales {
  subtotalLineas: number;
  totalGastos: number;
  impuestoGanancia: number;
  neto: number;
  totalSinIva: number;
  totalIva: number;
  totalConIva: number;
  reparto: [number, number];
}

/** Redondeo simétrico (-0,5 -> -1), para que un abono sea el espejo exacto de su factura. */
const redondear0 = (x: number): number => (x < 0 ? -Math.round(-x) : Math.round(x));
const aCentimos = (euros: number): number => redondear0((Number(euros) || 0) * 100);
const aEuros = (centimos: number): number => centimos / 100;

export function importeLinea(linea: LineaCalculo): number {
  return aEuros(redondear0(aCentimos(linea.precio) * (Number(linea.cantidad) || 0)));
}

/**
 * Línea principal del PDF con los gastos y la ganancia repartidos entre sus unidades:
 * precio unidad = precio + extra / cantidad, y total = importe + extra (exacto, para que cuadre con la base imponible).
 * El precio unidad se redondea a céntimos, así que precio × cantidad puede diferir del total en algún céntimo.
 * Con cantidad 0 no se puede repartir: el extra solo se suma al total.
 */
export function lineaConGastos(linea: LineaCalculo, extra: number): { precio: number; total: number } {
  const cantidad = Number(linea.cantidad) || 0;
  const total = aEuros(aCentimos(importeLinea(linea)) + aCentimos(extra));
  const precio = cantidad ? aEuros(redondear0(aCentimos(linea.precio) + aCentimos(extra) / cantidad)) : Number(linea.precio) || 0;
  return { precio, total };
}

export function calcularTotales(d: DatosCalculo): Totales {
  const subtotal = d.lineas.reduce(
    (acc, l) => acc + redondear0(aCentimos(l.precio) * (Number(l.cantidad) || 0)),
    0,
  );
  const gastos =
    aCentimos(d.hotel) +
    aCentimos(d.pasaje) +
    aCentimos(d.otros) +
    aCentimos(d.restaurante) +
    aCentimos(d.combustible);
  const ganancia = aCentimos(d.ganancia);
  const impGanancia = d.aplicaGanancia ? redondear0(ganancia * IMPUESTO_GANANCIA) : 0;
  const neto = d.totalManualActivo ? aCentimos(d.totalManual) : subtotal;
  const sinIva = neto + gastos + ganancia + aCentimos(d.transporte) + impGanancia;
  const iva = d.aplicaIva ? redondear0((sinIva * (Number(d.ivaPorcentaje) || 0)) / 100) : 0;
  const conIva = sinIva + iva;
  const p = Math.min(100, Math.max(0, Number(d.porcentajeReparto) || 0));
  const parte1 = redondear0((conIva * p) / 100);

  return {
    subtotalLineas: aEuros(subtotal),
    totalGastos: aEuros(gastos),
    impuestoGanancia: aEuros(impGanancia),
    neto: aEuros(neto),
    totalSinIva: aEuros(sinIva),
    totalIva: aEuros(iva),
    totalConIva: aEuros(conIva),
    reparto: [aEuros(parte1), aEuros(conIva - parte1)],
  };
}

/**
 * Coste estimado de combustible de los desplazamientos:
 * km totales = kilómetros × nº de viajes; litros = km totales × consumo (l/100 km) / 100;
 * coste = litros × precio de la gasolina (€/l).
 */
export function calcularViaje(
  kilometros: number,
  numViajes: number,
  consumo100km: number,
  precioGasolina: number,
): number {
  const kmTotales = (Number(kilometros) || 0) * (Number(numViajes) || 0);
  const litros = (kmTotales * (Number(consumo100km) || 0)) / 100;
  return Math.round(litros * (Number(precioGasolina) || 0) * 100) / 100;
}

/**
 * Calculadora de escalera (DialogoPitagoras).
 * - pitagoras: A y B son las medidas de un peldaño (cm) y C el nº de peldaños.
 *   hipotenusa = √((A·C)² + (B·C)²) en cm; total = metros + extra de plataforma (aux, en metros).
 * - stepper: A = V, B = A, C = O. hipotenusa = √((V−A)² + O²).
 */
export function calcularEscalera(
  tipo: 'pitagoras' | 'stepper',
  a: number,
  b: number,
  c: number,
  aux: number,
): { catetoA: number; catetoB: number; hipotenusa: number; metros: number; total: number } {
  if (tipo === 'stepper') {
    const catetoA = a - b;
    const hip = Math.sqrt(catetoA ** 2 + c ** 2);
    return {
      catetoA,
      catetoB: c,
      hipotenusa: redondear(hip),
      metros: redondear(hip / 100),
      total: redondear(hip),
    };
  }
  const catetoA = a * c;
  const catetoB = b * c;
  const hip = Math.sqrt(catetoA ** 2 + catetoB ** 2);
  return {
    catetoA,
    catetoB,
    hipotenusa: redondear(hip),
    metros: redondear(hip / 100),
    total: redondear(hip / 100 + (Number(aux) || 0)),
  };
}

function redondear(n: number): number {
  return Math.round(n * 100) / 100;
}
