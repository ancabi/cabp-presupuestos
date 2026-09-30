import { useState } from 'react';
import { Button, Paper, PasswordInput, Stack, Title } from '@mantine/core';
import { http } from '../api/http';
import { notificarError, notificarOk } from '../api/hooks';

export function CuentaPage() {
  const [actual, setActual] = useState('');
  const [nueva, setNueva] = useState('');
  const [repetir, setRepetir] = useState('');
  const [cargando, setCargando] = useState(false);

  const enviar = async (e: React.FormEvent) => {
    e.preventDefault();
    if (nueva !== repetir) return notificarError(new Error('Las contraseñas no coinciden'));
    setCargando(true);
    try {
      await http.post('/auth/password', { actual, nueva });
      notificarOk('Contraseña cambiada');
      setActual('');
      setNueva('');
      setRepetir('');
    } catch (err) {
      notificarError(err);
    } finally {
      setCargando(false);
    }
  };

  return (
    <Paper withBorder p="md" maw={420}>
      <form onSubmit={enviar}>
        <Stack>
          <Title order={3}>Cambiar contraseña</Title>
          <PasswordInput label="Contraseña actual" required autoComplete="current-password" value={actual} onChange={(e) => setActual(e.currentTarget.value)} />
          <PasswordInput label="Nueva contraseña" required minLength={8} autoComplete="new-password" value={nueva} onChange={(e) => setNueva(e.currentTarget.value)} />
          <PasswordInput label="Repite la nueva contraseña" required autoComplete="new-password" value={repetir} onChange={(e) => setRepetir(e.currentTarget.value)} />
          <Button type="submit" loading={cargando}>
            Guardar
          </Button>
        </Stack>
      </form>
    </Paper>
  );
}
