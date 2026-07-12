import { useEffect, useState } from 'react';
import {
  Box, Typography, Button, Dialog, DialogTitle, DialogContent, DialogActions, TextField, Alert,
  Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Paper, IconButton,
  Tooltip,
} from '@mui/material';
import AddIcon from '@mui/icons-material/Add';
import EditIcon from '@mui/icons-material/Edit';
import DeleteIcon from '@mui/icons-material/Delete';
import { categoryApi } from '../api/categories.api';
import type { CategoryDetailsDTO, CategoryCreateDTO } from '../models';
import { Loading } from '../components/ui/Loading';
import { ConfirmDialog } from '../components/ui/ConfirmDialog';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { z } from 'zod';
import { useNotification } from '../contexts/NotificationContext';

const schema = z.object({ name: z.string().min(1, 'Nome é obrigatório') });

export function CategoriesList() {
  const [categories, setCategories] = useState<CategoryDetailsDTO[]>([]);
  const [loading, setLoading] = useState(true);
  const [dialogOpen, setDialogOpen] = useState(false);
  const [editing, setEditing] = useState<CategoryDetailsDTO | null>(null);
  const [error, setError] = useState('');
  const [deleteTarget, setDeleteTarget] = useState<CategoryDetailsDTO | null>(null);
  const { notify } = useNotification();

  const {
    register, handleSubmit, reset, formState: { errors },
  } = useForm<CategoryCreateDTO>({ resolver: zodResolver(schema) as any });

  const load = () =>
    categoryApi.findAll()
      .then(setCategories)
      .catch((err) => notify(err.message))
      .finally(() => setLoading(false));

  useEffect(() => { load(); }, []);

  const openCreate = () => {
    setEditing(null);
    setError('');
    reset({ name: '' });
    setDialogOpen(true);
  };

  const openEdit = (c: CategoryDetailsDTO) => {
    setEditing(c);
    setError('');
    reset({ name: c.name });
    setDialogOpen(true);
  };

  const onSubmit = (data: CategoryCreateDTO) => {
    setError('');
    const request = editing
      ? categoryApi.update(editing.id, data)
      : categoryApi.create(data);
    request
      .then(() => { setDialogOpen(false); load(); })
      .catch((err) => setError(err.message));
  };

  const handleDelete = () => {
    if (!deleteTarget) return;
    categoryApi.remove(deleteTarget.id)
      .then(() => { setDeleteTarget(null); load(); })
      .catch((err) => notify(err.message));
  };

  if (loading) return <Loading />;

  return (
    <Box>
      <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 3 }}>
        <Typography variant="h4" sx={{ fontWeight: 700 }}>Categorias</Typography>
        <Button variant="contained" startIcon={<AddIcon />} onClick={openCreate}>
          Nova Categoria
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
            {categories.map((c) => (
              <TableRow key={c.id} hover sx={{ '&:hover': { bgcolor: 'action.hover', transition: 'background-color 0.2s' } }}>
                <TableCell>{c.id}</TableCell>
                <TableCell sx={{ fontWeight: 500 }}>{c.name}</TableCell>
                <TableCell align="right">
                  <Tooltip title="Editar">
                    <IconButton onClick={() => openEdit(c)} color="primary"><EditIcon /></IconButton>
                  </Tooltip>
                  <Tooltip title="Excluir">
                    <IconButton onClick={() => setDeleteTarget(c)} color="error"><DeleteIcon /></IconButton>
                  </Tooltip>
                </TableCell>
              </TableRow>
            ))}
            {categories.length === 0 && (
              <TableRow>
                <TableCell colSpan={3} align="center" sx={{ py: 4, color: 'text.secondary' }}>
                  Nenhuma categoria encontrada
                </TableCell>
              </TableRow>
            )}
          </TableBody>
        </Table>
      </TableContainer>

      <Dialog open={dialogOpen} onClose={() => setDialogOpen(false)} fullWidth maxWidth="xs">
        <form onSubmit={handleSubmit(onSubmit)}>
          <DialogTitle sx={{ fontWeight: 600 }}>{editing ? 'Editar Categoria' : 'Nova Categoria'}</DialogTitle>
          <DialogContent>
            <TextField
              autoComplete="off"
              autoFocus
              label="Nome"
              fullWidth
              margin="dense"
              {...register('name', { setValueAs: (v: string) => v?.toUpperCase() })}
              error={!!errors.name}
              slotProps={{ htmlInput: { style: { textTransform: 'uppercase' } } }}
              helperText={errors.name?.message}
            />
            {error && <Alert severity="error" sx={{ mt: 2 }}>{error}</Alert>}
          </DialogContent>
          <DialogActions>
            <Button onClick={() => setDialogOpen(false)}>Cancelar</Button>
            <Button type="submit" variant="contained">Salvar</Button>
          </DialogActions>
        </form>
      </Dialog>

      <ConfirmDialog
        open={!!deleteTarget}
        title="Excluir Categoria"
        message={`Tem certeza que deseja excluir a categoria "${deleteTarget?.name}"?`}
        confirmLabel="Excluir"
        onConfirm={handleDelete}
        onCancel={() => setDeleteTarget(null)}
      />
    </Box>
  );
}
