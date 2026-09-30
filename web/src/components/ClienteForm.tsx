import { useState } from 'react';
import { Button, Grid, Group, TagsInput, Textarea, TextInput } from '@mantine/core';
import type { Cliente, ClienteInput } from '@cabp/shared';
import { useGuardarCliente } from '../api/hooks';

const vacio: ClienteInput = {
  dni: '',
  nombre: '',
  apellidos: '',
  direccion: '',
  ciudad: '',
  provincia: '',
  empresa: '',
  notas: '',
  telefonos: [],
  emails: [],
};

export function ClienteForm({ cliente, onGuardado }: { cliente?: Cliente; onGuardado?: (c: Cliente) => void }) {
  const [d, setD] = useState<ClienteInput>(() => {
    if (!cliente) return vacio;
    const { id: _id, ...resto } = cliente;
    return resto;
  });
  const guardar = useGuardarCliente();
  const campo = (k: keyof ClienteInput) => ({
    value: d[k] as string,
    onChange: (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => setD({ ...d, [k]: e.currentTarget.value }),
  });

  const enviar = (e: React.FormEvent) => {
    e.preventDefault();
    guardar.mutate({ id: cliente?.id, datos: d }, { onSuccess: (c) => onGuardado?.(c) });
  };

  return (
    <form onSubmit={enviar}>
      <Grid>
        <Grid.Col span={{ base: 12, sm: 4 }}>
          <TextInput label="Nombre" required {...campo('nombre')} data-autofocus />
        </Grid.Col>
        <Grid.Col span={{ base: 12, sm: 5 }}>
          <TextInput label="Apellidos" {...campo('apellidos')} />
        </Grid.Col>
        <Grid.Col span={{ base: 12, sm: 3 }}>
          <TextInput label="DNI / NIF" {...campo('dni')} />
        </Grid.Col>
        <Grid.Col span={{ base: 12, sm: 6 }}>
          <TextInput label="Dirección" {...campo('direccion')} />
        </Grid.Col>
        <Grid.Col span={{ base: 12, sm: 3 }}>
          <TextInput label="Ciudad" {...campo('ciudad')} />
        </Grid.Col>
        <Grid.Col span={{ base: 12, sm: 3 }}>
          <TextInput label="Provincia" {...campo('provincia')} />
        </Grid.Col>
        <Grid.Col span={{ base: 12, sm: 4 }}>
          <TextInput label="Empresa" {...campo('empresa')} />
        </Grid.Col>
        <Grid.Col span={{ base: 12, sm: 4 }}>
          <TagsInput
            label="Teléfonos"
            description="Escribe y pulsa Enter"
            value={d.telefonos}
            onChange={(v) => setD({ ...d, telefonos: v.map((t) => t.trim()) })}
          />
        </Grid.Col>
        <Grid.Col span={{ base: 12, sm: 4 }}>
          <TagsInput
            label="Emails"
            description="Escribe y pulsa Enter"
            value={d.emails}
            onChange={(v) => setD({ ...d, emails: v.map((t) => t.trim()) })}
          />
        </Grid.Col>
        <Grid.Col span={12}>
          <Textarea label="Notas" autosize minRows={3} {...campo('notas')} />
        </Grid.Col>
      </Grid>
      <Group justify="flex-end" mt="md">
        <Button type="submit" loading={guardar.isPending}>
          Guardar
        </Button>
      </Group>
    </form>
  );
}
