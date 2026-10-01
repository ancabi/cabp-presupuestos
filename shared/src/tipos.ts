import type { Ajustes, ClienteInput, DistribuidorInput, DocumentoInput, LineaInput } from './schemas';
import type { TipoDocumento } from './formato';

export interface Usuario {
  id: number;
  email: string;
  nombre: string;
  rol: 'admin' | 'usuario';
  activo: boolean;
}

export interface Cliente extends ClienteInput {
  id: number;
}

export interface Distribuidor extends DistribuidorInput {
  id: number;
}

export interface Producto {
  id: number;
  distribuidorId: number;
  nombre: string;
  precio: number;
}

export interface Adjunto {
  id: number;
  clienteId: number;
  tipo: 'imagen' | 'pdf';
  nombreOriginal: string;
  mime: string;
  tamano: number;
  subidoEn: string;
}

export interface Linea extends LineaInput {
  id: number;
}

export interface Documento extends Omit<DocumentoInput, 'lineas'> {
  id: number;
  tipo: TipoDocumento;
  anio: number;
  numero: number;
  codigo: string;
  presupuestoOrigenId: number | null;
  facturaId: number | null;
  lineas: Linea[];
  totalSinIva: number;
  totalIva: number;
  totalConIva: number;
  cliente?: Pick<Cliente, 'id' | 'nombre' | 'apellidos' | 'dni' | 'direccion' | 'codigoPostal' | 'ciudad' | 'provincia' | 'telefonos' | 'emails' | 'empresa'>;
  distribuidor?: Pick<Distribuidor, 'id' | 'nombre'> | null;
}

export interface DocumentoResumen {
  id: number;
  tipo: TipoDocumento;
  anio: number;
  numero: number;
  codigo: string;
  fecha: string;
  clienteId: number;
  clienteNombre: string;
  distribuidorNombre: string | null;
  totalConIva: number;
  facturaId: number | null;
}

export type { Ajustes };
