import { Button, Group, Modal, Paper, ScrollArea, Table, Text, Title } from '@mantine/core';
import { useDisclosure } from '@mantine/hooks';
import { IconPlus } from '@tabler/icons-react';
import { useNavigate } from 'react-router-dom';
import { useDistribuidores } from '../api/hooks';
import { DistribuidorForm } from '../components/DistribuidorForm';

export function DistribuidoresPage() {
  const { data = [], isLoading } = useDistribuidores();
  const [nuevo, { open, close }] = useDisclosure();
  const navigate = useNavigate();

  return (
    <>
      <Group justify="space-between" mb="md">
        <Title order={2}>Distribuidores y productos</Title>
        <Button leftSection={<IconPlus size={16} />} onClick={open}>
          Nuevo distribuidor
        </Button>
      </Group>
      <Paper withBorder>
        <ScrollArea>
          <Table striped highlightOnHover miw={700}>
            <Table.Thead>
              <Table.Tr>
                <Table.Th>Nombre</Table.Th>
                <Table.Th>Teléfono</Table.Th>
                <Table.Th>Email</Table.Th>
                <Table.Th>Ciudad</Table.Th>
                <Table.Th>Provincia</Table.Th>
                <Table.Th>País</Table.Th>
              </Table.Tr>
            </Table.Thead>
            <Table.Tbody>
              {data.map((d) => (
                <Table.Tr key={d.id} className="fila-clicable" onClick={() => navigate(`/distribuidores/${d.id}`)}>
                  <Table.Td fw={500}>{d.nombre}</Table.Td>
                  <Table.Td>{d.telefono}</Table.Td>
                  <Table.Td>{d.email}</Table.Td>
                  <Table.Td>{d.ciudad}</Table.Td>
                  <Table.Td>{d.provincia}</Table.Td>
                  <Table.Td>{d.pais}</Table.Td>
                </Table.Tr>
              ))}
            </Table.Tbody>
          </Table>
        </ScrollArea>
        {!isLoading && !data.length && (
          <Text c="dimmed" ta="center" p="lg">
            No hay distribuidores.
          </Text>
        )}
      </Paper>
      <Modal opened={nuevo} onClose={close} title="Nuevo distribuidor" size="xl">
        <DistribuidorForm onGuardado={(d) => navigate(`/distribuidores/${d.id}`)} />
      </Modal>
    </>
  );
}
