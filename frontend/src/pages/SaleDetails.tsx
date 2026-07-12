import { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import {
  Box, Typography, Paper, Grid, Button, Table, TableBody, TableCell, TableContainer,
  TableHead, TableRow, Divider,
} from '@mui/material';
import ArrowBackIcon from '@mui/icons-material/ArrowBack';
import CancelIcon from '@mui/icons-material/Cancel';
import DeleteIcon from '@mui/icons-material/Delete';
import { saleApi } from '../api/sales.api';
import type { SaleDetailsDTO } from '../models';
import { Loading } from '../components/ui/Loading';
import { StatusChip } from '../components/ui/StatusChip';
import { ConfirmDialog } from '../components/ui/ConfirmDialog';
import { formatCurrency, formatDate } from '../utils/format';
import { useNotification } from '../contexts/NotificationContext';

export function SaleDetails() {
  const { id } = useParams();
  const navigate = useNavigate();
  const [sale, setSale] = useState<SaleDetailsDTO | null>(null);
  const [loading, setLoading] = useState(true);
  const [cancelDialogOpen, setCancelDialogOpen] = useState(false);
  const [deleteDialogOpen, setDeleteDialogOpen] = useState(false);
  const [canceling, setCanceling] = useState(false);
  const [deleting, setDeleting] = useState(false);
  const { notify } = useNotification();

  const load = () => {
    if (id) saleApi.findById(Number(id))
      .then(setSale)
      .catch((err) => notify(err.message))
      .finally(() => setLoading(false));
  };

  useEffect(() => { load(); }, [id]);

  const handleCancel = () => {
    if (!id) return;
    setCanceling(true);
    saleApi.updateStatus(Number(id), { status: 'CANCELED' })
      .then(() => {
        setCancelDialogOpen(false);
        load();
      })
      .catch((err) => notify(err.message))
      .finally(() => setCanceling(false));
  };

  const handleDelete = () => {
    if (!id) return;
    setDeleting(true);
    saleApi.remove(Number(id))
      .then(() => navigate('/sales'))
      .catch((err) => {
        notify(err.message);
        setDeleteDialogOpen(false);
      })
      .finally(() => setDeleting(false));
  };

  if (loading) return <Loading />;
  if (!sale) return <Typography>Venda não encontrada</Typography>;

  const canCancel = sale.status === 'FINALIZED';

  return (
    <Box sx={{ maxWidth: 900 }}>
      <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 3 }}>
        <Box sx={{ display: 'flex', alignItems: 'center', gap: 2 }}>
          <Button variant="outlined" size="small" onClick={() => navigate('/sales')} startIcon={<ArrowBackIcon />}>
            Voltar
          </Button>
          <Box>
            <Typography variant="h4" sx={{ fontWeight: 700 }}>Venda #{sale.id}</Typography>
            <Typography variant="body2" color="text.secondary">{formatDate(sale.saleDate)}</Typography>
          </Box>
        </Box>
        <Box sx={{ display: 'flex', gap: 1 }}>
          {canCancel && (
            <Button
              variant="outlined"
              color="error"
              startIcon={<CancelIcon />}
              onClick={() => setCancelDialogOpen(true)}
              disabled={canceling}
            >
              Cancelar Venda
            </Button>
          )}
          {sale.status === 'CANCELED' && (
            <Button
              variant="outlined"
              color="error"
              startIcon={<DeleteIcon />}
              onClick={() => setDeleteDialogOpen(true)}
              disabled={deleting}
            >
              Excluir Venda
            </Button>
          )}
        </Box>
      </Box>

      <Paper sx={{ p: 3, mb: 3, borderRadius: 2, boxShadow: 3 }}>
        <Typography variant="subtitle1" sx={{ fontWeight: 600, mb: 2, color: 'primary.main' }}>
          Dados da Venda
        </Typography>
        <Divider sx={{ mb: 2 }} />
        <Grid container spacing={2}>
          <Grid size={6}>
            <Typography variant="body2" color="text.secondary">Cliente</Typography>
            <Typography variant="h6" sx={{ fontWeight: 600 }}>{sale.client.name}</Typography>
          </Grid>
          <Grid size={6}>
            <Typography variant="body2" color="text.secondary">Status</Typography>
            <StatusChip status={sale.status} />
          </Grid>
        </Grid>
      </Paper>

      <Typography variant="h6" sx={{ fontWeight: 600, mb: 2 }}>Itens da Venda</Typography>
      <TableContainer component={Paper} sx={{ borderRadius: 2, boxShadow: 3 }}>
        <Table>
          <TableHead>
            <TableRow sx={{ bgcolor: 'primary.main' }}>
              <TableCell sx={{ color: 'primary.contrastText', fontWeight: 600 }}>Produto</TableCell>
              <TableCell align="right" sx={{ color: 'primary.contrastText', fontWeight: 600 }}>Qtd</TableCell>
              <TableCell align="right" sx={{ color: 'primary.contrastText', fontWeight: 600 }}>Preço Unit.</TableCell>
              <TableCell align="right" sx={{ color: 'primary.contrastText', fontWeight: 600 }}>Subtotal</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {sale.items.map((item, i) => (
              <TableRow key={i} hover>
                <TableCell sx={{ fontWeight: 500 }}>{item.product.name}</TableCell>
                <TableCell align="right">{item.quantity}</TableCell>
                <TableCell align="right">{formatCurrency(item.unitPrice)}</TableCell>
                <TableCell align="right" sx={{ fontWeight: 600 }}>{formatCurrency(item.subTotal)}</TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>

      <Box sx={{ display: 'flex', justifyContent: 'flex-end', mt: 2 }}>
        <Paper sx={{ p: 2, borderRadius: 2, bgcolor: 'primary.main', color: 'primary.contrastText' }}>
          <Typography variant="h5" sx={{ fontWeight: 700 }}>
            Total: {formatCurrency(sale.total)}
          </Typography>
        </Paper>
      </Box>

      <ConfirmDialog
        open={cancelDialogOpen}
        title="Cancelar Venda"
        message="Tem certeza que deseja cancelar esta venda? Esta ação não pode ser desfeita."
        confirmLabel="Sim, Cancelar"
        confirmColor="error"
        onConfirm={handleCancel}
        onCancel={() => setCancelDialogOpen(false)}
      />

      <ConfirmDialog
        open={deleteDialogOpen}
        title="Excluir Venda"
        message="Tem certeza que deseja excluir esta venda? Esta ação é permanente."
        confirmLabel="Excluir"
        confirmColor="error"
        onConfirm={handleDelete}
        onCancel={() => setDeleteDialogOpen(false)}
      />
    </Box>
  );
}
