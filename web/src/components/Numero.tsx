import { useEffect, useState } from 'react';
import { NumberInput, type NumberInputProps } from '@mantine/core';

const aNumero = (v: string | number) => (typeof v === 'number' ? v : Number(String(v).replace(',', '.')) || 0);
/** El 0 se muestra vacío: así, al borrar un valor para escribir otro, no queda un 0 delante (20 → 020 → 200). */
const aTexto = (n: number): string | number => (n === 0 ? '' : n);

/** Campo numérico con formato español (1.234,56). Vacío equivale a 0. */
export function Numero({
  value,
  onChange,
  euros,
  ...props
}: Omit<NumberInputProps, 'value' | 'onChange'> & { value: number; onChange: (n: number) => void; euros?: boolean }) {
  // Se guarda lo escrito tal cual ("0,", "-", "") para no pisarlo mientras se teclea.
  const [texto, setTexto] = useState<string | number>(() => aTexto(value));
  useEffect(() => {
    if (aNumero(texto) !== value) setTexto(aTexto(value));
  }, [value]); // eslint-disable-line react-hooks/exhaustive-deps

  return (
    <NumberInput
      decimalSeparator=","
      thousandSeparator="."
      decimalScale={2}
      hideControls
      suffix={euros ? ' €' : undefined}
      value={texto}
      onChange={(v) => {
        setTexto(v);
        onChange(aNumero(v));
      }}
      {...props}
    />
  );
}
