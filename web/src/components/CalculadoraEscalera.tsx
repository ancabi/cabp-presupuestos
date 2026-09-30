import { Button, Group, Modal, SegmentedControl, SimpleGrid, Stack, Table, Text } from '@mantine/core';
import { calcularEscalera, type DocumentoInput } from '@cabp/shared';
import { Numero } from './Numero';

type Valores = Pick<DocumentoInput, 'calcTipo' | 'valorA' | 'valorB' | 'valorC' | 'valorAux'>;

const fmt = (n: number) => n.toLocaleString('es-ES', { maximumFractionDigits: 2 });

/** Calculadora de medidas de escalera (antes DialogoPitagoras). Los valores se guardan con el documento. */
export function CalculadoraEscalera({
  abierta,
  onClose,
  valores,
  onChange,
}: {
  abierta: boolean;
  onClose: () => void;
  valores: Valores;
  onChange: (v: Valores) => void;
}) {
  const { calcTipo, valorA, valorB, valorC, valorAux } = valores;
  const r = calcularEscalera(calcTipo, valorA, valorB, valorC, valorAux);
  const set = (k: keyof Valores) => (n: number) => onChange({ ...valores, [k]: n });

  return (
    <Modal opened={abierta} onClose={onClose} title="Calculadora de escalera" size="lg">
      <Stack>
        <SegmentedControl
          value={calcTipo}
          onChange={(v) => onChange({ ...valores, calcTipo: v as Valores['calcTipo'] })}
          data={[
            { value: 'pitagoras', label: 'Pitágoras (peldaños)' },
            { value: 'stepper', label: 'Stepper' },
          ]}
        />
        {calcTipo === 'pitagoras' ? (
          <>
            <SimpleGrid cols={{ base: 1, xs: 2 }}>
              <Numero label="Medida A (huella, cm)" value={valorA} onChange={set('valorA')} />
              <Numero label="Medida B (tabica, cm)" value={valorB} onChange={set('valorB')} />
              <Numero label="Nº de peldaños" value={valorC} onChange={set('valorC')} />
              <Numero label="Sumar plataforma (m)" value={valorAux} onChange={set('valorAux')} />
            </SimpleGrid>
            <Table>
              <Table.Tbody>
                <Table.Tr>
                  <Table.Td>A × peldaños</Table.Td>
                  <Table.Td className="importe">{fmt(r.catetoA)} cm</Table.Td>
                </Table.Tr>
                <Table.Tr>
                  <Table.Td>B × peldaños</Table.Td>
                  <Table.Td className="importe">{fmt(r.catetoB)} cm</Table.Td>
                </Table.Tr>
                <Table.Tr>
                  <Table.Td>Hipotenusa</Table.Td>
                  <Table.Td className="importe">
                    {fmt(r.hipotenusa)} cm · {fmt(r.metros)} m
                  </Table.Td>
                </Table.Tr>
                <Table.Tr>
                  <Table.Td fw={700}>Total con plataforma</Table.Td>
                  <Table.Td className="importe" fw={700}>
                    {fmt(r.total)} m
                  </Table.Td>
                </Table.Tr>
              </Table.Tbody>
            </Table>
          </>
        ) : (
          <>
            <SimpleGrid cols={{ base: 1, xs: 3 }}>
              <Numero label="Valor V" value={valorA} onChange={set('valorA')} />
              <Numero label="Valor A" value={valorB} onChange={set('valorB')} />
              <Numero label="Valor O" value={valorC} onChange={set('valorC')} />
            </SimpleGrid>
            <Text>
              √((V − A)² + O²) = <b>{fmt(r.hipotenusa)}</b>
            </Text>
          </>
        )}
        <Group justify="flex-end">
          <Button onClick={onClose}>Cerrar</Button>
        </Group>
      </Stack>
    </Modal>
  );
}
