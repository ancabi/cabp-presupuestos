import type { CSSProperties } from 'react';
import { formatoEuros, variablesPdf, type Ajustes } from '@cabp/shared';
import { CabeceraDocumento } from './CabeceraDocumento';
import '../fuentesPdf';
import '../pages/impresion.css';

/** Muestra reducida de la hoja impresa con el aspecto que se está editando en Ajustes. */
export function VistaPreviaPdf({ ajustes }: { ajustes: Ajustes }) {
  const lineas = [
    { cant: 1, concepto: 'Silla salvaescaleras recta', precio: 2500.5 },
    { cant: 1, concepto: 'Instalación y transporte', precio: 300 },
  ];
  const base = lineas.reduce((s, l) => s + l.cant * l.precio, 0);
  const iva = Math.round(base * ajustes.ivaPorcentaje) / 100;
  return (
    <article className="hoja muestra" style={variablesPdf(ajustes.pdf) as CSSProperties}>
      <CabeceraDocumento empresa={ajustes.empresa} tipo="FACTURA" codigo="F-2026/0001" fecha="05/10/2026" />
      <div className="muestra-cuerpo">
        <section className="cliente">
          <div className="titulo-caja">Facturar a</div>
          <div className="cliente-nombre">María López Ruiz</div>
          <div>C/ Sol 1 · 29631 Málaga</div>
        </section>
        <table className="lineas">
          <thead>
            <tr>
              <th className="cant">Cant.</th>
              <th>Concepto</th>
              <th className="num">Precio unidad</th>
              <th className="num">Total</th>
            </tr>
          </thead>
          <tbody>
            {lineas.map((l) => (
              <tr key={l.concepto}>
                <td className="cant">{l.cant}</td>
                <td>{l.concepto}</td>
                <td className="num">{formatoEuros(l.precio)}</td>
                <td className="num">{formatoEuros(l.cant * l.precio)}</td>
              </tr>
            ))}
          </tbody>
        </table>
        <table className="totales">
          <tbody>
            <tr>
              <th>Base imponible</th>
              <td>{formatoEuros(base)}</td>
            </tr>
            <tr>
              <th>IVA {ajustes.ivaPorcentaje} %</th>
              <td>{formatoEuros(iva)}</td>
            </tr>
            <tr className="total">
              <th>TOTAL</th>
              <td>{formatoEuros(base + iva)}</td>
            </tr>
          </tbody>
        </table>
      </div>
    </article>
  );
}
