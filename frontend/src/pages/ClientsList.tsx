import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import {
  Box, Typography, Button, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Paper,
} from '@mui/material';
import AddIcon from '@mui/icons-material/Add';
import { clientApi } from '../api/clients.api';
import type { ClientSummaryDTO } from '../models';
import { Loading } from '../components/ui/Loading';
import { useNotification } from '../contexts/NotificationContext';

export function ClientsList() {
  const [clients, setClients] = useState<ClientSummaryDTO[]>([]);
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();
  const { notify } = useNotification();

  useEffect(() => {
    clientApi.findAll()
      .then(setClients)
      .catch((err) => notify(err.message))
      .finally(() => setLoading(false));
  }, []);

  if (loading) return <Loading />;

  return (
    <Box>
      <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 3 }}>
        <Typography variant="h4" sx={{ fontWeight: 700 }}>Clientes</Typography>
        <Button variant="contained" startIcon={<AddIcon />} onClick={() => navigate('/clients/new')}>
          Novo Cliente
        </Button>
      </Box>

      <TableContainer component={Paper} sx={{ borderRadius: 2, boxShadow: 3 }}>
        <Table>
          <TableHead>
            <TableRow sx={{ bgcolor: 'primary.main' }}>
              <TableCell sx={{ color: 'primary.contrastText', fontWeight: 600 }}>ID</TableCell>
              <TableCell sx={{ color: 'primary.contrastText', fontWeight: 600 }}>Nome</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {clients.length === 0 && (
              <TableRow>
                <TableCell colSpan={2} align="center" sx={{ py: 4, color: 'text.secondary' }}>
                  Nenhum cliente encontrado
                </TableCell>
              </TableRow>
            )}
            {clients.map((c) => (
              <TableRow
                key={c.id}
                hover
                sx={{ cursor: 'pointer', '&:hover': { bgcolor: 'action.hover', transition: 'background-color 0.2s' } }}
                onClick={() => navigate(`/clients/${c.id}`)}
              >
                <TableCell>{c.id}</TableCell>
                <TableCell sx={{ fontWeight: 500 }}>{c.name}</TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>
    </Box>
  );
}
