import { Button, Center, Group, Loader, Tabs, Text, Title } from '@mantine/core';
import { IconFileInvoice, IconFileText, IconPhoto, IconFileTypePdf, IconTrash, IconUser } from '@tabler/icons-react';
import { useNavigate, useParams } from 'react-router-dom';
import { useBorrarCliente, useCliente, useDocumentos, notificarOk } from '../api/hooks';
import { ClienteForm } from '../components/ClienteForm';
import { Adjuntos } from '../components/Adjuntos';
import { DocumentosTabla } from '../components/DocumentosTabla';
import { confirmar } from '../components/confirmar';
import type { TipoDocumento } from '@cabp/shared';

function DocumentosCliente({ clienteId, tipo }: { clienteId: number; tipo: TipoDocumento }) {
  const { data = [], isLoading } = useDocumentos({ clienteId, tipo });
  const navigate = useNavigate();
  return (
    <>
      <Group justify="flex-end" mb="md">
        <Button onClick={() => navigate(`/documentos/nuevo?tipo=${tipo}&clienteId=${clienteId}`)}>
          {tipo === 'presupuesto' ? 'Nuevo presupuesto' : 'Nueva factura'}
        </Button>
      </Group>
      <DocumentosTabla datos={data} cargando={isLoading} mostrarCliente={false} />
    </>
  );
}

export function ClientePage() {
  const id = Number(useParams().id);
  const { data: cliente, isLoading } = useCliente(id);
  const borrar = useBorrarCliente();
  const navigate = useNavigate();

  if (isLoading)
    return (
      <Center p="xl">
        <Loader />
      </Center>
    );
  if (!cliente) return <Text>Cliente no encontrado.</Text>;

  const pedirBorrado = () =>
    confirmar(
      'Borrar cliente',
      `¿Borrar a ${cliente.nombre} ${cliente.apellidos} junto con sus imágenes y PDFs? No se puede deshacer.`,
      () =>
        borrar.mutate(cliente.id, {
          onSuccess: () => {
            notificarOk('Cliente borrado');
            navigate('/clientes');
          },
        }),
    );

  return (
    <>
      <Group justify="space-between" mb="md">
        <Title order={2}>
          {cliente.nombre} {cliente.apellidos}
        </Title>
        <Button variant="subtle" color="red" leftSection={<IconTrash size={16} />} onClick={pedirBorrado}>
          Borrar cliente
        </Button>
      </Group>
      <Tabs defaultValue="datos" keepMounted={false}>
        <Tabs.List mb="md">
          <Tabs.Tab value="datos" leftSection={<IconUser size={16} />}>
            Datos
          </Tabs.Tab>
          <Tabs.Tab value="presupuestos" leftSection={<IconFileText size={16} />}>
            Presupuestos
          </Tabs.Tab>
          <Tabs.Tab value="facturas" leftSection={<IconFileInvoice size={16} />}>
            Facturas
          </Tabs.Tab>
          <Tabs.Tab value="imagenes" leftSection={<IconPhoto size={16} />}>
            Imágenes
          </Tabs.Tab>
          <Tabs.Tab value="pdf" leftSection={<IconFileTypePdf size={16} />}>
            PDFs
          </Tabs.Tab>
        </Tabs.List>
        <Tabs.Panel value="datos">
          <ClienteForm key={cliente.id} cliente={cliente} onGuardado={() => notificarOk('Cliente guardado')} />
        </Tabs.Panel>
        <Tabs.Panel value="presupuestos">
          <DocumentosCliente clienteId={cliente.id} tipo="presupuesto" />
        </Tabs.Panel>
        <Tabs.Panel value="facturas">
          <DocumentosCliente clienteId={cliente.id} tipo="factura" />
        </Tabs.Panel>
        <Tabs.Panel value="imagenes">
          <Adjuntos clienteId={cliente.id} tipo="imagen" />
        </Tabs.Panel>
        <Tabs.Panel value="pdf">
          <Adjuntos clienteId={cliente.id} tipo="pdf" />
        </Tabs.Panel>
      </Tabs>
    </>
  );
}
