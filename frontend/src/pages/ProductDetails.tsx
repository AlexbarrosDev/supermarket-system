import { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { Box, Typography, Paper, Grid, Button, Chip, Divider } from '@mui/material';
import ArrowBackIcon from '@mui/icons-material/ArrowBack';
import EditIcon from '@mui/icons-material/Edit';
import DeleteIcon from '@mui/icons-material/Delete';
import { productApi } from '../api/products.api';
import type { ProductDetailsDTO } from '../models';
import { Loading } from '../components/ui/Loading';
import { StatusChip } from '../components/ui/StatusChip';
import { ConfirmDialog } from '../components/ui/ConfirmDialog';
import { formatCurrency, formatDate } from '../utils/format';
import { useNotification } from '../contexts/NotificationContext';

export function ProductDetails() {
  const { id } = useParams();
  const navigate = useNavigate();
  const [product, setProduct] = useState<ProductDetailsDTO | null>(null);
  const [loading, setLoading] = useState(true);
  const [statusDialogOpen, setStatusDialogOpen] = useState(false);
  const [deleteDialogOpen, setDeleteDialogOpen] = useState(false);
  const [toggling, setToggling] = useState(false);
  const [deleting, setDeleting] = useState(false);
  const { notify } = useNotification();

  const loadProduct = () => {
    if (!id) return;
    setLoading(true);
    productApi.findById(Number(id))
      .then(setProduct)
      .catch((err) => notify(err.message))
      .finally(() => setLoading(false));
  };

  useEffect(() => { loadProduct(); }, [id]);

  const handleToggleStatus = () => {
    if (!product) return;
    setToggling(true);
    const newStatus = product.status === 'ACTIVE' ? 'INACTIVE' : 'ACTIVE';
    productApi.updateStatus(Number(id), { status: newStatus })
      .then(() => { setStatusDialogOpen(false); loadProduct(); })
      .catch((err) => notify(err.message))
      .finally(() => setToggling(false));
  };

  const handleDelete = () => {
    if (!id) return;
    setDeleting(true);
    productApi.remove(Number(id))
      .then(() => navigate('/products'))
      .catch((err) => {
        notify(err.message);
        setDeleteDialogOpen(false);
      })
      .finally(() => setDeleting(false));
  };

  if (loading) return <Loading />;
  if (!product) return <Typography>Produto não encontrado</Typography>;

  return (
    <Box sx={{ maxWidth: 600 }}>
      <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 3 }}>
        <Box sx={{ display: 'flex', alignItems: 'center', gap: 2 }}>
          <Button variant="outlined" size="small" onClick={() => navigate('/products')} startIcon={<ArrowBackIcon />}>
            Voltar
          </Button>
          <Typography variant="h4" sx={{ fontWeight: 700 }}>{product.name}</Typography>
          <StatusChip status={product.status} />
        </Box>
      </Box>

      <Paper sx={{ p: 3, borderRadius: 2, boxShadow: 3 }}>
        <Typography variant="subtitle1" sx={{ fontWeight: 600, mb: 2, color: 'primary.main' }}>
          Informações do Produto
        </Typography>
        <Divider sx={{ mb: 2 }} />
        <Grid container spacing={2}>
          <Grid size={6}>
            <Typography variant="body2" color="text.secondary">ID</Typography>
            <Typography variant="body1" sx={{ fontWeight: 500 }}>{product.id}</Typography>
          </Grid>
          <Grid size={6}>
            <Typography variant="body2" color="text.secondary">Preço</Typography>
            <Typography variant="body1" sx={{ fontWeight: 700, color: 'success.main', fontSize: '1.1rem' }}>
              {formatCurrency(product.currentPrice)}
            </Typography>
          </Grid>
          <Grid size={6}>
            <Typography variant="body2" color="text.secondary">Categoria</Typography>
            <Chip label={product.category.name} variant="outlined" color="primary" size="small" sx={{ fontWeight: 500 }} />
          </Grid>
          <Grid size={6}>
            <Typography variant="body2" color="text.secondary">Grupo</Typography>
            <Chip label={product.group.name} variant="outlined" color="secondary" size="small" sx={{ fontWeight: 500 }} />
          </Grid>
          <Grid size={6}>
            <Typography variant="body2" color="text.secondary">Criado em</Typography>
            <Typography variant="body1">{formatDate(product.createdAt)}</Typography>
          </Grid>
        </Grid>

        <Box sx={{ display: 'flex', gap: 2, mt: 4, justifyContent: 'flex-end' }}>
          <Button
            variant="outlined"
            color="error"
            startIcon={<DeleteIcon />}
            onClick={() => setDeleteDialogOpen(true)}
            disabled={deleting}
          >
            Excluir
          </Button>
          <Button
            variant="outlined"
            color={product.status === 'ACTIVE' ? 'warning' : 'success'}
            onClick={() => setStatusDialogOpen(true)}
            disabled={toggling}
          >
            {product.status === 'ACTIVE' ? 'Inativar' : 'Ativar'}
          </Button>
          <Button
            variant="contained"
            startIcon={<EditIcon />}
            onClick={() => navigate(`/products/${id}/edit`)}
          >
            Editar
          </Button>
        </Box>
      </Paper>

      <ConfirmDialog
        open={statusDialogOpen}
        title="Alterar Status"
        message={`Tem certeza que deseja ${product.status === 'ACTIVE' ? 'inativar' : 'ativar'} o produto "${product.name}"?`}
        onConfirm={handleToggleStatus}
        onCancel={() => setStatusDialogOpen(false)}
        confirmLabel="Confirmar"
      />

      <ConfirmDialog
        open={deleteDialogOpen}
        title="Excluir Produto"
        message={`Tem certeza que deseja excluir o produto "${product.name}"?`}
        confirmLabel="Excluir"
        confirmColor="error"
        onConfirm={handleDelete}
        onCancel={() => setDeleteDialogOpen(false)}
      />
    </Box>
  );
}
