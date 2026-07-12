import { useEffect, useState } from 'react';
import {
  Box, Typography, Button, Dialog, DialogTitle, DialogContent, DialogActions, TextField, Alert,
  Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Paper, IconButton,
  Tooltip,
} from '@mui/material';
import AddIcon from '@mui/icons-material/Add';
import EditIcon from '@mui/icons-material/Edit';
import DeleteIcon from '@mui/icons-material/Delete';
import { groupApi } from '../api/groups.api';
import type { GroupDetailsDTO, GroupCreateDTO } from '../models';
import { Loading } from '../components/ui/Loading';
import { ConfirmDialog } from '../components/ui/ConfirmDialog';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { z } from 'zod';
import { useNotification } from '../contexts/NotificationContext';

const schema = z.object({ name: z.string().min(1, 'Nome é obrigatório') });

export function GroupsList() {
  const [groups, setGroups] = useState<GroupDetailsDTO[]>([]);
  const [loading, setLoading] = useState(true);
  const [dialogOpen, setDialogOpen] = useState(false);
  const [editing, setEditing] = useState<GroupDetailsDTO | null>(null);
  const [error, setError] = useState('');
  const [deleteTarget, setDeleteTarget] = useState<GroupDetailsDTO | null>(null);
  const { notify } = useNotification();

  const {
    register, handleSubmit, reset, formState: { errors },
  } = useForm<GroupCreateDTO>({ resolver: zodResolver(schema) as any });

  const load = () =>
    groupApi.findAll()
      .then(setGroups)
      .catch((err) => notify(err.message))
      .finally(() => setLoading(false));

  useEffect(() => { load(); }, []);

  const openCreate = () => {
    setEditing(null);
    setError('');
    reset({ name: '' });
    setDialogOpen(true);
  };

  const openEdit = (g: GroupDetailsDTO) => {
    setEditing(g);
    setError('');
    reset({ name: g.name });
    setDialogOpen(true);
  };

  const onSubmit = (data: GroupCreateDTO) => {
    setError('');
    const request = editing
      ? groupApi.update(editing.id, data)
      : groupApi.create(data);
    request
      .then(() => { setDialogOpen(false); load(); })
      .catch((err) => setError(err.message));
  };

  const handleDelete = () => {
    if (!deleteTarget) return;
    groupApi.remove(deleteTarget.id)
      .then(() => { setDeleteTarget(null); load(); })
      .catch((err) => notify(err.message));
  };

  if (loading) return <Loading />;

  return (
    <Box>
      <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 3 }}>
        <Typography variant="h4" sx={{ fontWeight: 700 }}>Grupos</Typography>
        <Button variant="contained" startIcon={<AddIcon />} onClick={openCreate}>
          Novo Grupo
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
            {groups.map((g) => (
              <TableRow key={g.id} hover sx={{ '&:hover': { bgcolor: 'action.hover', transition: 'background-color 0.2s' } }}>
                <TableCell>{g.id}</TableCell>
                <TableCell sx={{ fontWeight: 500 }}>{g.name}</TableCell>
                <TableCell align="right">
                  <Tooltip title="Editar">
                    <IconButton onClick={() => openEdit(g)} color="primary"><EditIcon /></IconButton>
                  </Tooltip>
                  <Tooltip title="Excluir">
                    <IconButton onClick={() => setDeleteTarget(g)} color="error"><DeleteIcon /></IconButton>
                  </Tooltip>
                </TableCell>
              </TableRow>
            ))}
            {groups.length === 0 && (
              <TableRow>
                <TableCell colSpan={3} align="center" sx={{ py: 4, color: 'text.secondary' }}>
                  Nenhum grupo encontrado
                </TableCell>
              </TableRow>
            )}
          </TableBody>
        </Table>
      </TableContainer>

      <Dialog open={dialogOpen} onClose={() => setDialogOpen(false)} fullWidth maxWidth="xs">
        <form onSubmit={handleSubmit(onSubmit)}>
          <DialogTitle sx={{ fontWeight: 600 }}>{editing ? 'Editar Grupo' : 'Novo Grupo'}</DialogTitle>
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
        title="Excluir Grupo"
        message={`Tem certeza que deseja excluir o grupo "${deleteTarget?.name}"?`}
        confirmLabel="Excluir"
        onConfirm={handleDelete}
        onCancel={() => setDeleteTarget(null)}
      />
    </Box>
  );
}
