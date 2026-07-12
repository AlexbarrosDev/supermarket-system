import { useEffect, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { Box, Typography, TextField, Button, MenuItem, Paper, Grid, CircularProgress, InputAdornment, Alert, Divider } from '@mui/material';
import { clientApi } from '../api/clients.api';
import type { ClientCreateDTO, ClientUpdateDTO } from '../models';
import { Loading } from '../components/ui/Loading';
import { useForm, Controller } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { z } from 'zod';
import axios from 'axios';
import { useNotification } from '../contexts/NotificationContext';

const createSchema = z.object({
  name: z.string().min(1, 'Nome é obrigatório'),
  cpf: z.string().min(11, 'CPF deve ter 11 dígitos').max(14),
  status: z.enum(['ACTIVE', 'INACTIVE', 'BLOCKED']),
  phone: z.string().optional().or(z.literal('')),
  address: z.object({
    street: z.string().min(1, 'Rua é obrigatória'),
    number: z.string().min(1, 'Número é obrigatório'),
    city: z.string().min(1, 'Cidade é obrigatória'),
    state: z.string().min(1, 'Estado é obrigatório'),
    zip: z.string().min(1, 'CEP é obrigatório'),
  }),
});

const updateSchema = z.object({
  name: z.string().optional(),
  phone: z.string().optional().or(z.literal('')),
  address: z.object({
    street: z.string().optional().or(z.literal('')),
    number: z.string().optional().or(z.literal('')),
    city: z.string().optional().or(z.literal('')),
    state: z.string().optional().or(z.literal('')),
    zip: z.string().optional().or(z.literal('')),
  }).optional(),
});

interface ViaCepResponse {
  logradouro: string;
  localidade: string;
  uf: string;
  erro?: boolean;
}

export function ClientForm() {
  const { id } = useParams();
  const navigate = useNavigate();
  const isEditing = id && id !== 'new';
  const [loadingCep, setLoadingCep] = useState(false);
  const [loading, setLoading] = useState(!!isEditing);
  const [error, setError] = useState('');
  const [submitting, setSubmitting] = useState(false);
  const { notify } = useNotification();

  const {
    register, handleSubmit, control, setValue, reset, formState: { errors },
  } = useForm<any>({
    resolver: zodResolver(isEditing ? updateSchema : createSchema) as any,
    defaultValues: isEditing ? {} : { status: 'ACTIVE' },
  });

  useEffect(() => {
    if (isEditing && id) {
      clientApi.findById(Number(id))
        .then((client) => {
          reset({
            name: client.name,
            phone: client.phone || '',
            address: client.address ? {
              street: client.address.street,
              number: client.address.number,
              city: client.address.city,
              state: client.address.state,
              zip: client.address.zip,
            } : undefined,
          });
        })
        .catch((err) => notify(err.message))
        .finally(() => setLoading(false));
    }
  }, [id, isEditing, reset]);

  const fetchAddressByCep = async (cep: string) => {
    const digits = cep.replace(/\D/g, '');
    if (digits.length !== 8) return;

    setLoadingCep(true);
    try {
      const { data } = await axios.get<ViaCepResponse>(`https://viacep.com.br/ws/${digits}/json/`);
      if (!data.erro) {
        setValue('address.street', data.logradouro);
        setValue('address.city', data.localidade);
        setValue('address.state', data.uf);
      }
    } catch {
      // Silently fail — user can type manually
    } finally {
      setLoadingCep(false);
    }
  };

  const onSubmit = (data: any) => {
    setError('');
    setSubmitting(true);
    const navigateTo = () => navigate('/clients');
    if (isEditing && id) {
      clientApi.update(Number(id), data as ClientUpdateDTO)
        .then(navigateTo)
        .catch((err) => setError(err.message))
        .finally(() => setSubmitting(false));
    } else {
      clientApi.create(data as ClientCreateDTO)
        .then(navigateTo)
        .catch((err: Error) => setError(err.message))
        .finally(() => setSubmitting(false));
    }
  };

  if (loading) return <Loading />;

  return (
    <Box sx={{ maxWidth: 700 }}>
      <Typography variant="h4" sx={{ fontWeight: 700, mb: 3 }}>
        {isEditing ? 'Editar Cliente' : 'Novo Cliente'}
      </Typography>

      <Paper sx={{ p: 3, borderRadius: 2, boxShadow: 3 }}>
        <form onSubmit={handleSubmit(onSubmit)}>
          <Typography variant="subtitle1" sx={{ fontWeight: 600, mb: 2, color: 'primary.main' }}>
            Dados do Cliente
          </Typography>
          <Divider sx={{ mb: 2 }} />
          <Grid container spacing={2} sx={{ mb: 3 }}>
            <Grid size={6}>
              <TextField autoComplete="off" label="Nome" fullWidth
                {...register('name', { setValueAs: (v: string) => v?.toUpperCase() })}
                error={!!(errors as any).name} helperText={(errors as any).name?.message}
                slotProps={{ htmlInput: { style: { textTransform: 'uppercase' } } }} />
            </Grid>
            {!isEditing && (
              <>
                <Grid size={3}>
                  <TextField autoComplete="off" label="CPF" fullWidth {...register('cpf')}
                    error={!!(errors as any).cpf} helperText={(errors as any).cpf?.message} />
                </Grid>
                <Grid size={3}>
                  <Controller name="status" control={control}
                    render={({ field }) => (
                      <TextField {...field} select label="Status" fullWidth>
                        <MenuItem value="ACTIVE">Ativo</MenuItem>
                        <MenuItem value="INACTIVE">Inativo</MenuItem>
                        <MenuItem value="BLOCKED">Bloqueado</MenuItem>
                      </TextField>
                    )} />
                </Grid>
              </>
            )}
            <Grid size={6}>
              <TextField autoComplete="off" label="Telefone" fullWidth {...register('phone')} />
            </Grid>
          </Grid>

          <Typography variant="subtitle1" sx={{ fontWeight: 600, mb: 2, color: 'primary.main' }}>
            Endereço
          </Typography>
          <Divider sx={{ mb: 2 }} />
          <Grid container spacing={2} sx={{ mb: 3 }}>
            <Grid size={4}>
              <TextField
                autoComplete="off"
                label="CEP"
                fullWidth
                placeholder="00000-000"
                {...register('address.zip')}
                onBlur={(e) => fetchAddressByCep(e.target.value)}
                slotProps={{
                  input: {
                    endAdornment: loadingCep ? (
                      <InputAdornment position="end">
                        <CircularProgress size={20} />
                      </InputAdornment>
                    ) : undefined,
                  },
                }}
              />
            </Grid>
            <Grid size={6}>
              <TextField autoComplete="off" label="Rua" fullWidth
                {...register('address.street', { setValueAs: (v: string) => v?.toUpperCase() })}
                slotProps={{ htmlInput: { style: { textTransform: 'uppercase' } } }} />
            </Grid>
            <Grid size={2}>
              <TextField autoComplete="off" label="Número" fullWidth {...register('address.number')} />
            </Grid>
            <Grid size={4}>
              <TextField autoComplete="off" label="Cidade" fullWidth
                {...register('address.city', { setValueAs: (v: string) => v?.toUpperCase() })}
                slotProps={{ htmlInput: { style: { textTransform: 'uppercase' } } }} />
            </Grid>
            <Grid size={2}>
              <TextField autoComplete="off" label="Estado" fullWidth
                {...register('address.state', { setValueAs: (v: string) => v?.toUpperCase() })}
                slotProps={{ htmlInput: { style: { textTransform: 'uppercase' } } }} />
            </Grid>
          </Grid>

          {error && <Alert severity="error" sx={{ mb: 2 }}>{error}</Alert>}

          <Box sx={{ display: 'flex', gap: 2, justifyContent: 'flex-end' }}>
            <Button onClick={() => navigate('/clients')}>Cancelar</Button>
            <Button type="submit" variant="contained" disabled={submitting}>
              {isEditing ? 'Atualizar' : 'Salvar'}
            </Button>
          </Box>
        </form>
      </Paper>
    </Box>
  );
}
