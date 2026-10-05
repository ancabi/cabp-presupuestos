import { useEffect, useState } from 'react';
import {
  ActionIcon,
  Anchor,
  Badge,
  Button,
  Center,
  FileButton,
  Group,
  Loader,
  Paper,
  ScrollArea,
  Select,
  Stack,
  Table,
  Text,
  Textarea,
  Title,
  Tooltip,
} from '@mantine/core';
import { IconDownload, IconEdit, IconFileTypePdf, IconPhoto, IconPrinter, IconTrash, IconUpload } from '@tabler/icons-react';
import { Link, useParams } from 'react-router-dom';
import { formatoEuros, formatoFecha, type PedidoFichero } from '@cabp/shared';
import {
  notificarOk,
  useBorrarFicheroPedido,
  useCambiarTipoFichero,
  usePedido,
  useRenombrarPedido,
  useSubirFicherosPedido,
  useTiposFichero,
} from '../api/hooks';
import { confirmar } from '../components/confirmar';
import { fechaHora } from '../fechaHora';

const url = (f: PedidoFichero, descargar = false) => `/api/pedido-ficheros/${f.id}/fichero${descargar ? '?descargar=1' : ''}`;
const tamano = (n: number) => (n > 1024 * 1024 ? `${(n / 1024 / 1024).toFixed(1)} MB` : `${Math.ceil(n / 1024)} KB`);

export function PedidoPage() {
  const id = Number(useParams().id);
  const { data: p, isLoading } = usePedido(id);
  const { data: tipos = [] } = useTiposFichero();
  const renombrar = useRenombrarPedido(id);
  const subir = useSubirFicherosPedido(id);
  const cambiarTipo = useCambiarTipoFichero(id);
  const borrar = useBorrarFicheroPedido(id);
  const [nombre, setNombre] = useState('');
  const [tipoSubida, setTipoSubida] = useState<string | null>(null);
  useEffect(() => {
    if (p) setNombre(p.nombre);
  }, [p?.nombre]);

  if (isLoading)
    return (
      <Center p="xl">
        <Loader />
      </Center>
    );
  if (!p) return <Text>Pedido no encontrado.</Text>;

  const tipoFactura = tipos.find((t) => t.sistema)?.nombre ?? 'Factura cliente';
  const opcionesTipo = tipos.map((t) => ({ value: String(t.id), label: t.nombre }));
  const nombreCambiado = nombre.trim() !== p.nombre && nombre.trim() !== '';

  const pedirBorrado = (f: PedidoFichero) =>
    confirmar('Borrar fichero', `¿Borrar «${f.nombreOriginal}» del pedido? No se puede deshacer.`, () =>
      borrar.mutate(f.id, { onSuccess: () => notificarOk('Fichero borrado') }),
    );

  return (
    <Stack>
      <Title order={2}>Pedido</Title>

      <Paper withBorder p="md">
        <form
          onSubmit={(e) => {
            e.preventDefault();
            if (nombreCambiado) renombrar.mutate(nombre.trim(), { onSuccess: () => notificarOk('Nombre del pedido guardado') });
          }}
        >
          <Textarea
            label="Nombre del pedido"
            autosize
            minRows={2}
            maxLength={1000}
            value={nombre}
            onChange={(e) => setNombre(e.currentTarget.value)}
          />
          <Group justify="space-between" mt="sm" align="flex-start">
            <Stack gap={2}>
              <Text size="sm">
                Cliente:{' '}
                <Anchor component={Link} to={`/clientes/${p.clienteId}`}>
                  {p.clienteNombre}
                </Anchor>
              </Text>
              <Text size="sm" c="dimmed">
                Creado el {fechaHora(p.creadoEn)} · Última modificación el {fechaHora(p.actualizadoEn)}
              </Text>
            </Stack>
            <Button type="submit" disabled={!nombreCambiado} loading={renombrar.isPending}>
              Guardar nombre
            </Button>
          </Group>
        </form>
      </Paper>

      <Paper withBorder p="md">
        <Group justify="space-between" mb="sm" align="flex-end">
          <Title order={4}>Ficheros</Title>
          <Group gap="xs" align="flex-end">
            <Select
              label="Tipo del fichero a subir"
              placeholder="Elige el tipo…"
              w={240}
              data={opcionesTipo}
              value={tipoSubida}
              onChange={setTipoSubida}
              allowDeselect={false}
            />
            <FileButton
              multiple
              accept="application/pdf,image/png,image/jpeg,image/gif,image/webp"
              onChange={(f) =>
                f.length &&
                tipoSubida &&
                subir.mutate(
                  { tipoId: Number(tipoSubida), ficheros: f },
                  { onSuccess: (r) => notificarOk(r.length === 1 ? 'Fichero subido' : `${r.length} ficheros subidos`) },
                )
              }
            >
              {(props) => (
                <Tooltip label="Elige antes el tipo" disabled={!!tipoSubida}>
                  <Button {...props} leftSection={<IconUpload size={16} />} loading={subir.isPending} disabled={!tipoSubida}>
                    Subir ficheros
                  </Button>
                </Tooltip>
              )}
            </FileButton>
          </Group>
        </Group>

        <ScrollArea>
          <Table miw={700} verticalSpacing="xs">
            <Table.Thead>
              <Table.Tr>
                <Table.Th w={220}>Tipo</Table.Th>
                <Table.Th>Fichero</Table.Th>
                <Table.Th>Tamaño</Table.Th>
                <Table.Th>Fecha</Table.Th>
                <Table.Th />
              </Table.Tr>
            </Table.Thead>
            <Table.Tbody>
              <Table.Tr bg="var(--mantine-color-grape-light)">
                <Table.Td>
                  <Badge color="grape" variant="light">
                    {tipoFactura}
                  </Badge>
                </Table.Td>
                {p.factura ? (
                  <>
                    <Table.Td>
                      <Group gap="xs" wrap="nowrap">
                        <IconFileTypePdf size={18} color="var(--mantine-color-red-7)" />
                        <Anchor href={`/documentos/${p.factura.id}/imprimir`} target="_blank" rel="noopener" ff="monospace">
                          Factura {p.factura.codigo}
                        </Anchor>
                        <Text span size="sm" c="dimmed">
                          · {formatoEuros(p.factura.totalConIva)}
                        </Text>
                      </Group>
                    </Table.Td>
                    <Table.Td c="dimmed">—</Table.Td>
                    <Table.Td c="dimmed">{formatoFecha(p.factura.fecha)}</Table.Td>
                    <Table.Td>
                      <Group gap={2} justify="flex-end" wrap="nowrap">
                        <Tooltip label="Ver / imprimir PDF">
                          <ActionIcon variant="subtle" component="a" href={`/documentos/${p.factura.id}/imprimir`} target="_blank">
                            <IconPrinter size={16} />
                          </ActionIcon>
                        </Tooltip>
                        <Tooltip label="Editar factura">
                          <ActionIcon variant="subtle" component={Link} to={`/documentos/${p.factura.id}`}>
                            <IconEdit size={16} />
                          </ActionIcon>
                        </Tooltip>
                      </Group>
                    </Table.Td>
                  </>
                ) : (
                  <Table.Td colSpan={4} c="dimmed">
                    Sin factura (se borró).
                  </Table.Td>
                )}
              </Table.Tr>
              {p.ficheros.map((f) => (
                <Table.Tr key={f.id}>
                  <Table.Td>
                    <Select
                      size="xs"
                      aria-label={`Tipo de ${f.nombreOriginal}`}
                      data={opcionesTipo}
                      value={String(f.tipoId)}
                      allowDeselect={false}
                      onChange={(v) => v && Number(v) !== f.tipoId && cambiarTipo.mutate({ id: f.id, tipoId: Number(v) })}
                    />
                  </Table.Td>
                  <Table.Td>
                    <Group gap="xs" wrap="nowrap">
                      {f.mime === 'application/pdf' ? (
                        <IconFileTypePdf size={18} color="var(--mantine-color-red-7)" />
                      ) : (
                        <IconPhoto size={18} color="var(--mantine-color-blue-7)" />
                      )}
                      <Anchor href={url(f)} target="_blank" rel="noopener">
                        {f.nombreOriginal}
                      </Anchor>
                    </Group>
                  </Table.Td>
                  <Table.Td c="dimmed">{tamano(f.tamano)}</Table.Td>
                  <Table.Td c="dimmed" style={{ whiteSpace: 'nowrap' }}>
                    {fechaHora(f.subidoEn)}
                  </Table.Td>
                  <Table.Td>
                    <Group gap={2} justify="flex-end" wrap="nowrap">
                      <Tooltip label="Descargar">
                        <ActionIcon variant="subtle" component="a" href={url(f, true)}>
                          <IconDownload size={16} />
                        </ActionIcon>
                      </Tooltip>
                      <Tooltip label="Borrar">
                        <ActionIcon variant="subtle" color="red" onClick={() => pedirBorrado(f)} aria-label={`Borrar ${f.nombreOriginal}`}>
                          <IconTrash size={16} />
                        </ActionIcon>
                      </Tooltip>
                    </Group>
                  </Table.Td>
                </Table.Tr>
              ))}
            </Table.Tbody>
          </Table>
        </ScrollArea>
        <Text size="xs" c="dimmed" mt="xs">
          Se admiten PDF e imágenes (JPG, PNG, GIF, WebP) de hasta 20 MB. Los tipos se gestionan en Ajustes.
        </Text>
      </Paper>
    </Stack>
  );
}
