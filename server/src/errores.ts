export class HttpError extends Error {
  constructor(
    public statusCode: number,
    message: string,
    /** Campo del formulario al que se refiere el error, para mostrarlo junto a él. */
    public campo?: string,
  ) {
    super(message);
  }
}

export const noEncontrado = (que = 'Recurso') => new HttpError(404, `${que} no encontrado`);
export const conflicto = (msg: string, campo?: string) => new HttpError(409, msg, campo);
export const peticionIncorrecta = (msg: string, campo?: string) => new HttpError(400, msg, campo);
