import { useEffect, useState } from 'react';
import { Button, Group, Modal, Stack, Text, Textarea } from '@mantine/core';

/** Pide el nombre de un pedido (al convertir un presupuesto en factura o al abrir el pedido de una factura antigua). */
export function NombrePedidoModal({
  abierto,
  titulo,
  explicacion,
  inicial = '',
  etiquetaBoton,
  cargando,
  onCerrar,
  onAceptar,
}: {
  abierto: boolean;
  titulo: string;
  explicacion?: string;
  inicial?: string;
  etiquetaBoton: string;
  cargando?: boolean;
  onCerrar: () => void;
  onAceptar: (nombre: string) => void;
}) {
  const [nombre, setNombre] = useState(inicial);
  useEffect(() => {
    if (abierto) setNombre(inicial);
  }, [abierto, inicial]);

  return (
    <Modal opened={abierto} onClose={onCerrar} title={titulo} size="lg" centered>
      <form
        onSubmit={(e) => {
          e.preventDefault();
          if (nombre.trim()) onAceptar(nombre.trim());
        }}
      >
        <Stack>
          {explicacion && <Text size="sm">{explicacion}</Text>}
          <Textarea
            label="Nombre del pedido"
            required
            autosize
            minRows={2}
            maxLength={1000}
            data-autofocus
            value={nombre}
            onChange={(e) => setNombre(e.currentTarget.value)}
          />
          <Group justify="flex-end">
            <Button variant="default" onClick={onCerrar}>
              Cancelar
            </Button>
            <Button type="submit" loading={cargando} disabled={!nombre.trim()}>
              {etiquetaBoton}
            </Button>
          </Group>
        </Stack>
      </form>
    </Modal>
  );
}
