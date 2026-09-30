import { createContext, useContext, useState, type ReactNode } from 'react';

/** Año (ejercicio) seleccionado en la cabecera; filtra los listados de presupuestos y facturas. */
const Ctx = createContext<{ anio: number | null; setAnio: (a: number | null) => void }>({
  anio: null,
  setAnio: () => {},
});

const CLAVE = 'cabp.ejercicio';

function leer(): number | null {
  try {
    const v = localStorage.getItem(CLAVE);
    if (v === 'todos') return null;
    if (v) return Number(v);
  } catch {
    /* sin almacenamiento */
  }
  return new Date().getFullYear();
}

export function EjercicioProvider({ children }: { children: ReactNode }) {
  const [anio, setAnioState] = useState<number | null>(leer);
  const setAnio = (a: number | null) => {
    setAnioState(a);
    try {
      localStorage.setItem(CLAVE, a == null ? 'todos' : String(a));
    } catch {
      /* sin almacenamiento */
    }
  };
  return <Ctx.Provider value={{ anio, setAnio }}>{children}</Ctx.Provider>;
}

export const useEjercicio = () => useContext(Ctx);
