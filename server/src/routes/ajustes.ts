import type { FastifyInstance } from 'fastify';
import { PDF_POR_DEFECTO, ajustesSchema, type Ajustes } from '@cabp/shared';
import { consultar, ejecutar, type Db } from '../db/pool';
import { exigirAdmin } from '../auth/sesiones';

/** Valores iniciales, tomados de la plantilla de factura de la versión Java. */
export const AJUSTES_POR_DEFECTO: Ajustes = {
  ivaPorcentaje: 21,
  empresa: {
    nombre: 'C A B P - Empresa de servicios -',
    titular: 'Florencia Andréa Billordo Pérès Campagnale',
    nif: 'ES26266748N',
    direccion: 'San Telmo Nº 8 - Primer Piso, Arroyo de la Miel, Málaga 29631',
    telefonos: 'Teléfono: (34) 952-444-197 · Fax: (34) 952-444-197 · Móvil: (34) 669-093-494',
    web: 'www.cabp.es',
    emails: 'andres@cabp.es · andres444@hotmail.com',
    cuentaBancaria: 'Banco Popular · IBAN ES72 0075 0953 6407 0137 7061 · BIC POPUESMM',
    logoUrl: '/logo.png',
  },
  pdf: PDF_POR_DEFECTO,
  textoFormaPagoDefecto:
    'Existen dos formas de pago:\n' +
    'PEDIDO NORMAL: 40% al encargar la fabricación, 40% a los 30 días y 20% a la instalación. Plazo aproximado para la Península: 50-60 días.\n' +
    'PEDIDO URGENTE: 80% por adelantado y 20% a la instalación. Plazo aproximado para la Península: 25-30 días. Canarias: +10 días (plataformas).',
  textoCondiciones:
    'Una vez certificada la instalación se hace entrega del equipo. El equipo sigue siendo propiedad de la empresa hasta su pago total. ' +
    'El cliente debe facilitar una toma de corriente junto a la escalera/hueco del elevador.',
};

export async function leerAjustes(db: Db): Promise<Ajustes> {
  const [f] = await consultar(db, "SELECT valor FROM ajustes WHERE clave = 'general'");
  if (!f) return AJUSTES_POR_DEFECTO;
  const valor = typeof f.valor === 'string' ? JSON.parse(f.valor) : f.valor;
  return ajustesSchema.parse({
    ...AJUSTES_POR_DEFECTO,
    ...valor,
    empresa: { ...AJUSTES_POR_DEFECTO.empresa, ...valor.empresa },
    pdf: { ...PDF_POR_DEFECTO, ...valor.pdf },
  });
}

export async function rutasAjustes(app: FastifyInstance) {
  app.get('/ajustes', async () => leerAjustes(app.db));

  app.put('/ajustes', async (req) => {
    exigirAdmin(req);
    const a = ajustesSchema.parse(req.body);
    await ejecutar(
      app.db,
      "INSERT INTO ajustes (clave, valor) VALUES ('general', ?) ON DUPLICATE KEY UPDATE valor = VALUES(valor)",
      [JSON.stringify(a)],
    );
    return a;
  });
}
