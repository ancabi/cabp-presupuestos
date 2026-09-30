import { useState } from 'react';
import { ActionIcon, Anchor, Button, Card, FileButton, Group, Image, Modal, SimpleGrid, Table, Text, Tooltip } from '@mantine/core';
import { IconDownload, IconFileTypePdf, IconTrash, IconUpload } from '@tabler/icons-react';
import type { Adjunto } from '@cabp/shared';
import { useAdjuntos, useBorrarAdjunto, useSubirAdjuntos } from '../api/hooks';
import { confirmar } from './confirmar';

const url = (a: Adjunto, descargar = false) => `/api/adjuntos/${a.id}/fichero${descargar ? '?descargar=1' : ''}`;
const fecha = (s: string) => new Date(s.replace(' ', 'T') + 'Z').toLocaleString('es-ES');
const tamano = (n: number) => (n > 1024 * 1024 ? `${(n / 1024 / 1024).toFixed(1)} MB` : `${Math.ceil(n / 1024)} KB`);

export function Adjuntos({ clienteId, tipo }: { clienteId: number; tipo: 'imagen' | 'pdf' }) {
  const { data = [] } = useAdjuntos(clienteId);
  const subir = useSubirAdjuntos(clienteId);
  const borrar = useBorrarAdjunto(clienteId);
  const [vista, setVista] = useState<Adjunto | null>(null);
  const lista = data.filter((a) => a.tipo === tipo);
  const accept = tipo === 'imagen' ? 'image/png,image/jpeg,image/gif,image/webp' : 'application/pdf';

  const pedirBorrado = (a: Adjunto) =>
    confirmar('Borrar fichero', `¿Borrar «${a.nombreOriginal}»? No se puede deshacer.`, () => borrar.mutate(a.id));

  return (
    <>
      <Group justify="flex-end" mb="md">
        <FileButton multiple accept={accept} onChange={(f) => f.length && subir.mutate(f)}>
          {(props) => (
            <Button {...props} leftSection={<IconUpload size={16} />} loading={subir.isPending}>
              {tipo === 'imagen' ? 'Subir imágenes' : 'Subir PDFs'}
            </Button>
          )}
        </FileButton>
      </Group>

      {!lista.length && (
        <Text c="dimmed" ta="center" p="lg">
          No hay {tipo === 'imagen' ? 'imágenes' : 'PDFs'}.
        </Text>
      )}

      {tipo === 'imagen' ? (
        <SimpleGrid cols={{ base: 2, sm: 3, md: 4 }}>
          {lista.map((a) => (
            <Card key={a.id} withBorder padding="xs">
              <Card.Section>
                <Image src={url(a)} h={160} fit="cover" alt={a.nombreOriginal} className="fila-clicable" onClick={() => setVista(a)} />
              </Card.Section>
              <Group justify="space-between" mt="xs" wrap="nowrap">
                <Text size="xs" truncate title={a.nombreOriginal}>
                  {a.nombreOriginal}
                </Text>
                <Group gap={2} wrap="nowrap">
                  <ActionIcon variant="subtle" component="a" href={url(a, true)} aria-label="Descargar">
                    <IconDownload size={16} />
                  </ActionIcon>
                  <ActionIcon variant="subtle" color="red" onClick={() => pedirBorrado(a)} aria-label="Borrar">
                    <IconTrash size={16} />
                  </ActionIcon>
                </Group>
              </Group>
            </Card>
          ))}
        </SimpleGrid>
      ) : (
        lista.length > 0 && (
          <Table>
            <Table.Tbody>
              {lista.map((a) => (
                <Table.Tr key={a.id}>
                  <Table.Td>
                    <Group gap="xs" wrap="nowrap">
                      <IconFileTypePdf size={18} color="var(--mantine-color-red-7)" />
                      <Anchor href={url(a)} target="_blank" rel="noopener">
                        {a.nombreOriginal}
                      </Anchor>
                    </Group>
                  </Table.Td>
                  <Table.Td c="dimmed">{tamano(a.tamano)}</Table.Td>
                  <Table.Td c="dimmed">{fecha(a.subidoEn)}</Table.Td>
                  <Table.Td>
                    <Group gap={2} justify="flex-end" wrap="nowrap">
                      <Tooltip label="Descargar">
                        <ActionIcon variant="subtle" component="a" href={url(a, true)}>
                          <IconDownload size={16} />
                        </ActionIcon>
                      </Tooltip>
                      <Tooltip label="Borrar">
                        <ActionIcon variant="subtle" color="red" onClick={() => pedirBorrado(a)}>
                          <IconTrash size={16} />
                        </ActionIcon>
                      </Tooltip>
                    </Group>
                  </Table.Td>
                </Table.Tr>
              ))}
            </Table.Tbody>
          </Table>
        )
      )}

      <Modal opened={!!vista} onClose={() => setVista(null)} title={vista?.nombreOriginal} size="xl" centered>
        {vista && <Image src={url(vista)} alt={vista.nombreOriginal} fit="contain" mah="75vh" />}
      </Modal>
    </>
  );
}
