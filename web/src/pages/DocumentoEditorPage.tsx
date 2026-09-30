import { useEffect, useMemo, useState } from 'react';
import {
  ActionIcon,
  Alert,
  Anchor,
  Badge,
  Button,
  Center,
  Checkbox,
  Divider,
  Grid,
  Group,
  Loader,
  Paper,
  ScrollArea,
  Select,
  SimpleGrid,
  Slider,
  Stack,
  Table,
  Text,
  Textarea,
  TextInput,
  Title,
} from '@mantine/core';
import { useDisclosure } from '@mantine/hooks';
import {
  IconArrowRight,
  IconCalculator,
  IconDeviceFloppy,
  IconFileInvoice,
  IconPlus,
  IconPrinter,
  IconTrash,
} from '@tabler/icons-react';
import { Link, useNavigate, useParams, useSearchParams } from 'react-router-dom';
import {
  anioDeFecha,
  calcularTotales,
  calcularViaje,
  formatoEuros,
  hoyIso,
  importeLinea,
  type Documento,
  type DocumentoInput,
  type TipoDocumento,
} from '@cabp/shared';
import {
  notificarOk,
  useAjustes,
  useBorrarDocumento,
  useClientes,
  useConvertirAFactura,
  useDistribuidores,
  useDocumento,
  useGuardarDocumento,
  useProductos,
  useProximoNumero,
} from '../api/hooks';
import { Numero } from '../components/Numero';
import { CalculadoraEscalera } from '../components/CalculadoraEscalera';
import { confirmar } from '../components/confirmar';

function nuevoDocumento(tipo: TipoDocumento, clienteId: number, iva: number, formaPago: string): DocumentoInput {
  return {
    tipo,
    fecha: hoyIso(),
    clienteId,
    distribuidorId: null,
    lineas: [],
    ganancia: 0,
    restaurante: 0,
    pasaje: 0,
    combustible: 0,
    otros: 0,
    hotel: 0,
    transporte: 0,
    kilometros: 0,
    numViajes: 2,
    precioGasolina: 1,
    aplicaGanancia: true,
    aplicaIva: true,
    ivaPorcentaje: iva,
    totalManualActivo: false,
    totalManual: 0,
    porcentajeReparto: 50,
    textoConcepto: '',
    textoFormaPago: formaPago,
    textoExplicativo: '',
    calcTipo: 'pitagoras',
    valorA: 0,
    valorB: 0,
    valorC: 0,
    valorAux: 0,
  };
}

function aInput(d: Documento): DocumentoInput {
  const {
    id: _id,
    anio: _a,
    numero: _n,
    codigo: _c,
    presupuestoOrigenId: _p,
    facturaId: _f,
    cliente: _cl,
    distribuidor: _di,
    totalSinIva: _t1,
    totalIva: _t2,
    totalConIva: _t3,
    lineas,
    ...resto
  } = d;
  return { ...resto, lineas: lineas.map(({ id: _lid, ...l }) => l) };
}

export function DocumentoEditorPage() {
  const params = useParams();
  const id = params.id ? Number(params.id) : undefined;
  const { data: doc, isLoading } = useDocumento(id);
  const { data: ajustes } = useAjustes();
  const [sp] = useSearchParams();

  if ((id && isLoading) || !ajustes)
    return (
      <Center p="xl">
        <Loader />
      </Center>
    );
  if (id && !doc) return <Text>Documento no encontrado.</Text>;

  const inicial = doc
    ? aInput(doc)
    : nuevoDocumento(
        sp.get('tipo') === 'factura' ? 'factura' : 'presupuesto',
        Number(sp.get('clienteId')) || 0,
        ajustes.ivaPorcentaje,
        ajustes.textoFormaPagoDefecto,
      );
  return <Editor key={doc ? `${doc.id}` : 'nuevo'} doc={doc} inicial={inicial} />;
}

function Editor({ doc, inicial }: { doc?: Documento; inicial: DocumentoInput }) {
  const [d, setD] = useState<DocumentoInput>(inicial);
  const [sucio, setSucio] = useState(false);
  const [calcAbierta, calc] = useDisclosure();
  const [productoSel, setProductoSel] = useState<string | null>(null);
  const navigate = useNavigate();

  const { data: clientes = [] } = useClientes('');
  const { data: distribuidores = [] } = useDistribuidores();
  const { data: productos = [] } = useProductos(d.distribuidorId);
  const guardar = useGuardarDocumento();
  const borrar = useBorrarDocumento();
  const convertir = useConvertirAFactura();
  const proximo = useProximoNumero(d.tipo, anioDeFecha(d.fecha) || new Date().getFullYear(), !doc);

  const t = useMemo(() => calcularTotales(d), [d]);
  const viaje = calcularViaje(d.kilometros, d.numViajes, d.precioGasolina);
  const esPresupuesto = d.tipo === 'presupuesto';
  const nombreTipo = esPresupuesto ? 'Presupuesto' : 'Factura';

  // Aviso al salir con cambios sin guardar.
  useEffect(() => {
    if (!sucio) return;
    const h = (e: BeforeUnloadEvent) => e.preventDefault();
    window.addEventListener('beforeunload', h);
    return () => window.removeEventListener('beforeunload', h);
  }, [sucio]);

  const cambiar = (parcial: Partial<DocumentoInput>) => {
    setD((prev) => ({ ...prev, ...parcial }));
    setSucio(true);
  };
  const num = (k: keyof DocumentoInput) => ({
    value: d[k] as number,
    onChange: (n: number) => cambiar({ [k]: n } as Partial<DocumentoInput>),
  });
  const setLinea = (i: number, parcial: Partial<DocumentoInput['lineas'][number]>) =>
    cambiar({ lineas: d.lineas.map((l, j) => (j === i ? { ...l, ...parcial } : l)) });

  const anadirProducto = () => {
    const p = productos.find((x) => String(x.id) === productoSel);
    if (!p) return;
    const i = d.lineas.findIndex((l) => l.productoId === p.id);
    if (i >= 0) setLinea(i, { cantidad: d.lineas[i].cantidad + 1 });
    else cambiar({ lineas: [...d.lineas, { productoId: p.id, nombreProducto: p.nombre, cantidad: 1, precio: p.precio }] });
    setProductoSel(null);
  };

  const onGuardar = (despues?: (doc: Documento) => void) => {
    if (!d.clienteId) return;
    guardar.mutate(
      { id: doc?.id, datos: d },
      {
        onSuccess: (g) => {
          setSucio(false);
          notificarOk(`${nombreTipo} ${g.codigo} guardado`);
          if (despues) despues(g);
          else if (!doc) navigate(`/documentos/${g.id}`, { replace: true });
        },
      },
    );
  };

  const imprimir = () => {
    const abrir = (g: Documento) => window.open(`/documentos/${g.id}/imprimir`, '_blank');
    if (doc && !sucio) abrir(doc);
    else
      onGuardar((g) => {
        abrir(g);
        if (!doc) navigate(`/documentos/${g.id}`, { replace: true });
      });
  };

  const pedirBorrado = () =>
    doc &&
    confirmar(
      `Borrar ${nombreTipo.toLowerCase()}`,
      esPresupuesto
        ? `¿Borrar el presupuesto ${doc.codigo}? No se puede deshacer.`
        : `¿Borrar la factura ${doc.codigo}? Solo es posible si es la última del año ${doc.anio}.`,
      () =>
        borrar.mutate(doc.id, {
          onSuccess: () => {
            setSucio(false);
            notificarOk('Documento borrado');
            navigate(esPresupuesto ? '/presupuestos' : '/facturas');
          },
        }),
    );

  const pedirConversion = () =>
    doc &&
    confirmar(
      'Convertir a factura',
      `Se creará una factura con fecha de hoy copiando el presupuesto ${doc.codigo}. ¿Continuar?`,
      () =>
        convertir.mutate(doc.id, {
          onSuccess: (f) => {
            notificarOk(`Factura ${f.codigo} creada`);
            navigate(`/documentos/${f.id}`);
          },
        }),
      'Convertir',
    );

  const anioFijo = doc?.anio;

  return (
    <Stack>
      <Group justify="space-between">
        <Group gap="sm">
          <Title order={2}>
            {nombreTipo} {doc ? doc.codigo : proximo.data ? `(nuevo · ${proximo.data.codigo})` : '(nuevo)'}
          </Title>
          {sucio && (
            <Badge color="orange" variant="light">
              Sin guardar
            </Badge>
          )}
        </Group>
        <Group gap="xs">
          <Button leftSection={<IconCalculator size={16} />} variant="default" onClick={calc.open}>
            Calculadora
          </Button>
          <Button leftSection={<IconPrinter size={16} />} variant="default" onClick={imprimir} disabled={!d.clienteId}>
            Imprimir / PDF
          </Button>
          {doc && esPresupuesto && !doc.facturaId && (
            <Button leftSection={<IconFileInvoice size={16} />} variant="light" onClick={pedirConversion} loading={convertir.isPending}>
              Convertir a factura
            </Button>
          )}
          {doc && (
            <ActionIcon size="lg" variant="subtle" color="red" onClick={pedirBorrado} aria-label="Borrar">
              <IconTrash size={18} />
            </ActionIcon>
          )}
          <Button leftSection={<IconDeviceFloppy size={16} />} onClick={() => onGuardar()} loading={guardar.isPending} disabled={!d.clienteId}>
            Guardar
          </Button>
        </Group>
      </Group>

      {doc?.facturaId && (
        <Alert color="green" p="xs">
          Este presupuesto ya está facturado.{' '}
          <Anchor component={Link} to={`/documentos/${doc.facturaId}`}>
            Ver factura <IconArrowRight size={12} />
          </Anchor>
        </Alert>
      )}
      {doc?.presupuestoOrigenId && (
        <Alert color="blue" p="xs">
          Factura creada a partir de un presupuesto.{' '}
          <Anchor component={Link} to={`/documentos/${doc.presupuestoOrigenId}`}>
            Ver presupuesto <IconArrowRight size={12} />
          </Anchor>
        </Alert>
      )}

      <Paper withBorder p="md">
        <Grid>
          <Grid.Col span={{ base: 12, md: 5 }}>
            <Select
              label="Cliente"
              required
              searchable
              placeholder="Buscar cliente…"
              value={d.clienteId ? String(d.clienteId) : null}
              onChange={(v) => cambiar({ clienteId: Number(v) || 0 })}
              data={clientes.map((c) => ({ value: String(c.id), label: `${c.nombre} ${c.apellidos}${c.dni ? ` · ${c.dni}` : ''}` }))}
              nothingFoundMessage="Sin resultados"
            />
            {d.clienteId > 0 && (
              <Anchor component={Link} to={`/clientes/${d.clienteId}`} size="xs">
                Ver ficha del cliente
              </Anchor>
            )}
          </Grid.Col>
          <Grid.Col span={{ base: 12, sm: 6, md: 4 }}>
            <Select
              label="Distribuidor"
              searchable
              clearable
              placeholder="Sin distribuidor"
              value={d.distribuidorId ? String(d.distribuidorId) : null}
              onChange={(v) => cambiar({ distribuidorId: v ? Number(v) : null })}
              data={distribuidores.map((x) => ({ value: String(x.id), label: x.nombre }))}
            />
          </Grid.Col>
          <Grid.Col span={{ base: 12, sm: 6, md: 3 }}>
            <TextInput
              label="Fecha"
              type="date"
              required
              value={d.fecha}
              min={anioFijo ? `${anioFijo}-01-01` : undefined}
              max={anioFijo ? `${anioFijo}-12-31` : undefined}
              description={anioFijo ? `Ejercicio ${anioFijo}` : 'El año de la fecha determina la numeración'}
              onChange={(e) => cambiar({ fecha: e.currentTarget.value })}
            />
          </Grid.Col>
        </Grid>
      </Paper>

      <Paper withBorder p="md">
        <Group justify="space-between" mb="sm">
          <Title order={4}>Líneas</Title>
          <Group gap="xs" wrap="nowrap">
            <Select
              w={280}
              searchable
              placeholder={d.distribuidorId ? 'Producto del distribuidor…' : 'Elige antes un distribuidor'}
              disabled={!d.distribuidorId}
              value={productoSel}
              onChange={setProductoSel}
              data={productos.map((p) => ({ value: String(p.id), label: `${p.nombre} · ${formatoEuros(p.precio)}` }))}
              nothingFoundMessage="Sin productos"
            />
            <Button variant="light" onClick={anadirProducto} disabled={!productoSel}>
              Añadir
            </Button>
            <Button
              variant="subtle"
              leftSection={<IconPlus size={16} />}
              onClick={() => cambiar({ lineas: [...d.lineas, { productoId: null, nombreProducto: '', cantidad: 1, precio: 0 }] })}
            >
              Línea libre
            </Button>
          </Group>
        </Group>
        <ScrollArea>
          <Table miw={640}>
            <Table.Thead>
              <Table.Tr>
                <Table.Th>Concepto</Table.Th>
                <Table.Th w={100}>Cantidad</Table.Th>
                <Table.Th w={140}>Precio</Table.Th>
                <Table.Th w={120} className="importe">
                  Importe
                </Table.Th>
                <Table.Th w={40} />
              </Table.Tr>
            </Table.Thead>
            <Table.Tbody>
              {d.lineas.map((l, i) => (
                <Table.Tr key={i}>
                  <Table.Td>
                    <TextInput
                      value={l.nombreProducto}
                      required
                      placeholder="Descripción"
                      onChange={(e) => setLinea(i, { nombreProducto: e.currentTarget.value })}
                    />
                  </Table.Td>
                  <Table.Td>
                    <Numero value={l.cantidad} onChange={(n) => setLinea(i, { cantidad: n })} />
                  </Table.Td>
                  <Table.Td>
                    <Numero euros value={l.precio} onChange={(n) => setLinea(i, { precio: n })} />
                  </Table.Td>
                  <Table.Td className="importe">{formatoEuros(importeLinea(l))}</Table.Td>
                  <Table.Td>
                    <ActionIcon
                      variant="subtle"
                      color="red"
                      aria-label="Quitar línea"
                      onClick={() => cambiar({ lineas: d.lineas.filter((_, j) => j !== i) })}
                    >
                      <IconTrash size={16} />
                    </ActionIcon>
                  </Table.Td>
                </Table.Tr>
              ))}
              {!d.lineas.length && (
                <Table.Tr>
                  <Table.Td colSpan={5}>
                    <Text c="dimmed" ta="center" size="sm">
                      Sin líneas. Añade productos del distribuidor o una línea libre.
                    </Text>
                  </Table.Td>
                </Table.Tr>
              )}
            </Table.Tbody>
            <Table.Tfoot>
              <Table.Tr>
                <Table.Th colSpan={3}>Subtotal líneas</Table.Th>
                <Table.Th className="importe">{formatoEuros(t.subtotalLineas)}</Table.Th>
                <Table.Th />
              </Table.Tr>
            </Table.Tfoot>
          </Table>
        </ScrollArea>
      </Paper>

      <Grid>
        <Grid.Col span={{ base: 12, md: 7 }}>
          <Stack>
            <Paper withBorder p="md">
              <Title order={4} mb="sm">
                Gastos y ganancia
              </Title>
              <SimpleGrid cols={{ base: 2, sm: 4 }}>
                <Numero euros label="Hotel" {...num('hotel')} />
                <Numero euros label="Pasaje" {...num('pasaje')} />
                <Numero euros label="Restaurante" {...num('restaurante')} />
                <Numero euros label="Combustible" {...num('combustible')} />
                <Numero euros label="Otros" {...num('otros')} />
                <Numero euros label="Transporte" {...num('transporte')} />
                <Numero euros label="Ganancia" {...num('ganancia')} />
                <Checkbox
                  mt={30}
                  label="+20% sobre ganancia"
                  checked={d.aplicaGanancia}
                  onChange={(e) => cambiar({ aplicaGanancia: e.currentTarget.checked })}
                />
              </SimpleGrid>
            </Paper>
            <Paper withBorder p="md">
              <Title order={4} mb="sm">
                Cálculo de viaje
              </Title>
              <SimpleGrid cols={{ base: 2, sm: 4 }}>
                <Numero label="Kilómetros" {...num('kilometros')} />
                <Numero label="Nº de viajes" decimalScale={0} {...num('numViajes')} />
                <Numero euros label="Precio gasolina (€/l)" decimalScale={3} {...num('precioGasolina')} />
                <Stack gap={4} justify="flex-end">
                  <Text size="sm">
                    Coste: <b>{formatoEuros(viaje)}</b>
                  </Text>
                  <Button size="xs" variant="light" onClick={() => cambiar({ combustible: viaje })}>
                    Pasar a combustible
                  </Button>
                </Stack>
              </SimpleGrid>
            </Paper>
            <Paper withBorder p="md">
              <Stack>
                <Textarea
                  label={`Concepto (texto principal del ${nombreTipo.toLowerCase()})`}
                  autosize
                  minRows={3}
                  value={d.textoConcepto}
                  onChange={(e) => cambiar({ textoConcepto: e.currentTarget.value })}
                />
                <Textarea
                  label="Forma de pago"
                  autosize
                  minRows={3}
                  value={d.textoFormaPago}
                  onChange={(e) => cambiar({ textoFormaPago: e.currentTarget.value })}
                />
                <Textarea
                  label="Explicación / observaciones"
                  autosize
                  minRows={2}
                  value={d.textoExplicativo}
                  onChange={(e) => cambiar({ textoExplicativo: e.currentTarget.value })}
                />
              </Stack>
            </Paper>
          </Stack>
        </Grid.Col>

        <Grid.Col span={{ base: 12, md: 5 }}>
          <Paper withBorder p="md" pos="sticky" top={76}>
            <Title order={4} mb="sm">
              Totales
            </Title>
            <Stack gap="xs">
              <Checkbox
                label="Usar total manual en lugar de la suma de líneas"
                checked={d.totalManualActivo}
                onChange={(e) => cambiar({ totalManualActivo: e.currentTarget.checked })}
              />
              {d.totalManualActivo && <Numero euros label="Total manual" {...num('totalManual')} />}
              <Table>
                <Table.Tbody>
                  <Fila etiqueta={d.totalManualActivo ? 'Importe (manual)' : 'Subtotal líneas'} valor={t.neto} />
                  <Fila etiqueta="Gastos" valor={t.totalGastos} />
                  <Fila etiqueta="Transporte" valor={d.transporte} />
                  <Fila etiqueta="Ganancia" valor={d.ganancia} />
                  {d.aplicaGanancia && <Fila etiqueta="+20% ganancia" valor={t.impuestoGanancia} />}
                  <Fila etiqueta="Base imponible" valor={t.totalSinIva} fuerte />
                </Table.Tbody>
              </Table>
              <Group gap="xs" align="flex-end" wrap="nowrap">
                <Checkbox label="Aplicar IVA" checked={d.aplicaIva} onChange={(e) => cambiar({ aplicaIva: e.currentTarget.checked })} mb={8} />
                <Numero w={100} suffix=" %" disabled={!d.aplicaIva} {...num('ivaPorcentaje')} />
                <Text className="importe" ml="auto" mb={8}>
                  {formatoEuros(t.totalIva)}
                </Text>
              </Group>
              <Divider />
              <Group justify="space-between">
                <Text fw={700} size="lg">
                  TOTAL
                </Text>
                <Text fw={700} size="xl" className="importe">
                  {formatoEuros(t.totalConIva)}
                </Text>
              </Group>
              <Divider label="Reparto de pagos" labelPosition="left" mt="sm" />
              <Slider
                value={d.porcentajeReparto}
                onChange={(v) => cambiar({ porcentajeReparto: v })}
                marks={[{ value: 25 }, { value: 50 }, { value: 75 }]}
                label={(v) => `${v}%`}
              />
              <Group justify="space-between">
                <Text size="sm">
                  {d.porcentajeReparto}%: <b>{formatoEuros(t.reparto[0])}</b>
                </Text>
                <Text size="sm">
                  {100 - d.porcentajeReparto}%: <b>{formatoEuros(t.reparto[1])}</b>
                </Text>
              </Group>
            </Stack>
          </Paper>
        </Grid.Col>
      </Grid>

      <CalculadoraEscalera
        abierta={calcAbierta}
        onClose={calc.close}
        valores={{ calcTipo: d.calcTipo, valorA: d.valorA, valorB: d.valorB, valorC: d.valorC, valorAux: d.valorAux }}
        onChange={(v) => cambiar(v)}
      />
    </Stack>
  );
}

function Fila({ etiqueta, valor, fuerte }: { etiqueta: string; valor: number; fuerte?: boolean }) {
  return (
    <Table.Tr>
      <Table.Td fw={fuerte ? 700 : undefined}>{etiqueta}</Table.Td>
      <Table.Td className="importe" fw={fuerte ? 700 : undefined}>
        {formatoEuros(valor)}
      </Table.Td>
    </Table.Tr>
  );
}
