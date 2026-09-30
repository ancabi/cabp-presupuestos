export class HttpError extends Error {
  constructor(
    public statusCode: number,
    message: string,
  ) {
    super(message);
  }
}

export const noEncontrado = (que = 'Recurso') => new HttpError(404, `${que} no encontrado`);
export const conflicto = (msg: string) => new HttpError(409, msg);
export const peticionIncorrecta = (msg: string) => new HttpError(400, msg);
