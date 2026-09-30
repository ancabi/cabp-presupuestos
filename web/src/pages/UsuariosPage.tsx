import { useState } from 'react';
import { ActionIcon, Badge, Button, Group, Modal, Paper, PasswordInput, Select, Stack, Switch, Table, Text, TextInput, Title } from '@mantine/core';
import { IconPencil, IconPlus, IconTrash } from '@tabler/icons-react';
import type { Usuario } from '@cabp/shared';
import { useBorrarUsuario, useGuardarUsuario, useUsuarios, useYo } from '../api/hooks';
import { confirmar } from '../components/confirmar';

function UsuarioForm({ usuario, onHecho }: { usuario?: Usuario; onHecho: () => void }) {
  const [d, setD] = useState({
    email: usuario?.email ?? '',
    nombre: usuario?.nombre ?? '',
    rol: usuario?.rol ?? 'usuario',
    activo: usuario?.activo ?? true,
    password: '',
  });
  const guardar = useGuardarUsuario();
  return (
    <form
      onSubmit={(e) => {
        e.preventDefault();
        const datos = usuario
          ? { nombre: d.nombre, rol: d.rol, activo: d.activo, password: d.password }
          : { email: d.email, nombre: d.nombre, rol: d.rol, password: d.password };
        guardar.mutate({ id: usuario?.id, datos }, { onSuccess: onHecho });
      }}
    >
      <Stack>
        <TextInput label="Email" type="email" required disabled={!!usuario} value={d.email} onChange={(e) => setD({ ...d, email: e.currentTarget.value })} />
        <TextInput label="Nombre" required value={d.nombre} onChange={(e) => setD({ ...d, nombre: e.currentTarget.value })} />
        <Select
          label="Rol"
          value={d.rol}
          onChange={(v) => setD({ ...d, rol: (v as Usuario['rol']) ?? 'usuario' })}
          data={[
            { value: 'usuario', label: 'Usuario' },
            { value: 'admin', label: 'Administrador' },
          ]}
          allowDeselect={false}
        />
        <PasswordInput
          label={usuario ? 'Nueva contraseña (vacío para no cambiarla)' : 'Contraseña'}
          required={!usuario}
          minLength={usuario && !d.password ? undefined : 8}
          value={d.password}
          onChange={(e) => setD({ ...d, password: e.currentTarget.value })}
        />
        {usuario && <Switch label="Activo" checked={d.activo} onChange={(e) => setD({ ...d, activo: e.currentTarget.checked })} />}
        <Group justify="flex-end">
          <Button type="submit" loading={guardar.isPending}>
            Guardar
          </Button>
        </Group>
      </Stack>
    </form>
  );
}

export function UsuariosPage() {
  const { data = [] } = useUsuarios();
  const { data: yo } = useYo();
  const borrar = useBorrarUsuario();
  const [editando, setEditando] = useState<Usuario | 'nuevo' | null>(null);

  if (yo?.rol !== 'admin') return <Text>Solo para administradores.</Text>;

  return (
    <>
      <Group justify="space-between" mb="md">
        <Title order={2}>Usuarios</Title>
        <Button leftSection={<IconPlus size={16} />} onClick={() => setEditando('nuevo')}>
          Nuevo usuario
        </Button>
      </Group>
      <Paper withBorder>
        <Table>
          <Table.Thead>
            <Table.Tr>
              <Table.Th>Nombre</Table.Th>
              <Table.Th>Email</Table.Th>
              <Table.Th>Rol</Table.Th>
              <Table.Th>Estado</Table.Th>
              <Table.Th />
            </Table.Tr>
          </Table.Thead>
          <Table.Tbody>
            {data.map((u) => (
              <Table.Tr key={u.id}>
                <Table.Td>{u.nombre}</Table.Td>
                <Table.Td>{u.email}</Table.Td>
                <Table.Td>{u.rol === 'admin' ? 'Administrador' : 'Usuario'}</Table.Td>
                <Table.Td>
                  <Badge color={u.activo ? 'green' : 'gray'} variant="light">
                    {u.activo ? 'Activo' : 'Inactivo'}
                  </Badge>
                </Table.Td>
                <Table.Td>
                  <Group gap={2} justify="flex-end" wrap="nowrap">
                    <ActionIcon variant="subtle" onClick={() => setEditando(u)} aria-label="Editar">
                      <IconPencil size={16} />
                    </ActionIcon>
                    {u.id !== yo.id && (
                      <ActionIcon
                        variant="subtle"
                        color="red"
                        aria-label="Borrar"
                        onClick={() => confirmar('Borrar usuario', `¿Borrar a ${u.nombre}?`, () => borrar.mutate(u.id))}
                      >
                        <IconTrash size={16} />
                      </ActionIcon>
                    )}
                  </Group>
                </Table.Td>
              </Table.Tr>
            ))}
          </Table.Tbody>
        </Table>
      </Paper>
      <Modal opened={!!editando} onClose={() => setEditando(null)} title={editando === 'nuevo' ? 'Nuevo usuario' : 'Editar usuario'}>
        {editando && <UsuarioForm usuario={editando === 'nuevo' ? undefined : editando} onHecho={() => setEditando(null)} />}
      </Modal>
    </>
  );
}
