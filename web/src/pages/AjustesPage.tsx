import { useEffect, useState } from 'react';
import { Button, Center, Group, Loader, Paper, SimpleGrid, Stack, Text, Textarea, TextInput, Title } from '@mantine/core';
import type { Ajustes } from '@cabp/shared';
import { notificarOk, useAjustes, useGuardarAjustes, useYo } from '../api/hooks';
import { Numero } from '../components/Numero';

export function AjustesPage() {
  const { data } = useAjustes();
  const { data: yo } = useYo();
  const guardar = useGuardarAjustes();
  const [a, setA] = useState<Ajustes | null>(null);
  useEffect(() => {
    if (data) setA(data);
  }, [data]);

  if (yo?.rol !== 'admin') return <Text>Solo los administradores pueden cambiar los ajustes.</Text>;
  if (!a)
    return (
      <Center p="xl">
        <Loader />
      </Center>
    );

  const emp = (k: keyof Ajustes['empresa'], label: string) => (
    <TextInput label={label} value={a.empresa[k]} onChange={(e) => setA({ ...a, empresa: { ...a.empresa, [k]: e.currentTarget.value } })} />
  );

  return (
    <form
      onSubmit={(e) => {
        e.preventDefault();
        guardar.mutate(a, { onSuccess: () => notificarOk('Ajustes guardados') });
      }}
    >
      <Stack>
        <Title order={2}>Ajustes</Title>
        <Paper withBorder p="md">
          <Title order={4} mb="sm">
            IVA
          </Title>
          <Numero
            w={200}
            label="IVA por defecto (%)"
            description="Se aplica a los documentos nuevos. Cada documento guarda el suyo."
            suffix=" %"
            value={a.ivaPorcentaje}
            onChange={(n) => setA({ ...a, ivaPorcentaje: n })}
          />
        </Paper>
        <Paper withBorder p="md">
          <Title order={4} mb="sm">
            Datos de la empresa (cabecera de presupuestos y facturas)
          </Title>
          <SimpleGrid cols={{ base: 1, sm: 2 }}>
            {emp('nombre', 'Nombre comercial')}
            {emp('titular', 'Titular')}
            {emp('nif', 'NIF')}
            {emp('direccion', 'Dirección')}
            {emp('telefonos', 'Teléfonos')}
            {emp('web', 'Web')}
            {emp('emails', 'Emails')}
            {emp('logoUrl', 'URL del logo')}
          </SimpleGrid>
          <Textarea
            mt="sm"
            label="Datos bancarios"
            autosize
            minRows={2}
            value={a.empresa.cuentaBancaria}
            onChange={(e) => setA({ ...a, empresa: { ...a.empresa, cuentaBancaria: e.currentTarget.value } })}
          />
        </Paper>
        <Paper withBorder p="md">
          <Title order={4} mb="sm">
            Textos
          </Title>
          <Stack>
            <Textarea
              label="Forma de pago por defecto"
              description="Se copia en cada documento nuevo y se puede cambiar en él."
              autosize
              minRows={3}
              value={a.textoFormaPagoDefecto}
              onChange={(e) => setA({ ...a, textoFormaPagoDefecto: e.currentTarget.value })}
            />
            <Textarea
              label="Condiciones (pie de página)"
              autosize
              minRows={3}
              value={a.textoCondiciones}
              onChange={(e) => setA({ ...a, textoCondiciones: e.currentTarget.value })}
            />
          </Stack>
        </Paper>
        <Group justify="flex-end">
          <Button type="submit" loading={guardar.isPending}>
            Guardar ajustes
          </Button>
        </Group>
      </Stack>
    </form>
  );
}
