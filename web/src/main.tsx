import React from 'react';
import ReactDOM from 'react-dom/client';
import { MantineProvider, createTheme } from '@mantine/core';
import { ModalsProvider } from '@mantine/modals';
import { Notifications } from '@mantine/notifications';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { RouterProvider } from 'react-router-dom';
import '@mantine/core/styles.css';
import '@mantine/notifications/styles.css';
import './estilos.css';
import { router } from './router';

const queryClient = new QueryClient({
  defaultOptions: { queries: { refetchOnWindowFocus: false, retry: 1 } },
});

const theme = createTheme({ primaryColor: 'teal', defaultRadius: 'md' });

ReactDOM.createRoot(document.getElementById('root')!).render(
  <React.StrictMode>
    <MantineProvider theme={theme} defaultColorScheme="light">
      <QueryClientProvider client={queryClient}>
        <ModalsProvider labels={{ confirm: 'Aceptar', cancel: 'Cancelar' }}>
          <Notifications position="top-right" />
          <RouterProvider router={router} />
        </ModalsProvider>
      </QueryClientProvider>
    </MantineProvider>
  </React.StrictMode>,
);
