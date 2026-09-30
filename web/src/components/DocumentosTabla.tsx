import { Badge, Paper, ScrollArea, Table, Text } from '@mantine/core';
import { formatoEuros, formatoFecha, type DocumentoResumen } from '@cabp/shared';
import { useNavigate } from 'react-router-dom';

export function DocumentosTabla({
  datos,
  cargando,
  mostrarCliente = true,
}: {
  datos: DocumentoResumen[];
  cargando?: boolean;
  mostrarCliente?: boolean;
}) {
  const navigate = useNavigate();
  return (
    <Paper withBorder>
      <ScrollArea>
        <Table striped highlightOnHover miw={600}>
          <Table.Thead>
            <Table.Tr>
              <Table.Th>Número</Table.Th>
              <Table.Th>Fecha</Table.Th>
              {mostrarCliente && <Table.Th>Cliente</Table.Th>}
              <Table.Th>Distribuidor</Table.Th>
              <Table.Th className="importe">Total</Table.Th>
              <Table.Th />
            </Table.Tr>
          </Table.Thead>
          <Table.Tbody>
            {datos.map((d) => (
              <Table.Tr key={d.id} className="fila-clicable" onClick={() => navigate(`/documentos/${d.id}`)}>
                <Table.Td fw={600} ff="monospace">
                  {d.codigo}
                </Table.Td>
                <Table.Td>{formatoFecha(d.fecha)}</Table.Td>
                {mostrarCliente && <Table.Td>{d.clienteNombre}</Table.Td>}
                <Table.Td>{d.distribuidorNombre}</Table.Td>
                <Table.Td className="importe">{formatoEuros(d.totalConIva)}</Table.Td>
                <Table.Td>
                  {d.tipo === 'presupuesto' && d.facturaId && (
                    <Badge color="green" variant="light">
                      Facturado
                    </Badge>
                  )}
                </Table.Td>
              </Table.Tr>
            ))}
          </Table.Tbody>
        </Table>
      </ScrollArea>
      {!cargando && !datos.length && (
        <Text c="dimmed" ta="center" p="lg">
          No hay documentos.
        </Text>
      )}
    </Paper>
  );
}
