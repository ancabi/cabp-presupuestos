import { useState } from 'react';
import { Button, Group, SimpleGrid, TextInput } from '@mantine/core';
import type { Distribuidor, DistribuidorInput } from '@cabp/shared';
import { useGuardarDistribuidor } from '../api/hooks';

const vacio: DistribuidorInput = {
  nombre: '',
  direccion: '',
  email: '',
  telefono: '',
  ciudad: '',
  provincia: '',
  pais: '',
  numeroCta: '',
  iban: '',
  swift: '',
};

const CAMPOS: [keyof DistribuidorInput, string][] = [
  ['nombre', 'Nombre'],
  ['telefono', 'Teléfono'],
  ['email', 'Email'],
  ['direccion', 'Dirección'],
  ['ciudad', 'Ciudad'],
  ['provincia', 'Provincia'],
  ['pais', 'País'],
  ['numeroCta', 'Número de cuenta'],
  ['iban', 'IBAN'],
  ['swift', 'SWIFT / BIC'],
];

export function DistribuidorForm({ distribuidor, onGuardado }: { distribuidor?: Distribuidor; onGuardado?: (d: Distribuidor) => void }) {
  const [d, setD] = useState<DistribuidorInput>(() => {
    if (!distribuidor) return vacio;
    const { id: _id, ...resto } = distribuidor;
    return resto;
  });
  const guardar = useGuardarDistribuidor();

  return (
    <form
      onSubmit={(e) => {
        e.preventDefault();
        guardar.mutate({ id: distribuidor?.id, datos: d }, { onSuccess: onGuardado });
      }}
    >
      <SimpleGrid cols={{ base: 1, sm: 2, md: 3 }}>
        {CAMPOS.map(([k, label]) => (
          <TextInput
            key={k}
            label={label}
            required={k === 'nombre'}
            type={k === 'email' ? 'email' : 'text'}
            value={d[k]}
            onChange={(e) => setD({ ...d, [k]: e.currentTarget.value })}
          />
        ))}
      </SimpleGrid>
      <Group justify="flex-end" mt="md">
        <Button type="submit" loading={guardar.isPending}>
          Guardar
        </Button>
      </Group>
    </form>
  );
}
