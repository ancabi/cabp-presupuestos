import { z } from 'zod';

const texto = (max = 255) => z.string().trim().max(max).default('');
const importe = z.coerce.number().finite().min(0).max(9_999_999_999).default(0);
/** Importes de documentos: pueden ser negativos en facturas de devolución (abonos). */
const importeConSigno = z.coerce.number().finite().min(-9_999_999_999).max(9_999_999_999).default(0);
const fechaIso = z.string().regex(/^\d{4}-\d{2}-\d{2}$/, 'Fecha con formato AAAA-MM-DD');

export const loginSchema = z.object({
  email: z.string().trim().toLowerCase().email(),
  password: z.string().min(1),
});

export const clienteSchema = z.object({
  dni: texto(20),
  nombre: z.string().trim().min(1, 'El nombre es obligatorio').max(100),
  apellidos: texto(150),
  direccion: texto(),
  codigoPostal: z
    .string()
    .trim()
    .max(10)
    .regex(/^[0-9A-Za-z -]*$/, 'Código postal no válido')
    .default(''),
  ciudad: texto(100),
  provincia: texto(100),
  empresa: texto(150),
  notas: z.string().max(10000).default(''),
  telefonos: z
    .array(z.string().trim().regex(/^\+?[0-9 ]{3,20}$/, 'Teléfono no válido'))
    .default([])
    .transform((l) => [...new Set(l)]),
  emails: z
    .array(z.string().trim().toLowerCase().email('Email no válido'))
    .default([])
    .transform((l) => [...new Set(l)]),
});
export type ClienteInput = z.infer<typeof clienteSchema>;

export const distribuidorSchema = z.object({
  nombre: z.string().trim().min(1, 'El nombre es obligatorio').max(150),
  direccion: texto(),
  email: z.union([z.literal(''), z.string().trim().email('Email no válido')]).default(''),
  telefono: texto(30),
  ciudad: texto(100),
  provincia: texto(100),
  pais: texto(100),
  numeroCta: texto(50),
  iban: texto(50),
  swift: texto(20),
});
export type DistribuidorInput = z.infer<typeof distribuidorSchema>;

export const productoSchema = z.object({
  nombre: z.string().trim().min(1, 'El nombre es obligatorio').max(255),
  precio: importe,
});
export type ProductoInput = z.infer<typeof productoSchema>;

export const lineaSchema = z.object({
  productoId: z.number().int().positive().nullable().default(null),
  nombreProducto: z.string().trim().min(1).max(255),
  cantidad: z.coerce.number().finite().min(-1_000_000).max(1_000_000),
  precio: z.coerce.number().finite().min(-9_999_999_999).max(9_999_999_999),
});
export type LineaInput = z.infer<typeof lineaSchema>;

export const documentoSchema = z.object({
  tipo: z.enum(['presupuesto', 'factura']),
  fecha: fechaIso,
  clienteId: z.number().int().positive(),
  distribuidorId: z.number().int().positive().nullable().default(null),
  lineas: z.array(lineaSchema).default([]),
  ganancia: importeConSigno,
  restaurante: importeConSigno,
  pasaje: importeConSigno,
  combustible: importeConSigno,
  otros: importeConSigno,
  hotel: importeConSigno,
  transporte: importeConSigno,
  kilometros: importe,
  numViajes: z.coerce.number().int().min(0).max(1000).default(2),
  precioGasolina: importe,
  aplicaGanancia: z.boolean().default(true),
  aplicaIva: z.boolean().default(true),
  ivaPorcentaje: z.coerce.number().min(0).max(100),
  totalManualActivo: z.boolean().default(false),
  totalManual: importeConSigno,
  porcentajeReparto: z.coerce.number().int().min(0).max(100).default(50),
  textoConcepto: z.string().max(10000).default(''),
  textoFormaPago: z.string().max(10000).default(''),
  textoExplicativo: z.string().max(10000).default(''),
  textoImportacion1: z.string().max(10000).default(''),
  textoImportacion2: z.string().max(10000).default(''),
  calcTipo: z.enum(['pitagoras', 'stepper']).default('pitagoras'),
  valorA: z.coerce.number().finite().default(0),
  valorB: z.coerce.number().finite().default(0),
  valorC: z.coerce.number().finite().default(0),
  valorAux: z.coerce.number().finite().default(0),
});
export type DocumentoInput = z.infer<typeof documentoSchema>;

export const empresaSchema = z.object({
  nombre: texto(),
  titular: texto(),
  nif: texto(30),
  direccion: texto(),
  telefonos: texto(),
  web: texto(),
  emails: texto(),
  cuentaBancaria: z.string().max(2000).default(''),
  logoUrl: texto(1000),
});

export const ajustesSchema = z.object({
  ivaPorcentaje: z.coerce.number().min(0).max(100),
  empresa: empresaSchema,
  textoFormaPagoDefecto: z.string().max(10000).default(''),
  textoCondiciones: z.string().max(10000).default(''),
});
export type Ajustes = z.infer<typeof ajustesSchema>;

export const usuarioCrearSchema = z.object({
  email: z.string().trim().toLowerCase().email(),
  nombre: z.string().trim().min(1).max(100),
  password: z.string().min(8, 'Mínimo 8 caracteres').max(200),
  rol: z.enum(['admin', 'usuario']).default('usuario'),
});
export const usuarioEditarSchema = z.object({
  nombre: z.string().trim().min(1).max(100),
  rol: z.enum(['admin', 'usuario']),
  activo: z.boolean(),
  password: z.union([z.literal(''), z.string().min(8, 'Mínimo 8 caracteres').max(200)]).default(''),
});
export const cambiarPasswordSchema = z.object({
  actual: z.string().min(1),
  nueva: z.string().min(8, 'Mínimo 8 caracteres').max(200),
});
