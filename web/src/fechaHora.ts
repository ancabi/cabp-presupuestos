/** Fecha y hora guardadas en UTC por la base de datos ("2026-10-05 09:37:46"), en hora local. */
export const fechaHora = (s: string) =>
  new Date(s.replace(' ', 'T') + 'Z').toLocaleString('es-ES', { dateStyle: 'short', timeStyle: 'short' });
