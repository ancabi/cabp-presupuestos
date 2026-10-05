import { useMemo, useState } from 'react';
import { Paper, ScrollArea, Table, Text, TextInput, Title, UnstyledButton, Group } from '@mantine/core';
import { useDebouncedValue } from '@mantine/hooks';
import { IconArrowDown, IconArrowUp, IconSearch, IconSelector } from '@tabler/icons-react';
import { useNavigate } from 'react-router-dom';
import type { PedidoResumen } from '@cabp/shared';
import { usePedidos } from '../api/hooks';
import { useEjercicio } from '../components/Ejercicio';
import { fechaHora } from '../fechaHora';

type Campo = 'creadoEn' | 'actualizadoEn';

export function PedidosPage() {
  const { anio } = useEjercicio();
  const [q, setQ] = useState('');
  const [qd] = useDebouncedValue(q, 250);
  const { data = [], isLoading } = usePedidos({ anio, q: qd });
  const [orden, setOrden] = useState<{ campo: Campo; asc: boolean }>({ campo: 'actualizadoEn', asc: false });
  const navigate = useNavigate();

  // Las fechas vienen como "AAAA-MM-DD hh:mm:ss": se ordenan bien como texto.
  const filas = useMemo(
    () =>
      [...data].sort((a: PedidoResumen, b: PedidoResumen) => {
        const c = a[orden.campo].localeCompare(b[orden.campo]) || a.id - b.id;
        return orden.asc ? c : -c;
      }),
    [data, orden],
  );

  const cabeceraFecha = (campo: Campo, texto: string) => {
    const activo = orden.campo === campo;
    const Icono = !activo ? IconSelector : orden.asc ? IconArrowUp : IconArrowDown;
    return (
      <Table.Th aria-sort={activo ? (orden.asc ? 'ascending' : 'descending') : 'none'} style={{ whiteSpace: 'nowrap' }}>
        <UnstyledButton onClick={() => setOrden({ campo, asc: activo ? !orden.asc : false })} fw={700} fz="sm">
          <Group gap={4} wrap="nowrap">
            {texto}
            <Icono size={14} />
          </Group>
        </UnstyledButton>
      </Table.Th>
    );
  };

  return (
    <>
      <Title order={2} mb="md">
        Pedidos {anio ?? '(todos los años)'}
      </Title>
      <TextInput
        mb="md"
        leftSection={<IconSearch size={16} />}
        placeholder="Buscar por nombre del pedido o del cliente…"
        value={q}
        onChange={(e) => setQ(e.currentTarget.value)}
      />
      <Paper withBorder>
        <ScrollArea>
          <Table striped highlightOnHover miw={780}>
            <Table.Thead>
              <Table.Tr>
                <Table.Th>Pedido</Table.Th>
                <Table.Th>Cliente</Table.Th>
                <Table.Th>Factura</Table.Th>
                <Table.Th ta="right">Documentos</Table.Th>
                {cabeceraFecha('creadoEn', 'Creado')}
                {cabeceraFecha('actualizadoEn', 'Última modificación')}
              </Table.Tr>
            </Table.Thead>
            <Table.Tbody>
              {filas.map((p) => (
                <Table.Tr key={p.id} className="fila-clicable" onClick={() => navigate(`/pedidos/${p.id}`)}>
                  <Table.Td maw={420}>
                    <Text size="sm" lineClamp={2} title={p.nombre}>
                      {p.nombre}
                    </Text>
                  </Table.Td>
                  <Table.Td>{p.clienteNombre}</Table.Td>
                  <Table.Td ff="monospace" style={{ whiteSpace: 'nowrap' }}>
                    {p.facturaCodigo ?? <Text span c="dimmed" size="sm">Sin factura</Text>}
                  </Table.Td>
                  <Table.Td
                    ta="right"
                    style={{ fontVariantNumeric: 'tabular-nums' }}
                    title={[p.facturaId && 'Factura', p.numFicheros && `${p.numFicheros} fichero${p.numFicheros === 1 ? '' : 's'}`]
                      .filter(Boolean)
                      .join(' + ')}
                  >
                    {p.numFicheros + (p.facturaId ? 1 : 0)}
                  </Table.Td>
                  <Table.Td style={{ whiteSpace: 'nowrap' }}>{fechaHora(p.creadoEn)}</Table.Td>
                  <Table.Td style={{ whiteSpace: 'nowrap' }}>{fechaHora(p.actualizadoEn)}</Table.Td>
                </Table.Tr>
              ))}
            </Table.Tbody>
          </Table>
        </ScrollArea>
        {!isLoading && !filas.length && (
          <Text c="dimmed" ta="center" p="lg">
            {qd ? 'Ningún pedido coincide con la búsqueda.' : 'No hay pedidos. Se crean al hacer una factura.'}
          </Text>
        )}
      </Paper>
      {filas.length > 0 && (
        <Text ta="right" mt="sm" c="dimmed">
          {filas.length} pedido(s)
        </Text>
      )}
    </>
  );
}
