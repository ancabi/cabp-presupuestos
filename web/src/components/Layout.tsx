import { AppShell, Burger, Group, Image, Menu, NavLink, Select, Text, UnstyledButton } from '@mantine/core';
import { useDisclosure } from '@mantine/hooks';
import {
  IconBuildingStore,
  IconChevronDown,
  IconFileInvoice,
  IconFileText,
  IconKey,
  IconLogout,
  IconPackage,
  IconSettings,
  IconUsers,
  IconUserShield,
} from '@tabler/icons-react';
import { NavLink as RouterLink, Outlet, useLocation, useNavigate } from 'react-router-dom';
import { useQueryClient } from '@tanstack/react-query';
import { useAnios, useYo } from '../api/hooks';
import { http } from '../api/http';
import { EjercicioProvider, useEjercicio } from './Ejercicio';

function SelectorEjercicio() {
  const { anio, setAnio } = useEjercicio();
  const { data: anios = [] } = useAnios();
  const actual = new Date().getFullYear();
  const lista = [...new Set([actual, ...anios, ...(anio ? [anio] : [])])].sort((a, b) => b - a);
  return (
    <Select
      aria-label="Ejercicio"
      w={140}
      value={anio == null ? 'todos' : String(anio)}
      onChange={(v) => setAnio(!v || v === 'todos' ? null : Number(v))}
      data={[...lista.map((a) => ({ value: String(a), label: `Ejercicio ${a}` })), { value: 'todos', label: 'Todos los años' }]}
      allowDeselect={false}
    />
  );
}

function Contenido() {
  const [abierto, { toggle, close }] = useDisclosure();
  const { data: yo } = useYo();
  const location = useLocation();
  const navigate = useNavigate();
  const qc = useQueryClient();

  const enlaces = [
    { to: '/presupuestos', label: 'Presupuestos', icon: IconFileText },
    { to: '/facturas', label: 'Facturas', icon: IconFileInvoice },
    { to: '/pedidos', label: 'Pedidos', icon: IconPackage },
    { to: '/clientes', label: 'Clientes', icon: IconUsers },
    { to: '/distribuidores', label: 'Distribuidores y productos', icon: IconBuildingStore },
    ...(yo?.rol === 'admin'
      ? [
          { to: '/ajustes', label: 'Ajustes', icon: IconSettings },
          { to: '/usuarios', label: 'Usuarios', icon: IconUserShield },
        ]
      : []),
  ];

  const salir = async () => {
    await http.post('/auth/logout').catch(() => {});
    qc.clear();
    navigate('/login', { replace: true });
  };

  return (
    <AppShell header={{ height: 60 }} navbar={{ width: 250, breakpoint: 'sm', collapsed: { mobile: !abierto } }} padding="md">
      <AppShell.Header>
        <Group h="100%" px="md" justify="space-between" wrap="nowrap">
          <Group gap="sm" wrap="nowrap">
            <Burger opened={abierto} onClick={toggle} hiddenFrom="sm" size="sm" />
            <Image src="/logo.png" h={36} w="auto" alt="" />
            <Text fw={700} visibleFrom="xs">
              CABP
            </Text>
          </Group>
          <Group gap="sm" wrap="nowrap">
            <SelectorEjercicio />
            <Menu position="bottom-end">
              <Menu.Target>
                <UnstyledButton>
                  <Group gap={4} wrap="nowrap">
                    <Text size="sm" visibleFrom="sm">
                      {yo?.nombre}
                    </Text>
                    <IconChevronDown size={16} />
                  </Group>
                </UnstyledButton>
              </Menu.Target>
              <Menu.Dropdown>
                <Menu.Label>{yo?.email}</Menu.Label>
                <Menu.Item leftSection={<IconKey size={16} />} onClick={() => navigate('/cuenta')}>
                  Cambiar contraseña
                </Menu.Item>
                <Menu.Item leftSection={<IconLogout size={16} />} color="red" onClick={salir}>
                  Cerrar sesión
                </Menu.Item>
              </Menu.Dropdown>
            </Menu>
          </Group>
        </Group>
      </AppShell.Header>
      <AppShell.Navbar p="sm">
        {enlaces.map((e) => (
          <NavLink
            key={e.to}
            component={RouterLink}
            to={e.to}
            label={e.label}
            leftSection={<e.icon size={18} />}
            active={location.pathname.startsWith(e.to)}
            onClick={close}
          />
        ))}
      </AppShell.Navbar>
      <AppShell.Main>
        <Outlet />
      </AppShell.Main>
    </AppShell>
  );
}

export function Layout() {
  return (
    <EjercicioProvider>
      <Contenido />
    </EjercicioProvider>
  );
}
