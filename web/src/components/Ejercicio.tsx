import { createContext, useContext, useState, type ReactNode } from 'react';
import { SegmentedControl } from '@mantine/core';

/**
 * Año (ejercicio) seleccionado en la cabecera y trimestre elegido en los listados;
 * filtran los listados de presupuestos, facturas y pedidos.
 */
const Ctx = createContext<{
  anio: number | null;
  setAnio: (a: number | null) => void;
  trimestre: number | null;
  setTrimestre: (t: number | null) => void;
}>({
  anio: null,
  setAnio: () => {},
  trimestre: null,
  setTrimestre: () => {},
});

const CLAVE = 'cabp.ejercicio';
const CLAVE_TRIMESTRE = 'cabp.trimestre';

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

function leerTrimestre(): number | null {
  try {
    const t = Number(localStorage.getItem(CLAVE_TRIMESTRE));
    if (t >= 1 && t <= 4) return t;
  } catch {
    /* sin almacenamiento */
  }
  return null;
}

function guardar(clave: string, valor: string) {
  try {
    localStorage.setItem(clave, valor);
  } catch {
    /* sin almacenamiento */
  }
}

export function EjercicioProvider({ children }: { children: ReactNode }) {
  const [anio, setAnioState] = useState<number | null>(leer);
  const [trimestre, setTrimestreState] = useState<number | null>(leerTrimestre);
  const setAnio = (a: number | null) => {
    setAnioState(a);
    guardar(CLAVE, a == null ? 'todos' : String(a));
  };
  const setTrimestre = (t: number | null) => {
    setTrimestreState(t);
    guardar(CLAVE_TRIMESTRE, t == null ? 'todos' : String(t));
  };
  return <Ctx.Provider value={{ anio, setAnio, trimestre, setTrimestre }}>{children}</Ctx.Provider>;
}

export const useEjercicio = () => useContext(Ctx);

/** Texto del periodo para los títulos: "2026", "1T 2026", "(todos los años)"… */
export function textoPeriodo(anio: number | null, trimestre: number | null) {
  const t = trimestre ? `${trimestre}T ` : '';
  return anio ? `${t}${anio}` : trimestre ? `${trimestre}T (todos los años)` : '(todos los años)';
}

/** Selector de trimestre para la parte superior de los listados. */
export function FiltroTrimestre() {
  const { trimestre, setTrimestre } = useEjercicio();
  return (
    <SegmentedControl
      aria-label="Trimestre"
      value={trimestre ? String(trimestre) : 'todos'}
      onChange={(v) => setTrimestre(v === 'todos' ? null : Number(v))}
      data={[
        { value: 'todos', label: 'Todo el año' },
        { value: '1', label: '1T' },
        { value: '2', label: '2T' },
        { value: '3', label: '3T' },
        { value: '4', label: '4T' },
      ]}
    />
  );
}
