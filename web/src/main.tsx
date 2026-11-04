import React from 'react';
import ReactDOM from 'react-dom/client';
import { MantineProvider, createTheme, type CSSVariablesResolver } from '@mantine/core';
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

// teal.9 como tono principal: el teal.6 por defecto (y el teal.8) no llega a 4,5:1 con texto blanco ni como enlace sobre blanco.
const theme = createTheme({ primaryColor: 'teal', primaryShade: 9, defaultRadius: 'md' });

// Textos secundarios y placeholders con contraste AA (los grises por defecto quedan en 3,3:1 y 2,1:1).
const variablesCss: CSSVariablesResolver = () => ({
  variables: {},
  light: {
    '--mantine-color-dimmed': 'var(--mantine-color-gray-7)',
    '--mantine-color-placeholder': '#6c757d',
    '--mantine-color-teal-light-color': 'var(--mantine-color-teal-9)',
    '--mantine-color-orange-light-color': 'var(--mantine-color-orange-9)',
  },
  dark: {},
});

ReactDOM.createRoot(document.getElementById('root')!).render(
  <React.StrictMode>
    <MantineProvider theme={theme} cssVariablesResolver={variablesCss} defaultColorScheme="light">
      <QueryClientProvider client={queryClient}>
        <ModalsProvider labels={{ confirm: 'Aceptar', cancel: 'Cancelar' }}>
          <Notifications position="bottom-right" />
          <RouterProvider router={router} />
        </ModalsProvider>
      </QueryClientProvider>
    </MantineProvider>
  </React.StrictMode>,
);
