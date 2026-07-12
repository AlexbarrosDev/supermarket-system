import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import {
  Box, Typography, Button, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Paper,
  IconButton, Tooltip,
} from '@mui/material';
import AddIcon from '@mui/icons-material/Add';
import DeleteIcon from '@mui/icons-material/Delete';
import { productApi } from '../api/products.api';
import type { ProductSummaryDTO } from '../models';
import { Loading } from '../components/ui/Loading';
import { ConfirmDialog } from '../components/ui/ConfirmDialog';
import { useNotification } from '../contexts/NotificationContext';

export function ProductsList() {
  const [products, setProducts] = useState<ProductSummaryDTO[]>([]);
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();
  const { notify } = useNotification();
  const [deleteTarget, setDeleteTarget] = useState<ProductSummaryDTO | null>(null);

  const load = () =>
    productApi.findAll()
      .then(setProducts)
      .catch((err) => notify(err.message))
      .finally(() => setLoading(false));

  useEffect(() => { load(); }, []);

  const handleDelete = () => {
    if (!deleteTarget) return;
    productApi.remove(deleteTarget.id)
      .then(() => { setDeleteTarget(null); load(); })
      .catch((err) => notify(err.message));
  };

  if (loading) return <Loading />;

  return (
    <Box>
      <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 3 }}>
        <Typography variant="h4" sx={{ fontWeight: 700 }}>Produtos</Typography>
        <Button variant="contained" startIcon={<AddIcon />} onClick={() => navigate('/products/new')}>
          Novo Produto
        </Button>
      </Box>

      <TableContainer component={Paper} sx={{ borderRadius: 2, boxShadow: 3 }}>
        <Table>
          <TableHead>
            <TableRow sx={{ bgcolor: 'primary.main' }}>
              <TableCell sx={{ color: 'primary.contrastText', fontWeight: 600 }}>ID</TableCell>
              <TableCell sx={{ color: 'primary.contrastText', fontWeight: 600 }}>Nome</TableCell>
              <TableCell align="right" sx={{ color: 'primary.contrastText', fontWeight: 600 }}>Ações</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {products.length === 0 && (
              <TableRow>
                <TableCell colSpan={3} align="center" sx={{ py: 4, color: 'text.secondary' }}>
                  Nenhum produto encontrado
                </TableCell>
              </TableRow>
            )}
            {products.map((p) => (
              <TableRow
                key={p.id}
                hover
                sx={{ '&:hover': { bgcolor: 'action.hover', transition: 'background-color 0.2s' } }}
              >
                <TableCell
                  sx={{ cursor: 'pointer' }}
                  onClick={() => navigate(`/products/${p.id}`)}
                >
                  {p.id}
                </TableCell>
                <TableCell
                  sx={{ fontWeight: 500, cursor: 'pointer' }}
                  onClick={() => navigate(`/products/${p.id}`)}
                >
                  {p.name}
                </TableCell>
                <TableCell align="right">
                  <Tooltip title="Excluir">
                    <IconButton onClick={() => setDeleteTarget(p)} color="error" size="small">
                      <DeleteIcon />
                    </IconButton>
                  </Tooltip>
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>

      <ConfirmDialog
        open={!!deleteTarget}
        title="Excluir Produto"
        message={`Tem certeza que deseja excluir o produto "${deleteTarget?.name}"?`}
        confirmLabel="Excluir"
        confirmColor="error"
        onConfirm={handleDelete}
        onCancel={() => setDeleteTarget(null)}
      />
    </Box>
  );
}
