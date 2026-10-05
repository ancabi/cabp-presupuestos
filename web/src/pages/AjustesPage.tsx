import { useEffect, useState } from 'react';
import {
  Button,
  Center,
  ColorInput,
  Grid,
  Group,
  Input,
  Loader,
  Paper,
  Select,
  SimpleGrid,
  Slider,
  Stack,
  Text,
  Textarea,
  TextInput,
  Title,
} from '@mantine/core';
import { FUENTES_PDF, PDF_POR_DEFECTO, type Ajustes, type AspectoPdf } from '@cabp/shared';
import { notificarOk, useAjustes, useGuardarAjustes, useYo } from '../api/hooks';
import { Numero } from '../components/Numero';
import { VistaPreviaPdf } from '../components/VistaPreviaPdf';
import { TiposFicheroAjustes } from '../components/TiposFicheroAjustes';

const MUESTRAS_COLOR = ['#0c8599', '#1c5fa8', '#2b8a3e', '#8b1e3f', '#c2410c', '#495057', '#222222', '#000000'];

export function AjustesPage() {
  const { data } = useAjustes();
  const { data: yo } = useYo();
  const guardar = useGuardarAjustes();
  const [a, setA] = useState<Ajustes | null>(null);
  useEffect(() => {
    if (data) setA(data);
  }, [data]);

  if (yo?.rol !== 'admin') return <Text>Solo los administradores pueden cambiar los ajustes.</Text>;
  if (!a)
    return (
      <Center p="xl">
        <Loader />
      </Center>
    );

  const emp = (k: keyof Ajustes['empresa'], label: string) => (
    <TextInput label={label} value={a.empresa[k]} onChange={(e) => setA({ ...a, empresa: { ...a.empresa, [k]: e.currentTarget.value } })} />
  );

  const pdf = (cambios: Partial<AspectoPdf>) => setA({ ...a, pdf: { ...a.pdf, ...cambios } });

  return (
    <Stack>
      <form
        onSubmit={(e) => {
          e.preventDefault();
          guardar.mutate(a, { onSuccess: () => notificarOk('Ajustes guardados') });
        }}
      >
        <Stack>
          <Title order={2}>Ajustes</Title>
          <Paper withBorder p="md">
            <Title order={4} mb="sm">
              IVA
            </Title>
            <Numero
              w={200}
              label="IVA por defecto (%)"
              description="Se aplica a los documentos nuevos. Cada documento guarda el suyo."
              suffix=" %"
              value={a.ivaPorcentaje}
              onChange={(n) => setA({ ...a, ivaPorcentaje: n })}
            />
          </Paper>
          <Paper withBorder p="md">
            <Title order={4} mb="sm">
              Datos de la empresa (cabecera de presupuestos y facturas)
            </Title>
            <SimpleGrid cols={{ base: 1, sm: 2 }}>
              {emp('nombre', 'Nombre comercial')}
              {emp('titular', 'Titular')}
              {emp('nif', 'NIF')}
              {emp('direccion', 'Dirección')}
              {emp('telefonos', 'Teléfonos')}
              {emp('web', 'Web')}
              {emp('emails', 'Emails')}
              {emp('logoUrl', 'URL del logo')}
            </SimpleGrid>
            <Textarea
              mt="sm"
              label="Datos bancarios"
              autosize
              minRows={2}
              value={a.empresa.cuentaBancaria}
              onChange={(e) => setA({ ...a, empresa: { ...a.empresa, cuentaBancaria: e.currentTarget.value } })}
            />
          </Paper>
          <Paper withBorder p="md">
            <Group justify="space-between" mb="sm">
              <Title order={4}>Aspecto del PDF</Title>
              <Button variant="subtle" size="xs" onClick={() => setA({ ...a, pdf: PDF_POR_DEFECTO })}>
                Restablecer aspecto por defecto
              </Button>
            </Group>
            <Grid>
              <Grid.Col span={{ base: 12, md: 4 }}>
                <Stack>
                  <Select
                    label="Tipo de letra"
                    value={a.pdf.fuente}
                    onChange={(v) => v && pdf({ fuente: v as AspectoPdf['fuente'] })}
                    allowDeselect={false}
                    data={FUENTES_PDF.map((f) => ({ value: f.value, label: f.label }))}
                    renderOption={({ option }) => (
                      <span style={{ fontFamily: FUENTES_PDF.find((f) => f.value === option.value)?.css }}>{option.label}</span>
                    )}
                  />
                  <Numero
                    label="Tamaño de letra"
                    description="Tamaño del texto normal; títulos y totales crecen en proporción."
                    suffix=" pt"
                    decimalScale={1}
                    step={0.5}
                    min={6}
                    max={14}
                    hideControls={false}
                    value={a.pdf.tamanoLetra}
                    onChange={(n) => pdf({ tamanoLetra: n })}
                  />
                  <ColorInput
                    label="Color principal"
                    description="Títulos, nombre de la empresa, cabecera de la tabla y líneas."
                    format="hex"
                    swatches={MUESTRAS_COLOR}
                    value={a.pdf.colorPrincipal}
                    onChange={(v) => pdf({ colorPrincipal: v })}
                  />
                  <ColorInput
                    label="Color del texto"
                    format="hex"
                    swatches={MUESTRAS_COLOR}
                    value={a.pdf.colorTexto}
                    onChange={(v) => pdf({ colorTexto: v })}
                  />
                  <Input.Wrapper label={`Tamaño del logo: ${a.pdf.tamanoLogo} px`}>
                    <Slider
                      mt="xs"
                      min={16}
                      max={160}
                      step={2}
                      value={a.pdf.tamanoLogo}
                      onChange={(n) => pdf({ tamanoLogo: n })}
                      label={(n) => `${n} px`}
                      thumbLabel="Tamaño del logo"
                    />
                  </Input.Wrapper>
                </Stack>
              </Grid.Col>
              <Grid.Col span={{ base: 12, md: 8 }}>
                <Text size="sm" fw={500} mb={4}>
                  Vista previa
                </Text>
                <VistaPreviaPdf ajustes={a} />
              </Grid.Col>
            </Grid>
          </Paper>
          <Paper withBorder p="md">
            <Title order={4} mb="sm">
              Textos
            </Title>
            <Stack>
              <Textarea
                label="Forma de pago por defecto"
                description="Se copia en cada documento nuevo y se puede cambiar en él."
                autosize
                minRows={3}
                value={a.textoFormaPagoDefecto}
                onChange={(e) => setA({ ...a, textoFormaPagoDefecto: e.currentTarget.value })}
              />
              <Textarea
                label="Condiciones (pie de página)"
                autosize
                minRows={3}
                value={a.textoCondiciones}
                onChange={(e) => setA({ ...a, textoCondiciones: e.currentTarget.value })}
              />
            </Stack>
          </Paper>
          <Group justify="flex-end">
            <Button type="submit" loading={guardar.isPending}>
              Guardar ajustes
            </Button>
          </Group>
        </Stack>
      </form>
      <TiposFicheroAjustes />
    </Stack>
  );
}
