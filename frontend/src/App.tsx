import { BrowserRouter, Routes, Route } from 'react-router-dom';
import { CssBaseline, ThemeProvider } from '@mui/material';
import { ThemeContextProvider, useThemeContext } from './contexts/ThemeContext';
import { NotificationProvider } from './contexts/NotificationContext';
import { AppShell } from './components/layout/AppShell';
import { Dashboard } from './pages/Dashboard';
import { GroupsList } from './pages/GroupsList';
import { CategoriesList } from './pages/CategoriesList';
import { ProductsList } from './pages/ProductsList';
import { ProductForm } from './pages/ProductForm';
import { ProductDetails } from './pages/ProductDetails';
import { ClientsList } from './pages/ClientsList';
import { ClientForm } from './pages/ClientForm';
import { ClientDetails } from './pages/ClientDetails';
import { SalesList } from './pages/SalesList';
import { SaleForm } from './pages/SaleForm';
import { SaleDetails } from './pages/SaleDetails';

function AppContent() {
  const { theme } = useThemeContext();

  return (
    <ThemeProvider theme={theme}>
      <CssBaseline />
      <NotificationProvider>
        <BrowserRouter>
          <Routes>
            <Route element={<AppShell />}>
              <Route path="/" element={<Dashboard />} />
              <Route path="/groups" element={<GroupsList />} />
              <Route path="/categories" element={<CategoriesList />} />
              <Route path="/products" element={<ProductsList />} />
              <Route path="/products/new" element={<ProductForm />} />
              <Route path="/products/:id/edit" element={<ProductForm />} />
              <Route path="/products/:id" element={<ProductDetails />} />
              <Route path="/clients" element={<ClientsList />} />
              <Route path="/clients/new" element={<ClientForm />} />
              <Route path="/clients/:id/edit" element={<ClientForm />} />
              <Route path="/clients/:id" element={<ClientDetails />} />
              <Route path="/sales" element={<SalesList />} />
              <Route path="/sales/new" element={<SaleForm />} />
              <Route path="/sales/:id" element={<SaleDetails />} />
            </Route>
          </Routes>
        </BrowserRouter>
      </NotificationProvider>
    </ThemeProvider>
  );
}

function App() {
  return (
    <ThemeContextProvider>
      <AppContent />
    </ThemeContextProvider>
  );
}

export default App;
