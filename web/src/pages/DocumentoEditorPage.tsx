import { useEffect, useMemo, useRef, useState } from 'react';
import {
  ActionIcon,
  Alert,
  Anchor,
  Badge,
  Box,
  Button,
  Center,
  Checkbox,
  Divider,
  Grid,
  Group,
  Loader,
  NumberInput,
  Paper,
  Radio,
  Select,
  SimpleGrid,
  Slider,
  Stack,
  Table,
  Text,
  Textarea,
  TextInput,
  Title,
  Tooltip,
} from '@mantine/core';
import { useDebouncedValue, useDisclosure, useHotkeys } from '@mantine/hooks';
import { modals } from '@mantine/modals';
import {
  IconArrowRight,
  IconCalculator,
  IconDeviceFloppy,
  IconFileInvoice,
  IconPackage,
  IconPlus,
  IconPrinter,
  IconRefresh,
  IconTrash,
} from '@tabler/icons-react';
import { Link, useBlocker, useNavigate, useParams, useSearchParams } from 'react-router-dom';
import {
  aDolares,
  anioDeFecha,
  calcularTotales,
  calcularViaje,
  formatoDolares,
  formatoEuros,
  formatoFecha,
  hoyIso,
  importeLinea,
  indicePrincipal,
  numeroDocumento,
  type Documento,
  type DocumentoInput,
  type TipoDocumento,
} from '@cabp/shared';
import {
  notificarError,
  notificarOk,
  useAjustes,
  useBorrarDocumento,
  useClientes,
  useConvertirAFactura,
  useCrearPedidoFactura,
  useDistribuidores,
  useDocumento,
  useGuardarDocumento,
  useNumeroLibre,
  useProductos,
  useProximoNumero,
} from '../api/hooks';
import { ApiError, http } from '../api/http';
import { Numero } from '../components/Numero';
import { CalculadoraEscalera } from '../components/CalculadoraEscalera';
import { confirmar } from '../components/confirmar';
import { NombrePedidoModal } from '../components/NombrePedidoModal';

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
    consumo: 7,
    precioGasolina: 1,
    aplicaGanancia: true,
    aplicaIva: true,
    ivaPorcentaje: iva,
    totalManualActivo: false,
    totalManual: 0,
    porcentajeReparto: 50,
    mostrarDolares: false,
    tipoCambioUsd: 0,
    textoConcepto: '',
    textoFormaPago: formaPago,
    textoExplicativo: '',
    textoImportacion1: '',
    textoImportacion2: '',
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
    codigo: _c,
    presupuestoOrigenId: _p,
    facturaId: _f,
    pedido: _pe,
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

type Errores = Partial<Record<'clienteId' | 'numero' | 'pedidoNombre', string>>;
type Linea = DocumentoInput['lineas'][number];

function Editor({ doc, inicial }: { doc?: Documento; inicial: DocumentoInput }) {
  const [d, setD] = useState<DocumentoInput>(inicial);
  const [sucio, setSucioEstado] = useState(false);
  const [errores, setErrores] = useState<Errores>({});
  const [calcAbierta, calc] = useDisclosure();
  const [productoSel, setProductoSel] = useState<string | null>(null);
  const [enfocarLinea, setEnfocarLinea] = useState<number | null>(null);
  const [cotizacion, setCotizacion] = useState<{ cargando: boolean; fecha?: string }>({ cargando: false });
  const navigate = useNavigate();

  // El bloqueo de navegación lee el ref: así, al guardar y navegar en el mismo clic, ya no bloquea.
  const sucioRef = useRef(false);
  const setSucio = (v: boolean) => {
    sucioRef.current = v;
    setSucioEstado(v);
  };
  const clienteRef = useRef<HTMLInputElement>(null);
  const numeroRef = useRef<HTMLInputElement>(null);
  const pedidoRef = useRef<HTMLTextAreaElement>(null);
  const refsCampo = { clienteId: clienteRef, numero: numeroRef, pedidoNombre: pedidoRef };
  const conceptoRefs = useRef<(HTMLInputElement | null)[]>([]);
  const conceptoMovilRefs = useRef<(HTMLInputElement | null)[]>([]);

  const { data: clientes = [] } = useClientes('');
  const { data: distribuidores = [] } = useDistribuidores();
  const { data: productos = [] } = useProductos(d.distribuidorId);
  const guardar = useGuardarDocumento();
  const borrar = useBorrarDocumento();
  const convertir = useConvertirAFactura();
  const crearPedido = useCrearPedidoFactura();
  const [modalPedido, setModalPedido] = useState<'convertir' | 'crear' | null>(null);

  const t = useMemo(() => calcularTotales(d), [d]);
  const viaje = calcularViaje(d.kilometros, d.numViajes, d.consumo, d.precioGasolina);
  const esPresupuesto = d.tipo === 'presupuesto';
  const nombreTipo = esPresupuesto ? 'Presupuesto' : 'Factura';
  const anio = doc?.anio ?? (anioDeFecha(d.fecha) || new Date().getFullYear());
  const proximo = useProximoNumero(d.tipo, anio, true);

  // Comprobación en vivo del nº de factura elegido a mano.
  const [numeroEscrito] = useDebouncedValue(d.numero, 300);
  const numeroPropio = !esPresupuesto && d.numero != null && d.numero !== doc?.numero;
  const comprobacion = useNumeroLibre(d.tipo, anio, numeroPropio ? numeroEscrito : undefined, doc?.id);
  const numeroOcupado = numeroPropio && numeroEscrito === d.numero && comprobacion.data?.libre === false;
  const errorNumero = numeroOcupado
    ? `${comprobacion.data!.codigo} ya existe${proximo.data ? ` · siguiente libre: ${proximo.data.codigo}` : ''}`
    : errores.numero;

  // Solo se puede borrar la última factura del año (no deja huecos en la numeración).
  const esUltimaFactura = !doc || esPresupuesto || proximo.data?.numero === doc.numero + 1;

  // Aviso al cerrar o recargar la pestaña con cambios sin guardar...
  useEffect(() => {
    if (!sucio) return;
    const h = (e: BeforeUnloadEvent) => e.preventDefault();
    window.addEventListener('beforeunload', h);
    return () => window.removeEventListener('beforeunload', h);
  }, [sucio]);

  // ...y al navegar dentro de la aplicación (menú, enlaces).
  const blocker = useBlocker(({ currentLocation, nextLocation }) => sucioRef.current && currentLocation.pathname !== nextLocation.pathname);
  const blockerRef = useRef(blocker);
  blockerRef.current = blocker;
  useEffect(() => {
    if (blocker.state !== 'blocked') return;
    modals.openConfirmModal({
      title: 'Cambios sin guardar',
      children: <Text size="sm">Si sales ahora se perderán los cambios de {doc ? doc.codigo : `este ${nombreTipo.toLowerCase()} nuevo`}.</Text>,
      labels: { confirm: 'Salir sin guardar', cancel: 'Seguir editando' },
      confirmProps: { color: 'red' },
      onConfirm: () => blockerRef.current.proceed?.(),
      onClose: () => {
        if (blockerRef.current.state === 'blocked') blockerRef.current.reset?.();
      },
    });
  }, [blocker.state]); // eslint-disable-line react-hooks/exhaustive-deps

  // Lleva el foco al concepto de la línea recién añadida (en la tabla o en la vista móvil, la que se vea).
  useEffect(() => {
    if (enfocarLinea == null) return;
    const candidatos = [conceptoRefs.current[enfocarLinea], conceptoMovilRefs.current[enfocarLinea]];
    candidatos.find((el) => el && el.offsetParent !== null)?.focus();
    setEnfocarLinea(null);
  }, [enfocarLinea]);

  const cambiar = (parcial: Partial<DocumentoInput>) => {
    setD((prev) => ({ ...prev, ...parcial }));
    setSucio(true);
    const claves = Object.keys(parcial).filter((k) => k in errores);
    if (claves.length)
      setErrores((prev) => {
        const n = { ...prev };
        for (const k of claves) delete n[k as keyof Errores];
        return n;
      });
  };
  const num = (k: keyof DocumentoInput) => ({
    value: d[k] as number,
    onChange: (n: number) => cambiar({ [k]: n } as Partial<DocumentoInput>),
  });
  // Precarga la cotización del BCE para la fecha del documento; se puede corregir a mano.
  const cargarCotizacion = async () => {
    setCotizacion({ cargando: true });
    try {
      const c = await http.get<{ tipoCambio: number; fecha: string }>(`/documentos/cotizacion-usd?fecha=${d.fecha}`);
      cambiar({ tipoCambioUsd: Math.round(c.tipoCambio * 10000) / 10000 });
      setCotizacion({ cargando: false, fecha: c.fecha });
    } catch (e) {
      notificarError(e);
      setCotizacion({ cargando: false });
    }
  };
  const activarDolares = (activo: boolean) => {
    cambiar({ mostrarDolares: activo });
    if (activo && !d.tipoCambioUsd) void cargarCotizacion();
  };
  const setLinea = (i: number, parcial: Partial<Linea>) =>
    cambiar({ lineas: d.lineas.map((l, j) => (j === i ? { ...l, ...parcial } : l)) });
  const quitarLinea = (i: number) => cambiar({ lineas: d.lineas.filter((_, j) => j !== i) });
  const marcarPrincipal = (i: number) => cambiar({ lineas: d.lineas.map((l, j) => ({ ...l, principal: j === i })) });
  const principal = indicePrincipal(d.lineas);
  const lineaLibre = () => {
    cambiar({ lineas: [...d.lineas, { productoId: null, nombreProducto: '', cantidad: 1, precio: 0, principal: false }] });
    setEnfocarLinea(d.lineas.length);
  };

  const anadirProducto = () => {
    const p = productos.find((x) => String(x.id) === productoSel);
    if (!p) return;
    const i = d.lineas.findIndex((l) => l.productoId === p.id);
    if (i >= 0) setLinea(i, { cantidad: d.lineas[i].cantidad + 1 });
    else cambiar({ lineas: [...d.lineas, { productoId: p.id, nombreProducto: p.nombre, cantidad: 1, precio: p.precio, principal: false }] });
    setProductoSel(null);
  };

  const marcarError = (campo: keyof Errores, mensaje: string) => {
    setErrores((prev) => ({ ...prev, [campo]: mensaje }));
    refsCampo[campo].current?.focus();
  };

  const onGuardar = (despues?: (doc: Documento) => void) => {
    if (!d.clienteId) return marcarError('clienteId', 'Elige el cliente');
    if (numeroOcupado) return numeroRef.current?.focus();
    if (!doc && !esPresupuesto && !d.pedidoNombre?.trim()) return marcarError('pedidoNombre', 'Indica el nombre del pedido');
    guardar.mutate(
      { id: doc?.id, datos: d },
      {
        onSuccess: (g) => {
          setSucio(false);
          notificarOk(`${nombreTipo} ${g.codigo} ${esPresupuesto ? 'guardado' : 'guardada'}`);
          if (despues) despues(g);
          else if (!doc) navigate(`/documentos/${g.id}`, { replace: true });
        },
        onError: (e) => {
          if (e instanceof ApiError && e.campo && e.campo in refsCampo) marcarError(e.campo as keyof Errores, e.message);
        },
      },
    );
  };

  useHotkeys([['mod+S', () => onGuardar()]], []);

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
        : `¿Borrar la factura ${doc.codigo}? No se puede deshacer; la numeración del año ${doc.anio} vuelve a la anterior.`,
      () =>
        borrar.mutate(doc.id, {
          onSuccess: () => {
            setSucio(false);
            notificarOk(`${nombreTipo} ${doc.codigo} ${esPresupuesto ? 'borrado' : 'borrada'}`);
            navigate(esPresupuesto ? '/presupuestos' : '/facturas');
          },
        }),
    );

  // La factura se crea en el servidor a partir del presupuesto guardado: si hay cambios sin guardar
  // (gastos, observaciones…), se guardan antes para que pasen a la factura.
  const convertirConPedido = (pedidoNombre: string) => {
    if (!doc) return;
    const crearFactura = () =>
      convertir.mutate(
        { id: doc.id, pedidoNombre },
        {
          onSuccess: (f) => {
            setModalPedido(null);
            notificarOk(`Factura ${f.codigo} creada`);
            navigate(`/documentos/${f.id}`);
          },
        },
      );
    if (sucio) onGuardar(crearFactura);
    else crearFactura();
  };

  const crearPedidoFactura = (nombre: string) =>
    doc &&
    crearPedido.mutate(
      { facturaId: doc.id, nombre },
      {
        onSuccess: (f) => {
          setModalPedido(null);
          notificarOk('Pedido creado');
          if (f.pedido) navigate(`/pedidos/${f.pedido.id}`);
        },
      },
    );

  const anioFijo = doc?.anio;
  const codigoActual = doc
    ? doc.numero === d.numero || d.numero == null
      ? doc.codigo
      : `${doc.codigo} → ${numeroDocumento(d.tipo, doc.anio, d.numero)}`
    : d.numero
      ? `(nuevo · ${numeroDocumento(d.tipo, anio, d.numero)})`
      : proximo.data
        ? `(nuevo · ${proximo.data.codigo})`
        : '(nuevo)';
  // Las descripciones van debajo del campo para que todos los inputs de la fila queden alineados.
  const ordenCampo: ('label' | 'input' | 'description' | 'error')[] = ['label', 'input', 'description', 'error'];

  const botonGuardar = (
    <Button
      leftSection={<IconDeviceFloppy size={16} />}
      onClick={() => onGuardar()}
      loading={guardar.isPending}
      disabled={!d.clienteId}
      title="Guardar (Ctrl+S)"
    >
      Guardar
    </Button>
  );

  return (
    <Stack>
      <Group justify="space-between" gap="sm">
        <Group gap="sm">
          <Title order={2}>
            {nombreTipo} {codigoActual}
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
            <Button leftSection={<IconFileInvoice size={16} />} variant="light" onClick={() => setModalPedido('convertir')}>
              Convertir a factura
            </Button>
          )}
          {doc && (
            <Tooltip
              label={
                esUltimaFactura
                  ? `Borrar ${doc.codigo}`
                  : `Solo se puede borrar la última factura de ${doc.anio}${proximo.data ? ` (${numeroDocumento('factura', doc.anio, proximo.data.numero - 1)})` : ''}`
              }
            >
              <ActionIcon
                size="lg"
                variant="subtle"
                color="red"
                onClick={esUltimaFactura ? pedirBorrado : undefined}
                data-disabled={!esUltimaFactura || undefined}
                aria-disabled={!esUltimaFactura}
                aria-label={esUltimaFactura ? `Borrar ${doc.codigo}` : `${doc.codigo}: solo se puede borrar la última factura del año`}
              >
                <IconTrash size={18} />
              </ActionIcon>
            </Tooltip>
          )}
          {botonGuardar}
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
      {doc && !esPresupuesto && (
        <Alert variant="default" p="xs" icon={<IconPackage size={18} />}>
          {doc.pedido ? (
            <>
              Pedido:{' '}
              <Anchor component={Link} to={`/pedidos/${doc.pedido.id}`} fw={500}>
                {doc.pedido.nombre}
              </Anchor>
            </>
          ) : (
            <Group justify="space-between" gap="xs">
              <span>Esta factura no tiene pedido.</span>
              <Button size="compact-sm" variant="light" onClick={() => setModalPedido('crear')}>
                Crear pedido
              </Button>
            </Group>
          )}
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
          <Grid.Col span={{ base: 12, md: esPresupuesto ? 5 : 4 }}>
            <Select
              ref={clienteRef}
              label="Cliente"
              required
              searchable
              placeholder="Buscar cliente…"
              value={d.clienteId ? String(d.clienteId) : null}
              onChange={(v) => cambiar({ clienteId: Number(v) || 0 })}
              data={clientes.map((c) => ({ value: String(c.id), label: `${c.nombre} ${c.apellidos}${c.dni ? ` · ${c.dni}` : ''}` }))}
              nothingFoundMessage="Sin resultados"
              error={errores.clienteId}
            />
            {d.clienteId > 0 && (
              <Anchor component={Link} to={`/clientes/${d.clienteId}`} size="xs">
                Ver ficha del cliente
              </Anchor>
            )}
          </Grid.Col>
          <Grid.Col span={{ base: 12, sm: 6, md: esPresupuesto ? 4 : 3 }}>
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
              inputWrapperOrder={ordenCampo}
              description={anioFijo ? `Ejercicio ${anioFijo}` : 'El año fija la numeración'}
              onChange={(e) => cambiar({ fecha: e.currentTarget.value })}
            />
          </Grid.Col>
          {!esPresupuesto && (
            <Grid.Col span={{ base: 12, sm: 6, md: 2 }}>
              <NumberInput
                ref={numeroRef}
                label="Nº de factura"
                inputWrapperOrder={ordenCampo}
                description={doc || errorNumero ? undefined : 'Vacío = automático'}
                placeholder={proximo.data ? String(proximo.data.numero) : 'Automático'}
                allowDecimal={false}
                allowNegative={false}
                hideControls
                min={1}
                max={999999}
                required={!!doc}
                value={d.numero ?? ''}
                onChange={(v) => cambiar({ numero: typeof v === 'number' && v > 0 ? v : undefined })}
                error={errorNumero}
              />
            </Grid.Col>
          )}
          {!doc && !esPresupuesto && (
            <Grid.Col span={12}>
              <Textarea
                ref={pedidoRef}
                label="Nombre del pedido"
                description="Cada factura abre un pedido del cliente, donde después se suben sus ficheros. Por ejemplo: «Silla salvaescaleras · C/ Mayor 12»."
                required
                autosize
                minRows={1}
                maxLength={1000}
                value={d.pedidoNombre ?? ''}
                onChange={(e) => cambiar({ pedidoNombre: e.currentTarget.value })}
                error={errores.pedidoNombre}
              />
            </Grid.Col>
          )}
        </Grid>
      </Paper>

      <Paper withBorder p="md">
        <Group justify="space-between" mb="sm" gap="sm">
          <Title order={3} size="h4">
            Líneas
          </Title>
          <Group gap="xs" w={{ base: '100%', sm: 'auto' }}>
            <Select
              flex={1}
              miw={220}
              searchable
              aria-label="Producto del distribuidor"
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
            <Button variant="subtle" leftSection={<IconPlus size={16} />} onClick={lineaLibre}>
              Línea libre
            </Button>
          </Group>
        </Group>

        {/* Escritorio y tableta: tabla */}
        <Box visibleFrom="sm">
          <Table>
            <Table.Thead>
              <Table.Tr>
                <Table.Th w={70}>Principal</Table.Th>
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
                    <Center>
                      <Radio
                        name="linea-principal"
                        aria-label={`Línea ${i + 1} principal`}
                        checked={i === principal}
                        onChange={() => marcarPrincipal(i)}
                      />
                    </Center>
                  </Table.Td>
                  <Table.Td>
                    <TextInput
                      ref={(el) => {
                        conceptoRefs.current[i] = el;
                      }}
                      value={l.nombreProducto}
                      required
                      aria-label={`Concepto, línea ${i + 1}`}
                      placeholder="Descripción"
                      onChange={(e) => setLinea(i, { nombreProducto: e.currentTarget.value })}
                    />
                  </Table.Td>
                  <Table.Td>
                    <Numero aria-label={`Cantidad, línea ${i + 1}`} value={l.cantidad} onChange={(n) => setLinea(i, { cantidad: n })} />
                  </Table.Td>
                  <Table.Td>
                    <Numero euros aria-label={`Precio, línea ${i + 1}`} value={l.precio} onChange={(n) => setLinea(i, { precio: n })} />
                  </Table.Td>
                  <Table.Td className="importe">{formatoEuros(importeLinea(l))}</Table.Td>
                  <Table.Td>
                    <ActionIcon variant="subtle" color="red" aria-label={`Quitar línea ${i + 1}`} onClick={() => quitarLinea(i)}>
                      <IconTrash size={16} />
                    </ActionIcon>
                  </Table.Td>
                </Table.Tr>
              ))}
              {!d.lineas.length && (
                <Table.Tr>
                  <Table.Td colSpan={6}>
                    <Text c="dimmed" ta="center" size="sm">
                      Sin líneas. Añade productos del distribuidor o una línea libre.
                    </Text>
                  </Table.Td>
                </Table.Tr>
              )}
            </Table.Tbody>
            <Table.Tfoot>
              <Table.Tr>
                <Table.Th colSpan={4}>Subtotal líneas</Table.Th>
                <Table.Th className="importe">{formatoEuros(t.subtotalLineas)}</Table.Th>
                <Table.Th />
              </Table.Tr>
            </Table.Tfoot>
          </Table>
        </Box>

        {/* Móvil: una ficha por línea */}
        <Stack hiddenFrom="sm" gap="sm">
          {d.lineas.map((l, i) => (
            <Paper key={i} withBorder p="sm" bg="gray.0">
              <Stack gap="xs">
                <TextInput
                  ref={(el) => {
                    conceptoMovilRefs.current[i] = el;
                  }}
                  label={`Concepto ${i + 1}`}
                  value={l.nombreProducto}
                  required
                  placeholder="Descripción"
                  onChange={(e) => setLinea(i, { nombreProducto: e.currentTarget.value })}
                />
                <Group grow gap="xs">
                  <Numero label="Cantidad" value={l.cantidad} onChange={(n) => setLinea(i, { cantidad: n })} />
                  <Numero euros label="Precio" value={l.precio} onChange={(n) => setLinea(i, { precio: n })} />
                </Group>
                <Radio
                  name="linea-principal-movil"
                  label="Línea principal"
                  checked={i === principal}
                  onChange={() => marcarPrincipal(i)}
                />
                <Group justify="space-between">
                  <Text size="sm">
                    Importe: <b className="importe">{formatoEuros(importeLinea(l))}</b>
                  </Text>
                  <Button size="compact-sm" variant="subtle" color="red" leftSection={<IconTrash size={14} />} onClick={() => quitarLinea(i)}>
                    Quitar
                  </Button>
                </Group>
              </Stack>
            </Paper>
          ))}
          {!d.lineas.length && (
            <Text c="dimmed" ta="center" size="sm">
              Sin líneas. Añade productos del distribuidor o una línea libre.
            </Text>
          )}
          <Group justify="space-between" px="xs">
            <Text fw={700} size="sm">
              Subtotal líneas
            </Text>
            <Text fw={700} size="sm" className="importe">
              {formatoEuros(t.subtotalLineas)}
            </Text>
          </Group>
        </Stack>
      </Paper>

      <Grid>
        <Grid.Col span={{ base: 12, md: 7 }}>
          <Stack>
            <Paper withBorder p="md">
              <Title order={3} size="h4" mb="sm">
                Gastos y ganancia
              </Title>
              <Text size="sm" c="dimmed" mb="sm">
                {d.totalManualActivo
                  ? 'En el PDF se suman al total manual, en una sola línea.'
                  : principal >= 0
                    ? `En el PDF se reparten en el precio unidad de la línea principal (${d.lineas[principal].nombreProducto || `línea ${principal + 1}`}) y se suman a su total, sin crear otra línea.`
                    : 'Sin líneas: en el PDF aparecerán como una línea aparte.'}
                {t.totalSinIva !== t.neto && <> Ahora suman <b className="importe">{formatoEuros(Math.round((t.totalSinIva - t.neto) * 100) / 100)}</b>.</>}
              </Text>
              <SimpleGrid cols={{ base: 2, sm: 4 }}>
                <Numero euros label="Hotel" {...num('hotel')} />
                <Numero euros label="Pasaje" {...num('pasaje')} />
                <Numero euros label="Restaurante" {...num('restaurante')} />
                <Numero euros label="Combustible" {...num('combustible')} />
                <Numero euros label="Otros" {...num('otros')} />
                <Numero euros label="Transporte" {...num('transporte')} />
                <Numero euros label="Ganancia" {...num('ganancia')} />
              </SimpleGrid>
              <Checkbox
                mt="md"
                label="Recargo del 20 % sobre la ganancia"
                checked={d.aplicaGanancia}
                onChange={(e) => cambiar({ aplicaGanancia: e.currentTarget.checked })}
              />
            </Paper>
            <Paper withBorder p="md">
              <Title order={3} size="h4" mb="sm">
                Cálculo de viaje
              </Title>
              <SimpleGrid cols={{ base: 2, lg: 4 }}>
                <Numero label="Kilómetros" {...num('kilometros')} />
                <Numero label="Nº de viajes" decimalScale={0} {...num('numViajes')} />
                <Numero label="Consumo (l/100 km)" decimalScale={2} {...num('consumo')} />
                <Numero euros label="Gasolina (€/l)" decimalScale={3} {...num('precioGasolina')} />
              </SimpleGrid>
              <Group justify="space-between" mt="md" gap="sm">
                <Text size="sm">
                  Coste estimado: <b className="importe">{formatoEuros(viaje)}</b>
                </Text>
                <Button size="xs" variant="default" onClick={() => cambiar({ combustible: viaje })}>
                  Pasar a combustible
                </Button>
              </Group>
            </Paper>
            <Paper withBorder p="md">
              <Stack>
                <Textarea
                  label={`Concepto (texto principal ${esPresupuesto ? 'del presupuesto' : 'de la factura'})`}
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
            <Title order={3} size="h4" mb="sm">
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
                  {d.aplicaGanancia && <Fila etiqueta="Recargo 20 % ganancia" valor={t.impuestoGanancia} />}
                  <Fila etiqueta="Base imponible" valor={t.totalSinIva} fuerte />
                </Table.Tbody>
              </Table>
              <Group gap="xs" align="flex-end" wrap="nowrap">
                <Checkbox label="Aplicar IVA" checked={d.aplicaIva} onChange={(e) => cambiar({ aplicaIva: e.currentTarget.checked })} mb={8} />
                <Numero w={100} suffix=" %" aria-label="Porcentaje de IVA" disabled={!d.aplicaIva} {...num('ivaPorcentaje')} />
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
              <Checkbox
                label="Mostrar también el total en dólares (US$)"
                checked={d.mostrarDolares}
                onChange={(e) => activarDolares(e.currentTarget.checked)}
              />
              {d.mostrarDolares && (
                <>
                  <Group gap="xs" align="flex-end" wrap="nowrap">
                    <Numero
                      w={150}
                      label="1 € ="
                      suffix=" US$"
                      decimalScale={4}
                      disabled={cotizacion.cargando}
                      error={!cotizacion.cargando && !(d.tipoCambioUsd > 0) ? 'Indica la cotización' : undefined}
                      value={d.tipoCambioUsd}
                      onChange={(n) => {
                        cambiar({ tipoCambioUsd: n });
                        setCotizacion({ cargando: false });
                      }}
                    />
                    <Tooltip label="Cargar la cotización del BCE para la fecha del documento">
                      <ActionIcon
                        variant="default"
                        size="input-sm"
                        aria-label="Cargar cotización"
                        loading={cotizacion.cargando}
                        onClick={cargarCotizacion}
                        mb={!cotizacion.cargando && !(d.tipoCambioUsd > 0) ? 22 : 0}
                      >
                        <IconRefresh size={16} />
                      </ActionIcon>
                    </Tooltip>
                    <Text fw={700} className="importe" ml="auto" mb={8}>
                      {formatoDolares(aDolares(t.totalConIva, d.tipoCambioUsd))}
                    </Text>
                  </Group>
                  {cotizacion.fecha && (
                    <Text size="xs" c="dimmed">
                      Cotización del BCE del {formatoFecha(cotizacion.fecha)}. Corrígela si no es la que quieres usar.
                    </Text>
                  )}
                </>
              )}
              <Divider label="Reparto de pagos" labelPosition="left" mt="sm" />
              <Slider
                value={d.porcentajeReparto}
                onChange={(v) => cambiar({ porcentajeReparto: v })}
                marks={[{ value: 25 }, { value: 50 }, { value: 75 }]}
                label={(v) => `${v}%`}
                thumbLabel="Porcentaje del primer pago"
              />
              <Group justify="space-between">
                <Text size="sm">
                  {d.porcentajeReparto}%: <b className="importe">{formatoEuros(t.reparto[0])}</b>
                </Text>
                <Text size="sm">
                  {100 - d.porcentajeReparto}%: <b className="importe">{formatoEuros(t.reparto[1])}</b>
                </Text>
              </Group>
            </Stack>
          </Paper>
        </Grid.Col>
      </Grid>

      <Paper withBorder p="md">
        <Title order={3} size="h4">
          Texto de importación (dos columnas)
        </Title>
        <Text size="sm" c="dimmed" mb="sm">
          Se imprime en dos columnas paralelas debajo de los totales, p. ej. español a la izquierda e inglés a la derecha.
          Los saltos de línea se respetan, así puedes alinear las dos columnas.
        </Text>
        <SimpleGrid cols={{ base: 1, sm: 2 }}>
          <Textarea
            label="Columna importación 1"
            autosize
            minRows={6}
            value={d.textoImportacion1}
            onChange={(e) => cambiar({ textoImportacion1: e.currentTarget.value })}
          />
          <Textarea
            label="Columna importación 2"
            autosize
            minRows={6}
            value={d.textoImportacion2}
            onChange={(e) => cambiar({ textoImportacion2: e.currentTarget.value })}
          />
        </SimpleGrid>
      </Paper>

      {/* Móvil: total y guardar siempre a mano */}
      <Box h={56} hiddenFrom="md" />
      <Box
        hiddenFrom="md"
        pos="fixed"
        bottom={0}
        left={0}
        right={0}
        px="md"
        py="xs"
        bg="var(--mantine-color-body)"
        style={{ borderTop: '1px solid var(--mantine-color-gray-3)', zIndex: 150 }}
      >
        <Group justify="space-between" wrap="nowrap">
          <div>
            <Text size="xs" c="dimmed" lh={1.2}>
              TOTAL{sucio ? ' · sin guardar' : ''}
            </Text>
            <Text fw={700} size="lg" className="importe" lh={1.2}>
              {formatoEuros(t.totalConIva)}
            </Text>
          </div>
          {botonGuardar}
        </Group>
      </Box>

      <CalculadoraEscalera
        abierta={calcAbierta}
        onClose={calc.close}
        valores={{ calcTipo: d.calcTipo, valorA: d.valorA, valorB: d.valorB, valorC: d.valorC, valorAux: d.valorAux }}
        onChange={(v) => cambiar(v)}
      />

      <NombrePedidoModal
        abierto={modalPedido !== null}
        titulo={modalPedido === 'convertir' ? 'Convertir a factura' : 'Crear pedido'}
        explicacion={
          modalPedido === 'convertir'
            ? `Se creará una factura con fecha de hoy copiando el presupuesto ${doc?.codigo}, y con ella su pedido.${sucio ? ' Antes se guardarán los cambios del presupuesto.' : ''}`
            : `El pedido será del cliente de la factura ${doc?.codigo}.`
        }
        inicial={modalPedido === 'convertir' ? (doc?.textoConcepto.split('\n')[0] ?? '').trim() : ''}
        etiquetaBoton={modalPedido === 'convertir' ? 'Convertir' : 'Crear pedido'}
        cargando={convertir.isPending || crearPedido.isPending || guardar.isPending}
        onCerrar={() => setModalPedido(null)}
        onAceptar={modalPedido === 'convertir' ? convertirConPedido : crearPedidoFactura}
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
