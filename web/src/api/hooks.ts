import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { notifications } from '@mantine/notifications';
import type {
  Adjunto,
  Ajustes,
  Cliente,
  ClienteInput,
  Distribuidor,
  DistribuidorInput,
  Documento,
  DocumentoInput,
  DocumentoResumen,
  Producto,
  ProductoInput,
  TipoDocumento,
  Usuario,
} from '@cabp/shared';
import { http } from './http';

export const notificarError = (e: unknown) =>
  notifications.show({ color: 'red', title: 'Error', message: e instanceof Error ? e.message : String(e) });
export const notificarOk = (message: string) => notifications.show({ color: 'green', message });

// --- Sesión ---
export const useYo = () =>
  useQuery({ queryKey: ['yo'], queryFn: () => http.get<Usuario>('/auth/me'), retry: false, staleTime: 60_000 });

// --- Clientes ---
export const useClientes = (q: string) =>
  useQuery({ queryKey: ['clientes', q], queryFn: () => http.get<Cliente[]>(`/clientes?q=${encodeURIComponent(q)}`) });
export const useCliente = (id: number | undefined) =>
  useQuery({ queryKey: ['cliente', id], queryFn: () => http.get<Cliente>(`/clientes/${id}`), enabled: !!id });

export function useGuardarCliente() {
  const qc = useQueryClient();
  return useMutation({
    mutationFn: ({ id, datos }: { id?: number; datos: ClienteInput }) =>
      id ? http.put<Cliente>(`/clientes/${id}`, datos) : http.post<Cliente>('/clientes', datos),
    onSuccess: (c) => {
      qc.invalidateQueries({ queryKey: ['clientes'] });
      qc.setQueryData(['cliente', c.id], c);
    },
    onError: notificarError,
  });
}
export function useBorrarCliente() {
  const qc = useQueryClient();
  return useMutation({
    mutationFn: (id: number) => http.del(`/clientes/${id}`),
    onSuccess: () => qc.invalidateQueries({ queryKey: ['clientes'] }),
    onError: notificarError,
  });
}

// --- Adjuntos ---
export const useAdjuntos = (clienteId: number) =>
  useQuery({ queryKey: ['adjuntos', clienteId], queryFn: () => http.get<Adjunto[]>(`/clientes/${clienteId}/adjuntos`) });
export function useSubirAdjuntos(clienteId: number) {
  const qc = useQueryClient();
  return useMutation({
    mutationFn: (ficheros: File[]) => {
      const fd = new FormData();
      ficheros.forEach((f) => fd.append('ficheros', f));
      return http.post<Adjunto[]>(`/clientes/${clienteId}/adjuntos`, fd);
    },
    onSuccess: () => qc.invalidateQueries({ queryKey: ['adjuntos', clienteId] }),
    onError: notificarError,
  });
}
export function useBorrarAdjunto(clienteId: number) {
  const qc = useQueryClient();
  return useMutation({
    mutationFn: (id: number) => http.del(`/adjuntos/${id}`),
    onSuccess: () => qc.invalidateQueries({ queryKey: ['adjuntos', clienteId] }),
    onError: notificarError,
  });
}

// --- Distribuidores y productos ---
export const useDistribuidores = () =>
  useQuery({ queryKey: ['distribuidores'], queryFn: () => http.get<Distribuidor[]>('/distribuidores') });
export function useGuardarDistribuidor() {
  const qc = useQueryClient();
  return useMutation({
    mutationFn: ({ id, datos }: { id?: number; datos: DistribuidorInput }) =>
      id ? http.put<Distribuidor>(`/distribuidores/${id}`, datos) : http.post<Distribuidor>('/distribuidores', datos),
    onSuccess: () => qc.invalidateQueries({ queryKey: ['distribuidores'] }),
    onError: notificarError,
  });
}
export function useBorrarDistribuidor() {
  const qc = useQueryClient();
  return useMutation({
    mutationFn: (id: number) => http.del(`/distribuidores/${id}`),
    onSuccess: () => qc.invalidateQueries({ queryKey: ['distribuidores'] }),
    onError: notificarError,
  });
}
export const useProductos = (distribuidorId: number | null | undefined) =>
  useQuery({
    queryKey: ['productos', distribuidorId],
    queryFn: () => http.get<Producto[]>(`/distribuidores/${distribuidorId}/productos`),
    enabled: !!distribuidorId,
  });
export function useGuardarProducto(distribuidorId: number) {
  const qc = useQueryClient();
  return useMutation({
    mutationFn: ({ id, datos }: { id?: number; datos: ProductoInput }) =>
      id ? http.put<Producto>(`/productos/${id}`, datos) : http.post<Producto>(`/distribuidores/${distribuidorId}/productos`, datos),
    onSuccess: () => qc.invalidateQueries({ queryKey: ['productos', distribuidorId] }),
    onError: notificarError,
  });
}
export function useBorrarProducto(distribuidorId: number) {
  const qc = useQueryClient();
  return useMutation({
    mutationFn: (id: number) => http.del(`/productos/${id}`),
    onSuccess: () => qc.invalidateQueries({ queryKey: ['productos', distribuidorId] }),
    onError: notificarError,
  });
}

// --- Documentos ---
export const useDocumentos = (f: { tipo?: TipoDocumento; anio?: number | null; clienteId?: number; q?: string }) => {
  const p = new URLSearchParams();
  if (f.tipo) p.set('tipo', f.tipo);
  if (f.anio) p.set('anio', String(f.anio));
  if (f.clienteId) p.set('clienteId', String(f.clienteId));
  if (f.q) p.set('q', f.q);
  return useQuery({ queryKey: ['documentos', f], queryFn: () => http.get<DocumentoResumen[]>(`/documentos?${p}`) });
};
export const useAnios = () => useQuery({ queryKey: ['anios'], queryFn: () => http.get<number[]>('/documentos/anios') });
export const useDocumento = (id: number | undefined) =>
  useQuery({ queryKey: ['documento', id], queryFn: () => http.get<Documento>(`/documentos/${id}`), enabled: !!id });
export const useProximoNumero = (tipo: TipoDocumento, anio: number, activo: boolean) =>
  useQuery({
    queryKey: ['proximo', tipo, anio],
    queryFn: () => http.get<{ codigo: string }>(`/documentos/proximo-numero?tipo=${tipo}&anio=${anio}`),
    enabled: activo,
  });

function useInvalidarDocumentos() {
  const qc = useQueryClient();
  return (d?: Documento) => {
    qc.invalidateQueries({ queryKey: ['documentos'] });
    qc.invalidateQueries({ queryKey: ['anios'] });
    qc.invalidateQueries({ queryKey: ['proximo'] });
    if (d) qc.setQueryData(['documento', d.id], d);
  };
}
export function useGuardarDocumento() {
  const invalidar = useInvalidarDocumentos();
  return useMutation({
    mutationFn: ({ id, datos }: { id?: number; datos: DocumentoInput }) =>
      id ? http.put<Documento>(`/documentos/${id}`, datos) : http.post<Documento>('/documentos', datos),
    onSuccess: invalidar,
    onError: notificarError,
  });
}
export function useBorrarDocumento() {
  const invalidar = useInvalidarDocumentos();
  return useMutation({
    mutationFn: (id: number) => http.del(`/documentos/${id}`),
    onSuccess: () => invalidar(),
    onError: notificarError,
  });
}
export function useConvertirAFactura() {
  const invalidar = useInvalidarDocumentos();
  const qc = useQueryClient();
  return useMutation({
    mutationFn: (id: number) => http.post<Documento>(`/documentos/${id}/convertir-a-factura`),
    onSuccess: (f) => {
      invalidar(f);
      if (f.presupuestoOrigenId) qc.invalidateQueries({ queryKey: ['documento', f.presupuestoOrigenId] });
    },
    onError: notificarError,
  });
}

// --- Ajustes y usuarios ---
export const useAjustes = () => useQuery({ queryKey: ['ajustes'], queryFn: () => http.get<Ajustes>('/ajustes') });
export function useGuardarAjustes() {
  const qc = useQueryClient();
  return useMutation({
    mutationFn: (a: Ajustes) => http.put<Ajustes>('/ajustes', a),
    onSuccess: (a) => qc.setQueryData(['ajustes'], a),
    onError: notificarError,
  });
}
export const useUsuarios = () => useQuery({ queryKey: ['usuarios'], queryFn: () => http.get<Usuario[]>('/usuarios') });
export function useGuardarUsuario() {
  const qc = useQueryClient();
  return useMutation({
    mutationFn: ({ id, datos }: { id?: number; datos: Record<string, unknown> }) =>
      id ? http.put<Usuario>(`/usuarios/${id}`, datos) : http.post<Usuario>('/usuarios', datos),
    onSuccess: () => qc.invalidateQueries({ queryKey: ['usuarios'] }),
    onError: notificarError,
  });
}
export function useBorrarUsuario() {
  const qc = useQueryClient();
  return useMutation({
    mutationFn: (id: number) => http.del(`/usuarios/${id}`),
    onSuccess: () => qc.invalidateQueries({ queryKey: ['usuarios'] }),
    onError: notificarError,
  });
}
