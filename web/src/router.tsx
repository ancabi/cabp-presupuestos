import { createBrowserRouter, Navigate } from 'react-router-dom';
import { Layout } from './components/Layout';
import { LoginPage } from './pages/LoginPage';
import { ClientesPage } from './pages/ClientesPage';
import { ClientePage } from './pages/ClientePage';
import { DocumentosPage } from './pages/DocumentosPage';
import { DocumentoEditorPage } from './pages/DocumentoEditorPage';
import { DocumentoImprimirPage } from './pages/DocumentoImprimirPage';
import { DistribuidoresPage } from './pages/DistribuidoresPage';
import { DistribuidorPage } from './pages/DistribuidorPage';
import { AjustesPage } from './pages/AjustesPage';
import { UsuariosPage } from './pages/UsuariosPage';
import { CuentaPage } from './pages/CuentaPage';
import { RequiereSesion } from './components/RequiereSesion';

export const router = createBrowserRouter([
  { path: '/login', element: <LoginPage /> },
  {
    element: <RequiereSesion />,
    children: [
      { path: '/documentos/:id/imprimir', element: <DocumentoImprimirPage /> },
      {
        element: <Layout />,
        children: [
          { index: true, element: <Navigate to="/presupuestos" replace /> },
          { path: '/clientes', element: <ClientesPage /> },
          { path: '/clientes/:id', element: <ClientePage /> },
          { path: '/presupuestos', element: <DocumentosPage tipo="presupuesto" /> },
          { path: '/facturas', element: <DocumentosPage tipo="factura" /> },
          { path: '/documentos/nuevo', element: <DocumentoEditorPage /> },
          { path: '/documentos/:id', element: <DocumentoEditorPage /> },
          { path: '/distribuidores', element: <DistribuidoresPage /> },
          { path: '/distribuidores/:id', element: <DistribuidorPage /> },
          { path: '/ajustes', element: <AjustesPage /> },
          { path: '/usuarios', element: <UsuariosPage /> },
          { path: '/cuenta', element: <CuentaPage /> },
          { path: '*', element: <Navigate to="/" replace /> },
        ],
      },
    ],
  },
]);
