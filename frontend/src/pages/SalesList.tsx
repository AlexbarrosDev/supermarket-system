import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import {
  Box, Typography, Button, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Paper,
} from '@mui/material';
import AddIcon from '@mui/icons-material/Add';
import { saleApi } from '../api/sales.api';
import type { SaleSummaryDTO } from '../models';
import { Loading } from '../components/ui/Loading';
import { StatusChip } from '../components/ui/StatusChip';
import { formatCurrency, formatDateShort } from '../utils/format';
import { useNotification } from '../contexts/NotificationContext';

export function SalesList() {
  const [sales, setSales] = useState<SaleSummaryDTO[]>([]);
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();
  const { notify } = useNotification();

  useEffect(() => {
    saleApi.findAll()
      .then(setSales)
      .catch((err) => notify(err.message))
      .finally(() => setLoading(false));
  }, []);

  if (loading) return <Loading />;

  return (
    <Box>
      <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 3 }}>
        <Typography variant="h4" sx={{ fontWeight: 700 }}>Vendas</Typography>
        <Button variant="contained" startIcon={<AddIcon />} onClick={() => navigate('/sales/new')}>
          Nova Venda
        </Button>
      </Box>

      <TableContainer component={Paper} sx={{ borderRadius: 2, boxShadow: 3 }}>
        <Table>
          <TableHead>
            <TableRow sx={{ bgcolor: 'primary.main' }}>
              <TableCell sx={{ color: 'primary.contrastText', fontWeight: 600 }}>ID</TableCell>
              <TableCell sx={{ color: 'primary.contrastText', fontWeight: 600 }}>Data</TableCell>
              <TableCell sx={{ color: 'primary.contrastText', fontWeight: 600 }}>Cliente</TableCell>
              <TableCell sx={{ color: 'primary.contrastText', fontWeight: 600 }}>Total</TableCell>
              <TableCell sx={{ color: 'primary.contrastText', fontWeight: 600 }}>Status</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {sales.length === 0 && (
              <TableRow>
                <TableCell colSpan={5} align="center" sx={{ py: 4, color: 'text.secondary' }}>
                  Nenhuma venda encontrada
                </TableCell>
              </TableRow>
            )}
            {sales.map((s) => (
              <TableRow
                key={s.id}
                hover
                sx={{ cursor: 'pointer', '&:hover': { bgcolor: 'action.hover', transition: 'background-color 0.2s' } }}
                onClick={() => navigate(`/sales/${s.id}`)}
              >
                <TableCell>{s.id}</TableCell>
                <TableCell>{formatDateShort(s.saleDate)}</TableCell>
                <TableCell sx={{ fontWeight: 500 }}>{s.client.name}</TableCell>
                <TableCell>{formatCurrency(s.total)}</TableCell>
                <TableCell><StatusChip status={s.status} /></TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>
    </Box>
  );
}
