import { useState } from 'react';
import { Badge, Button, Group, Paper, Stack, Text, TextInput, Title } from '@mantine/core';
import { IconPlus } from '@tabler/icons-react';
import type { TipoFichero } from '@cabp/shared';
import { notificarOk, useGuardarTipoFichero, useTiposFichero } from '../api/hooks';

function FilaTipo({ tipo }: { tipo: TipoFichero }) {
  const [nombre, setNombre] = useState(tipo.nombre);
  const guardar = useGuardarTipoFichero();
  const cambiado = nombre.trim() !== '' && nombre.trim() !== tipo.nombre;
  return (
    <form
      onSubmit={(e) => {
        e.preventDefault();
        if (cambiado) guardar.mutate({ id: tipo.id, nombre: nombre.trim() }, { onSuccess: () => notificarOk('Tipo renombrado') });
      }}
    >
      <Group gap="xs" wrap="nowrap">
        <TextInput
          aria-label={`Nombre del tipo ${tipo.nombre}`}
          maxLength={100}
          style={{ flex: 1 }}
          value={nombre}
          onChange={(e) => setNombre(e.currentTarget.value)}
          rightSectionWidth={110}
          rightSection={
            tipo.sistema && (
              <Badge size="xs" color="grape" variant="light">
                Factura
              </Badge>
            )
          }
        />
        {cambiado && (
          <>
            <Button type="submit" size="sm" loading={guardar.isPending}>
              Guardar
            </Button>
            <Button size="sm" variant="subtle" onClick={() => setNombre(tipo.nombre)}>
              Deshacer
            </Button>
          </>
        )}
      </Group>
    </form>
  );
}

/** Tipos de los ficheros de pedidos: se añaden y renombran; no se borran para no dejar ficheros sin tipo. */
export function TiposFicheroAjustes() {
  const { data: tipos = [] } = useTiposFichero();
  const guardar = useGuardarTipoFichero();
  const [nuevo, setNuevo] = useState('');

  return (
    <Paper withBorder p="md">
      <Title order={4}>Tipos de fichero de pedidos</Title>
      <Text size="sm" c="dimmed" mb="sm">
        Los tipos no se pueden borrar para no dejar ficheros sin tipo; sí se pueden renombrar. El marcado como «Factura» es el
        que se muestra en la fila de la factura de cada pedido.
      </Text>
      <Stack gap="xs" maw={560}>
        {tipos.map((t) => (
          <FilaTipo key={`${t.id}-${t.nombre}`} tipo={t} />
        ))}
        <form
          onSubmit={(e) => {
            e.preventDefault();
            if (nuevo.trim())
              guardar.mutate(
                { nombre: nuevo.trim() },
                {
                  onSuccess: () => {
                    notificarOk(`Tipo «${nuevo.trim()}» añadido`);
                    setNuevo('');
                  },
                },
              );
          }}
        >
          <Group gap="xs" wrap="nowrap" mt="xs">
            <TextInput
              placeholder="Nuevo tipo (p. ej. Documento aduana)"
              aria-label="Nuevo tipo"
              maxLength={100}
              style={{ flex: 1 }}
              value={nuevo}
              onChange={(e) => setNuevo(e.currentTarget.value)}
            />
            <Button type="submit" variant="light" leftSection={<IconPlus size={16} />} disabled={!nuevo.trim()} loading={guardar.isPending}>
              Añadir tipo
            </Button>
          </Group>
        </form>
      </Stack>
    </Paper>
  );
}
