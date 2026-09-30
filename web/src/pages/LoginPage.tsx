import { useState } from 'react';
import { Alert, Button, Center, Image, Paper, PasswordInput, Stack, TextInput, Title } from '@mantine/core';
import { useLocation, useNavigate } from 'react-router-dom';
import { useQueryClient } from '@tanstack/react-query';
import type { Usuario } from '@cabp/shared';
import { http } from '../api/http';

export function LoginPage() {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState<string | null>(null);
  const [cargando, setCargando] = useState(false);
  const navigate = useNavigate();
  const location = useLocation();
  const qc = useQueryClient();

  const entrar = async (e: React.FormEvent) => {
    e.preventDefault();
    setError(null);
    setCargando(true);
    try {
      const u = await http.post<Usuario>('/auth/login', { email, password });
      qc.setQueryData(['yo'], u);
      navigate((location.state as { desde?: string } | null)?.desde ?? '/', { replace: true });
    } catch (err) {
      setError(err instanceof Error ? err.message : 'No se pudo iniciar sesión');
    } finally {
      setCargando(false);
    }
  };

  return (
    <Center mih="100vh" bg="gray.0" p="md">
      <Paper withBorder shadow="sm" p="xl" w={380} maw="100%">
        <form onSubmit={entrar}>
          <Stack>
            <Center>
              <Image src="/logo.png" h={72} w="auto" alt="CABP" />
            </Center>
            <Title order={3} ta="center">
              Presupuestos y facturas
            </Title>
            {error && <Alert color="red">{error}</Alert>}
            <TextInput label="Email" type="email" autoComplete="username" required value={email} onChange={(e) => setEmail(e.currentTarget.value)} />
            <PasswordInput
              label="Contraseña"
              autoComplete="current-password"
              required
              value={password}
              onChange={(e) => setPassword(e.currentTarget.value)}
            />
            <Button type="submit" loading={cargando}>
              Entrar
            </Button>
          </Stack>
        </form>
      </Paper>
    </Center>
  );
}
