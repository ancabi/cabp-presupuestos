export class ApiError extends Error {
  constructor(
    public status: number,
    message: string,
    /** Campo del formulario al que se refiere el error (si el servidor lo indica). */
    public campo?: string,
  ) {
    super(message);
  }
}

async function pedir<T>(method: string, url: string, body?: unknown): Promise<T> {
  const esForm = body instanceof FormData;
  const r = await fetch(`/api${url}`, {
    method,
    credentials: 'same-origin',
    headers: {
      'X-Requested-With': 'cabp',
      ...(body !== undefined && !esForm ? { 'Content-Type': 'application/json' } : {}),
    },
    body: body === undefined ? undefined : esForm ? body : JSON.stringify(body),
  });
  const texto = await r.text();
  const datos = texto ? JSON.parse(texto) : null;
  if (!r.ok) {
    if (r.status === 401 && !url.startsWith('/auth/')) window.dispatchEvent(new Event('cabp:no-autenticado'));
    throw new ApiError(r.status, datos?.error ?? `Error ${r.status}`, datos?.campo);
  }
  return datos as T;
}

export const http = {
  get: <T>(url: string) => pedir<T>('GET', url),
  post: <T>(url: string, body: unknown = {}) => pedir<T>('POST', url, body),
  put: <T>(url: string, body: unknown) => pedir<T>('PUT', url, body),
  patch: <T>(url: string, body: unknown) => pedir<T>('PATCH', url, body),
  del: <T = { ok: true }>(url: string) => pedir<T>('DELETE', url),
};
