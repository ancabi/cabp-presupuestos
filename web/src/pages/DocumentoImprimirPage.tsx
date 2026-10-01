import { useEffect, useLayoutEffect, useRef } from 'react';
import { Button, Center, Group, Loader, Text } from '@mantine/core';
import { IconArrowLeft, IconPrinter } from '@tabler/icons-react';
import { Link, useParams } from 'react-router-dom';
import { formatoEuros, formatoFecha, importeLinea } from '@cabp/shared';
import { useAjustes, useDocumento } from '../api/hooks';
import './impresion.css';

/**
 * Versión imprimible (A4) de un presupuesto o factura.
 * Sustituye a los informes de JasperReports: el navegador genera el PDF con "Imprimir → Guardar como PDF".
 *
 * Cabecera y pie se repiten en todas las páginas al imprimir: son elementos `position: fixed` (Chrome,
 * Edge y Firefox los pintan en cada página) y el contenido va dentro de una tabla cuyo thead/tfoot,
 * que el navegador también repite, reservan exactamente su altura para que nada quede debajo.
 */
export function DocumentoImprimirPage() {
  const id = Number(useParams().id);
  const { data: doc, isLoading } = useDocumento(id);
  const { data: ajustes } = useAjustes();
  const hojaRef = useRef<HTMLElement>(null);
  const cabeceraRef = useRef<HTMLElement>(null);
  const pieRef = useRef<HTMLElement>(null);

  // Reserva en cada página el alto real de la cabecera y el pie (dependen de los textos de Ajustes).
  useLayoutEffect(() => {
    const medir = () => {
      const hoja = hojaRef.current;
      if (!hoja || !cabeceraRef.current || !pieRef.current) return;
      hoja.style.setProperty('--alto-cabecera', `${cabeceraRef.current.offsetHeight}px`);
      hoja.style.setProperty('--alto-pie', `${pieRef.current.offsetHeight}px`);
    };
    medir();
    window.addEventListener('beforeprint', medir);
    const imgs = hojaRef.current?.querySelectorAll('img') ?? [];
    imgs.forEach((i) => i.addEventListener('load', medir));
    return () => {
      window.removeEventListener('beforeprint', medir);
      imgs.forEach((i) => i.removeEventListener('load', medir));
    };
  });

  useEffect(() => {
    if (doc) {
      const cliente = doc.cliente ? ` ${doc.cliente.nombre} ${doc.cliente.apellidos}`.trimEnd() : '';
      document.title = `${doc.codigo.replace('/', '-')}${cliente}`;
    }
    return () => {
      document.title = 'CABP · Presupuestos y facturas';
    };
  }, [doc]);

  if (isLoading || !ajustes)
    return (
      <Center h="100vh">
        <Loader />
      </Center>
    );
  if (!doc) return <Text p="xl">Documento no encontrado.</Text>;

  const e = ajustes.empresa;
  const c = doc.cliente;
  const esFactura = doc.tipo === 'factura';
  const neto = doc.totalManualActivo ? doc.totalManual : doc.lineas.reduce((a, l) => a + importeLinea(l), 0);
  const otrosConceptos = Math.round((doc.totalSinIva - neto) * 100) / 100;
  const lineas = doc.totalManualActivo
    ? [{ nombreProducto: doc.textoConcepto.split('\n')[0] || 'Según concepto', cantidad: 1, precio: doc.totalManual }]
    : doc.lineas;

  return (
    <div className="impresion">
      <Group className="no-imprimir barra" justify="space-between">
        <Button component={Link} to={`/documentos/${doc.id}`} variant="default" leftSection={<IconArrowLeft size={16} />}>
          Volver
        </Button>
        <Text size="sm" c="dimmed">
          Para el PDF: destino «Guardar como PDF», márgenes «Predeterminados» y sin «Encabezados y pies de página».
        </Text>
        <Button leftSection={<IconPrinter size={16} />} onClick={() => window.print()}>
          Imprimir / Guardar PDF
        </Button>
      </Group>

      <article className="hoja" ref={hojaRef}>
        <header className="cabecera-pagina" ref={cabeceraRef}>
          <div className="empresa">
            {e.logoUrl && <img src={e.logoUrl} alt="" className="logo" />}
            <div>
              <div className="empresa-nombre">{e.nombre}</div>
              <div>{[e.titular, e.nif && `NIF: ${e.nif}`].filter(Boolean).join(' · ')}</div>
              {e.direccion && <div>{e.direccion}</div>}
              {e.telefonos && <div>{e.telefonos}</div>}
            </div>
          </div>
          <div className="documento">
            <div className="documento-tipo">{esFactura ? 'FACTURA' : 'PRESUPUESTO'}</div>
            <table>
              <tbody>
                <tr>
                  <th>Nº</th>
                  <td>{doc.codigo}</td>
                </tr>
                <tr>
                  <th>Fecha</th>
                  <td>{formatoFecha(doc.fecha)}</td>
                </tr>
              </tbody>
            </table>
          </div>
        </header>

        <table className="maqueta">
          <thead>
            <tr>
              <td>
                <div className="espacio-cabecera" />
              </td>
            </tr>
          </thead>
          <tfoot>
            <tr>
              <td>
                <div className="espacio-pie" />
              </td>
            </tr>
          </tfoot>
          <tbody>
            <tr>
              <td>
                {c && (
                  <section className="cliente">
                    <div className="titulo-caja">{esFactura ? 'Facturar a' : 'Cliente'}</div>
                    <div className="cliente-nombre">
                      {c.nombre} {c.apellidos}
                    </div>
                    {c.empresa && <div>{c.empresa}</div>}
                    {c.dni && <div>DNI/NIF: {c.dni}</div>}
                    {c.direccion && <div>{c.direccion}</div>}
                    {(c.codigoPostal || c.ciudad || c.provincia) && (
                      <div>{[[c.codigoPostal, c.ciudad].filter(Boolean).join(' '), c.provincia].filter(Boolean).join(' · ')}</div>
                    )}
                    {c.telefonos[0] && <div>Tel.: {c.telefonos.join(', ')}</div>}
                  </section>
                )}

                {doc.textoConcepto && <section className="concepto">{doc.textoConcepto}</section>}

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
                    {lineas.map((l, i) => (
                      <tr key={i}>
                        <td className="cant">{l.cantidad.toLocaleString('es-ES')}</td>
                        <td>{l.nombreProducto}</td>
                        <td className="num">{formatoEuros(l.precio)}</td>
                        <td className="num">{formatoEuros(importeLinea(l))}</td>
                      </tr>
                    ))}
                    {otrosConceptos !== 0 && (
                      <tr>
                        <td className="cant">1</td>
                        <td>Desplazamiento, instalación y gestión</td>
                        <td className="num">{formatoEuros(otrosConceptos)}</td>
                        <td className="num">{formatoEuros(otrosConceptos)}</td>
                      </tr>
                    )}
                  </tbody>
                </table>

                <table className="totales">
                  <tbody>
                    <tr>
                      <th>Base imponible</th>
                      <td>{formatoEuros(doc.totalSinIva)}</td>
                    </tr>
                    <tr>
                      <th>IVA {doc.aplicaIva ? `${doc.ivaPorcentaje.toLocaleString('es-ES')} %` : '(exento)'}</th>
                      <td>{formatoEuros(doc.totalIva)}</td>
                    </tr>
                    <tr className="total">
                      <th>TOTAL</th>
                      <td>{formatoEuros(doc.totalConIva)}</td>
                    </tr>
                  </tbody>
                </table>

                {(doc.textoImportacion1 || doc.textoImportacion2) && (
                  <section className="importacion">
                    <div>{doc.textoImportacion1}</div>
                    <div>{doc.textoImportacion2}</div>
                  </section>
                )}

                {doc.textoExplicativo && (
                  <section className="bloque">
                    <div className="titulo-caja">Observaciones</div>
                    <div className="texto">{doc.textoExplicativo}</div>
                  </section>
                )}
                {doc.textoFormaPago && (
                  <section className="bloque">
                    <div className="titulo-caja">Forma de pago</div>
                    <div className="texto">{doc.textoFormaPago}</div>
                  </section>
                )}
                {e.cuentaBancaria && (
                  <section className="bloque">
                    <div className="titulo-caja">Datos bancarios</div>
                    <div className="texto">{e.cuentaBancaria}</div>
                  </section>
                )}
              </td>
            </tr>
          </tbody>
        </table>

        <footer className="pie-pagina" ref={pieRef}>
          {ajustes.textoCondiciones && <p className="condiciones">{ajustes.textoCondiciones}</p>}
          <p>{[e.web && `Web: ${e.web}`, e.emails && `E-mail: ${e.emails}`].filter(Boolean).join('  ·  ')}</p>
        </footer>
      </article>
    </div>
  );
}
