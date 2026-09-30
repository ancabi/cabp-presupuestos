import { useState } from 'react';
import { Button, Group, Modal, Paper, ScrollArea, Table, Text, TextInput, Title } from '@mantine/core';
import { useDebouncedValue, useDisclosure } from '@mantine/hooks';
import { IconPlus, IconSearch } from '@tabler/icons-react';
import { useNavigate } from 'react-router-dom';
import { useClientes } from '../api/hooks';
import { ClienteForm } from '../components/ClienteForm';

export function ClientesPage() {
  const [q, setQ] = useState('');
  const [qd] = useDebouncedValue(q, 250);
  const { data = [], isLoading } = useClientes(qd);
  const [nuevo, { open, close }] = useDisclosure();
  const navigate = useNavigate();

  return (
    <>
      <Group justify="space-between" mb="md">
        <Title order={2}>Clientes</Title>
        <Button leftSection={<IconPlus size={16} />} onClick={open}>
          Nuevo cliente
        </Button>
      </Group>
      <TextInput
        mb="md"
        leftSection={<IconSearch size={16} />}
        placeholder="Buscar por nombre, DNI, ciudad, teléfono, email…"
        value={q}
        onChange={(e) => setQ(e.currentTarget.value)}
      />
      <Paper withBorder>
        <ScrollArea>
          <Table striped highlightOnHover miw={800}>
            <Table.Thead>
              <Table.Tr>
                <Table.Th>Nombre</Table.Th>
                <Table.Th>DNI</Table.Th>
                <Table.Th>Teléfono</Table.Th>
                <Table.Th>Email</Table.Th>
                <Table.Th>Ciudad</Table.Th>
                <Table.Th>Provincia</Table.Th>
                <Table.Th>Empresa</Table.Th>
              </Table.Tr>
            </Table.Thead>
            <Table.Tbody>
              {data.map((c) => (
                <Table.Tr key={c.id} className="fila-clicable" onClick={() => navigate(`/clientes/${c.id}`)}>
                  <Table.Td fw={500}>
                    {c.nombre} {c.apellidos}
                  </Table.Td>
                  <Table.Td>{c.dni}</Table.Td>
                  <Table.Td>{c.telefonos[0]}</Table.Td>
                  <Table.Td>{c.emails[0]}</Table.Td>
                  <Table.Td>{c.ciudad}</Table.Td>
                  <Table.Td>{c.provincia}</Table.Td>
                  <Table.Td>{c.empresa}</Table.Td>
                </Table.Tr>
              ))}
            </Table.Tbody>
          </Table>
        </ScrollArea>
        {!isLoading && !data.length && (
          <Text c="dimmed" ta="center" p="lg">
            No hay clientes{q ? ' que coincidan con la búsqueda' : ''}.
          </Text>
        )}
      </Paper>
      <Modal opened={nuevo} onClose={close} title="Nuevo cliente" size="xl">
        <ClienteForm onGuardado={(c) => navigate(`/clientes/${c.id}`)} />
      </Modal>
    </>
  );
}
