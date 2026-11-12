import { Fragment, forwardRef } from 'react';
import type { Ajustes } from '@cabp/shared';

/** Cabecera de la hoja impresa: empresa + tipo, número y fecha. La usan la impresión y la vista previa de Ajustes. */
export const CabeceraDocumento = forwardRef<
  HTMLElement,
  { empresa: Ajustes['empresa']; tipo: string; codigo: string; fecha: string }
>(function CabeceraDocumento({ empresa: e, tipo, codigo, fecha }, ref) {
  // Datos fiscales, en este orden; cada uno se parte de línea entero, nunca por la mitad.
  const fiscales = [
    e.nif && `DNI: ${e.nif}`,
    e.exportadorRegistrado && `Nº Exportador Registrado: ${e.exportadorRegistrado}`,
    e.eori && `EORI: ${e.eori}`,
    e.ivaIntracomunitario && `IVA Intracomunitario: ${e.ivaIntracomunitario}`,
  ].filter((x): x is string => !!x);
  return (
    <header className="cabecera-pagina" ref={ref}>
      <div className="empresa">
        {e.logoUrl && <img src={e.logoUrl} alt="" className="logo" />}
        <div>
          <div className="empresa-nombre">{e.nombre}</div>
          {e.titular && <div>{e.titular}</div>}
          {e.direccion && <div>{e.direccion}</div>}
          {e.telefonos && <div>{e.telefonos}</div>}
          {fiscales.length > 0 && (
            <div className="empresa-fiscal">
              {fiscales.map((f, i) => (
                <Fragment key={f}>
                  {i > 0 && ' · '}
                  <span>{f}</span>
                </Fragment>
              ))}
            </div>
          )}
        </div>
      </div>
      <div className="documento">
        <div className="documento-tipo">{tipo}</div>
        <table>
          <tbody>
            <tr>
              <th>Nº</th>
              <td>{codigo}</td>
            </tr>
            <tr>
              <th>Fecha</th>
              <td>{fecha}</td>
            </tr>
          </tbody>
        </table>
      </div>
    </header>
  );
});
