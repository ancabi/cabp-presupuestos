import { useEffect } from 'react';
import { Center, Loader } from '@mantine/core';
import { Navigate, Outlet, useLocation, useNavigate } from 'react-router-dom';
import { useQueryClient } from '@tanstack/react-query';
import { useYo } from '../api/hooks';

export function RequiereSesion() {
  const { data, isLoading, isError } = useYo();
  const location = useLocation();
  const navigate = useNavigate();
  const qc = useQueryClient();

  useEffect(() => {
    const salir = () => {
      qc.clear();
      navigate('/login', { replace: true });
    };
    window.addEventListener('cabp:no-autenticado', salir);
    return () => window.removeEventListener('cabp:no-autenticado', salir);
  }, [navigate, qc]);

  if (isLoading)
    return (
      <Center h="100vh">
        <Loader />
      </Center>
    );
  if (isError || !data) return <Navigate to="/login" replace state={{ desde: location.pathname }} />;
  return <Outlet />;
}
