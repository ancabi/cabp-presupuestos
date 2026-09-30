import { Text } from '@mantine/core';
import { modals } from '@mantine/modals';

export function confirmar(titulo: string, mensaje: string, onConfirm: () => void, etiqueta = 'Borrar') {
  modals.openConfirmModal({
    title: titulo,
    children: <Text size="sm">{mensaje}</Text>,
    labels: { confirm: etiqueta, cancel: 'Cancelar' },
    confirmProps: { color: 'red' },
    onConfirm,
  });
}
