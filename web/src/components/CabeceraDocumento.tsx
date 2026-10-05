import { forwardRef } from 'react';
import type { Ajustes } from '@cabp/shared';

/** Cabecera de la hoja impresa: empresa + tipo, número y fecha. La usan la impresión y la vista previa de Ajustes. */
export const CabeceraDocumento = forwardRef<
  HTMLElement,
  { empresa: Ajustes['empresa']; tipo: string; codigo: string; fecha: string }
>(function CabeceraDocumento({ empresa: e, tipo, codigo, fecha }, ref) {
  return (
    <header className="cabecera-pagina" ref={ref}>
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
