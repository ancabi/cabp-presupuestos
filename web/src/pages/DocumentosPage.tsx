import { useState } from 'react';
import { Button, Group, Text, TextInput, Title } from '@mantine/core';
import { useDebouncedValue } from '@mantine/hooks';
import { IconPlus, IconSearch } from '@tabler/icons-react';
import { formatoEuros, type TipoDocumento } from '@cabp/shared';
import { useNavigate } from 'react-router-dom';
import { useDocumentos } from '../api/hooks';
import { useEjercicio } from '../components/Ejercicio';
import { DocumentosTabla } from '../components/DocumentosTabla';

export function DocumentosPage({ tipo }: { tipo: TipoDocumento }) {
  const { anio } = useEjercicio();
  const [q, setQ] = useState('');
  const [qd] = useDebouncedValue(q, 250);
  const { data = [], isLoading } = useDocumentos({ tipo, anio, q: qd });
  const navigate = useNavigate();
  const titulo = tipo === 'presupuesto' ? 'Presupuestos' : 'Facturas';
  const total = data.reduce((a, d) => a + d.totalConIva, 0);

  return (
    <>
      <Group justify="space-between" mb="md">
        <Title order={2}>
          {titulo} {anio ?? '(todos los años)'}
        </Title>
        <Button leftSection={<IconPlus size={16} />} onClick={() => navigate(`/documentos/nuevo?tipo=${tipo}`)}>
          {tipo === 'presupuesto' ? 'Nuevo presupuesto' : 'Nueva factura'}
        </Button>
      </Group>
      <TextInput
        mb="md"
        leftSection={<IconSearch size={16} />}
        placeholder="Buscar por cliente, DNI, concepto o número…"
        value={q}
        onChange={(e) => setQ(e.currentTarget.value)}
      />
      <DocumentosTabla datos={data} cargando={isLoading} />
      {data.length > 0 && (
        <Text ta="right" mt="sm" c="dimmed">
          {data.length} documento(s) · Total {formatoEuros(total)}
        </Text>
      )}
    </>
  );
}
