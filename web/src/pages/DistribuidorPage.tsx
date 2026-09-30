import { useState } from 'react';
import { ActionIcon, Button, Center, Group, Loader, Paper, Table, Text, TextInput, Title } from '@mantine/core';
import { IconCheck, IconPencil, IconPlus, IconTrash, IconX } from '@tabler/icons-react';
import { useNavigate, useParams } from 'react-router-dom';
import { formatoEuros, type Producto } from '@cabp/shared';
import {
  notificarOk,
  useBorrarDistribuidor,
  useBorrarProducto,
  useDistribuidores,
  useGuardarProducto,
  useProductos,
} from '../api/hooks';
import { DistribuidorForm } from '../components/DistribuidorForm';
import { Numero } from '../components/Numero';
import { confirmar } from '../components/confirmar';

function FilaEditable({
  inicial,
  onGuardar,
  onCancelar,
  guardando,
}: {
  inicial: { nombre: string; precio: number };
  onGuardar: (d: { nombre: string; precio: number }) => void;
  onCancelar: () => void;
  guardando: boolean;
}) {
  const [d, setD] = useState(inicial);
  return (
    <Table.Tr>
      <Table.Td>
        <TextInput
          value={d.nombre}
          placeholder="Nombre del producto"
          autoFocus
          onChange={(e) => setD({ ...d, nombre: e.currentTarget.value })}
          onKeyDown={(e) => e.key === 'Enter' && d.nombre.trim() && onGuardar(d)}
        />
      </Table.Td>
      <Table.Td>
        <Numero euros value={d.precio} onChange={(precio) => setD({ ...d, precio })} />
      </Table.Td>
      <Table.Td>
        <Group gap={2} wrap="nowrap" justify="flex-end">
          <ActionIcon color="green" variant="subtle" loading={guardando} disabled={!d.nombre.trim()} onClick={() => onGuardar(d)} aria-label="Guardar">
            <IconCheck size={16} />
          </ActionIcon>
          <ActionIcon variant="subtle" onClick={onCancelar} aria-label="Cancelar">
            <IconX size={16} />
          </ActionIcon>
        </Group>
      </Table.Td>
    </Table.Tr>
  );
}

export function DistribuidorPage() {
  const id = Number(useParams().id);
  const { data: lista, isLoading } = useDistribuidores();
  const { data: productos = [] } = useProductos(id);
  const guardarProd = useGuardarProducto(id);
  const borrarProd = useBorrarProducto(id);
  const borrarDist = useBorrarDistribuidor();
  const [editando, setEditando] = useState<number | 'nuevo' | null>(null);
  const navigate = useNavigate();
  const dist = lista?.find((d) => d.id === id);

  if (isLoading)
    return (
      <Center p="xl">
        <Loader />
      </Center>
    );
  if (!dist) return <Text>Distribuidor no encontrado.</Text>;

  const guardar = (pid: number | undefined) => (datos: { nombre: string; precio: number }) =>
    guardarProd.mutate({ id: pid, datos }, { onSuccess: () => setEditando(null) });

  const pedirBorradoProd = (p: Producto) =>
    confirmar('Borrar producto', `¿Borrar «${p.nombre}»? Los documentos que ya lo usan no cambian.`, () => borrarProd.mutate(p.id));

  const pedirBorradoDist = () =>
    confirmar(
      'Borrar distribuidor',
      `¿Borrar ${dist.nombre} y todos sus productos? Los documentos existentes conservan sus líneas.`,
      () =>
        borrarDist.mutate(dist.id, {
          onSuccess: () => {
            notificarOk('Distribuidor borrado');
            navigate('/distribuidores');
          },
        }),
    );

  return (
    <>
      <Group justify="space-between" mb="md">
        <Title order={2}>{dist.nombre}</Title>
        <Button variant="subtle" color="red" leftSection={<IconTrash size={16} />} onClick={pedirBorradoDist}>
          Borrar distribuidor
        </Button>
      </Group>
      <Paper withBorder p="md" mb="md">
        <DistribuidorForm key={JSON.stringify(dist)} distribuidor={dist} onGuardado={() => notificarOk('Distribuidor guardado')} />
      </Paper>
      <Paper withBorder p="md">
        <Group justify="space-between" mb="sm">
          <Title order={4}>Productos</Title>
          <Button size="xs" leftSection={<IconPlus size={14} />} onClick={() => setEditando('nuevo')} disabled={editando === 'nuevo'}>
            Nuevo producto
          </Button>
        </Group>
        <Table highlightOnHover>
          <Table.Thead>
            <Table.Tr>
              <Table.Th>Nombre</Table.Th>
              <Table.Th w={160} className="importe">
                Precio
              </Table.Th>
              <Table.Th w={90} />
            </Table.Tr>
          </Table.Thead>
          <Table.Tbody>
            {editando === 'nuevo' && (
              <FilaEditable inicial={{ nombre: '', precio: 0 }} onGuardar={guardar(undefined)} onCancelar={() => setEditando(null)} guardando={guardarProd.isPending} />
            )}
            {productos.map((p) =>
              editando === p.id ? (
                <FilaEditable key={p.id} inicial={p} onGuardar={guardar(p.id)} onCancelar={() => setEditando(null)} guardando={guardarProd.isPending} />
              ) : (
                <Table.Tr key={p.id}>
                  <Table.Td>{p.nombre}</Table.Td>
                  <Table.Td className="importe">{formatoEuros(p.precio)}</Table.Td>
                  <Table.Td>
                    <Group gap={2} wrap="nowrap" justify="flex-end">
                      <ActionIcon variant="subtle" onClick={() => setEditando(p.id)} aria-label="Editar">
                        <IconPencil size={16} />
                      </ActionIcon>
                      <ActionIcon variant="subtle" color="red" onClick={() => pedirBorradoProd(p)} aria-label="Borrar">
                        <IconTrash size={16} />
                      </ActionIcon>
                    </Group>
                  </Table.Td>
                </Table.Tr>
              ),
            )}
          </Table.Tbody>
        </Table>
        {!productos.length && editando !== 'nuevo' && (
          <Text c="dimmed" ta="center" p="md">
            Este distribuidor no tiene productos.
          </Text>
        )}
      </Paper>
    </>
  );
}
