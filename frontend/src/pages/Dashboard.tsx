import { useEffect, useState } from 'react';
import { Grid, Card, CardContent, Typography, Box, CardActionArea } from '@mui/material';
import PointOfSaleIcon from '@mui/icons-material/PointOfSale';
import InventoryIcon from '@mui/icons-material/Inventory';
import PeopleIcon from '@mui/icons-material/People';
import AttachMoneyIcon from '@mui/icons-material/AttachMoney';
import { saleApi } from '../api/sales.api';
import { productApi } from '../api/products.api';
import { clientApi } from '../api/clients.api';
import { Loading } from '../components/ui/Loading';
import { formatCurrency } from '../utils/format';
import { useNotification } from '../contexts/NotificationContext';

export function Dashboard() {
  const [stats, setStats] = useState({ sales: 0, products: 0, clients: 0, total: 0 });
  const [loading, setLoading] = useState(true);
  const { notify } = useNotification();

  useEffect(() => {
    Promise.all([saleApi.findAll(), productApi.findAll(), clientApi.findAll()])
      .then(([sales, products, clients]) => {
        setStats({
          sales: sales.length,
          products: products.length,
          clients: clients.length,
          total: sales.reduce((acc, s) => acc + s.total, 0),
        });
      })
      .catch((err) => notify(err.message))
      .finally(() => setLoading(false));
  }, []);

  if (loading) return <Loading />;

  const cards = [
    { label: 'Vendas Realizadas', value: stats.sales, icon: <PointOfSaleIcon sx={{ fontSize: 40 }} />, color: '#1976d2', bg: '#e3f2fd' },
    { label: 'Produtos Cadastrados', value: stats.products, icon: <InventoryIcon sx={{ fontSize: 40 }} />, color: '#388e3c', bg: '#e8f5e9' },
    { label: 'Clientes Cadastrados', value: stats.clients, icon: <PeopleIcon sx={{ fontSize: 40 }} />, color: '#f57c00', bg: '#fff3e0' },
    { label: 'Faturamento Total', value: formatCurrency(stats.total), icon: <AttachMoneyIcon sx={{ fontSize: 40 }} />, color: '#7b1fa2', bg: '#f3e5f5' },
  ];

  return (
    <Box>
      <Typography variant="h4" sx={{ fontWeight: 700, mb: 1 }}>Dashboard</Typography>
      <Typography variant="body1" color="text.secondary" sx={{ mb: 4 }}>
        Resumo do sistema
      </Typography>
      <Grid container spacing={3}>
        {cards.map((card) => (
          <Grid key={card.label} size={{ xs: 12, sm: 6, md: 3 }}>
            <Card
              sx={{
                borderRadius: 3,
                boxShadow: 3,
                transition: 'transform 0.2s, box-shadow 0.2s',
                '&:hover': { transform: 'translateY(-4px)', boxShadow: 6 },
              }}
            >
              <CardActionArea>
                <CardContent sx={{ bgcolor: card.bg, position: 'relative', overflow: 'hidden' }}>
                  <Box sx={{ display: 'flex', alignItems: 'center', gap: 2, position: 'relative', zIndex: 1 }}>
                    <Box sx={{ color: card.color }}>{card.icon}</Box>
                    <Box>
                      <Typography variant="body2" color="text.secondary" sx={{ fontWeight: 500 }}>
                        {card.label}
                      </Typography>
                      <Typography variant="h4" sx={{ fontWeight: 700, color: card.color }}>
                        {card.value}
                      </Typography>
                    </Box>
                  </Box>
                </CardContent>
              </CardActionArea>
            </Card>
          </Grid>
        ))}
      </Grid>
    </Box>
  );
}
