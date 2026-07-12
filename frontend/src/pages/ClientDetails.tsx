import { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { Box, Typography, Paper, Grid, Button, Divider } from '@mui/material';
import ArrowBackIcon from '@mui/icons-material/ArrowBack';
import EditIcon from '@mui/icons-material/Edit';
import { clientApi } from '../api/clients.api';
import type { ClientDetailsDTO } from '../models';
import { Loading } from '../components/ui/Loading';
import { StatusChip } from '../components/ui/StatusChip';
import { ConfirmDialog } from '../components/ui/ConfirmDialog';
import { formatDate } from '../utils/format';
import { useNotification } from '../contexts/NotificationContext';

export function ClientDetails() {
  const { id } = useParams();
  const navigate = useNavigate();
  const [client, setClient] = useState<ClientDetailsDTO | null>(null);
  const [loading, setLoading] = useState(true);
  const [statusDialogOpen, setStatusDialogOpen] = useState(false);
  const [toggling, setToggling] = useState(false);
  const { notify } = useNotification();

  const load = () => {
    if (!id) return;
    setLoading(true);
    clientApi.findById(Number(id))
      .then(setClient)
      .catch((err) => notify(err.message))
      .finally(() => setLoading(false));
  };

  useEffect(() => { load(); }, [id]);

  const handleToggleStatus = () => {
    if (!client) return;
    setToggling(true);
    const newStatus = client.status === 'ACTIVE' ? 'INACTIVE' : 'ACTIVE';
    clientApi.updateStatus(Number(id), { status: newStatus })
      .then(() => { setStatusDialogOpen(false); load(); })
      .catch((err) => notify(err.message))
      .finally(() => setToggling(false));
  };

  if (loading) return <Loading />;
  if (!client) return <Typography>Cliente não encontrado</Typography>;

  return (
    <Box sx={{ maxWidth: 700 }}>
      <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 3 }}>
        <Box sx={{ display: 'flex', alignItems: 'center', gap: 2 }}>
          <Button variant="outlined" size="small" onClick={() => navigate('/clients')} startIcon={<ArrowBackIcon />}>
            Voltar
          </Button>
          <Typography variant="h4" sx={{ fontWeight: 700 }}>{client.name}</Typography>
          <StatusChip status={client.status} />
        </Box>
      </Box>

      <Paper sx={{ p: 3, borderRadius: 2, boxShadow: 3 }}>
        <Typography variant="subtitle1" sx={{ fontWeight: 600, mb: 2, color: 'primary.main' }}>
          Informações do Cliente
        </Typography>
        <Divider sx={{ mb: 2 }} />
        <Grid container spacing={2}>
          <Grid size={6}>
            <Typography variant="body2" color="text.secondary">ID</Typography>
            <Typography variant="body1" sx={{ fontWeight: 500 }}>{client.id}</Typography>
          </Grid>
          <Grid size={6}>
            <Typography variant="body2" color="text.secondary">CPF</Typography>
            <Typography variant="body1" sx={{ fontWeight: 500 }}>{client.cpf}</Typography>
          </Grid>
          <Grid size={6}>
            <Typography variant="body2" color="text.secondary">Telefone</Typography>
            <Typography variant="body1" sx={{ fontWeight: 500 }}>{client.phone || '-'}</Typography>
          </Grid>
          <Grid size={6}>
            <Typography variant="body2" color="text.secondary">Cadastro</Typography>
            <Typography variant="body1" sx={{ fontWeight: 500 }}>{formatDate(client.registrationDate)}</Typography>
          </Grid>
        </Grid>

        <Typography variant="subtitle1" sx={{ fontWeight: 600, mt: 3, mb: 2, color: 'primary.main' }}>
          Endereço
        </Typography>
        <Divider sx={{ mb: 2 }} />
        <Grid container spacing={2}>
          <Grid size={8}>
            <Typography variant="body2" color="text.secondary">Rua</Typography>
            <Typography variant="body1">{client.address?.street || '-'}</Typography>
          </Grid>
          <Grid size={4}>
            <Typography variant="body2" color="text.secondary">Número</Typography>
            <Typography variant="body1">{client.address?.number || '-'}</Typography>
          </Grid>
          <Grid size={4}>
            <Typography variant="body2" color="text.secondary">Cidade</Typography>
            <Typography variant="body1">{client.address?.city || '-'}</Typography>
          </Grid>
          <Grid size={4}>
            <Typography variant="body2" color="text.secondary">Estado</Typography>
            <Typography variant="body1">{client.address?.state || '-'}</Typography>
          </Grid>
          <Grid size={4}>
            <Typography variant="body2" color="text.secondary">CEP</Typography>
            <Typography variant="body1">{client.address?.zip || '-'}</Typography>
          </Grid>
        </Grid>

        <Box sx={{ display: 'flex', gap: 2, mt: 4, justifyContent: 'flex-end' }}>
          <Button
            variant="outlined"
            color={client.status === 'ACTIVE' ? 'warning' : 'success'}
            onClick={() => setStatusDialogOpen(true)}
            disabled={toggling}
          >
            {client.status === 'ACTIVE' ? 'Inativar' : 'Ativar'}
          </Button>
          <Button
            variant="contained"
            startIcon={<EditIcon />}
            onClick={() => navigate(`/clients/${id}/edit`)}
          >
            Editar
          </Button>
        </Box>
      </Paper>

      <ConfirmDialog
        open={statusDialogOpen}
        title="Alterar Status"
        message={`Tem certeza que deseja ${client.status === 'ACTIVE' ? 'inativar' : 'ativar'} o cliente "${client.name}"?`}
        onConfirm={handleToggleStatus}
        onCancel={() => setStatusDialogOpen(false)}
        confirmLabel="Confirmar"
      />
    </Box>
  );
}
