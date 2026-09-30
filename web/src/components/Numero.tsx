import { NumberInput, type NumberInputProps } from '@mantine/core';

/** Campo numérico con formato español (1.234,56). */
export function Numero({
  value,
  onChange,
  euros,
  ...props
}: Omit<NumberInputProps, 'value' | 'onChange'> & { value: number; onChange: (n: number) => void; euros?: boolean }) {
  return (
    <NumberInput
      decimalSeparator=","
      thousandSeparator="."
      decimalScale={2}
      hideControls
      suffix={euros ? ' €' : undefined}
      value={value}
      onChange={(v) => onChange(typeof v === 'number' ? v : Number(String(v).replace(',', '.')) || 0)}
      {...props}
    />
  );
}
