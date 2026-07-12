import { useEffect, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { Box, Typography, TextField, Button, MenuItem, Paper, Grid, Divider } from '@mui/material';
import { productApi } from '../api/products.api';
import { groupApi } from '../api/groups.api';
import { categoryApi } from '../api/categories.api';
import type { ProductCreateDTO, ProductUpdateDTO, GroupDetailsDTO, CategoryDetailsDTO } from '../models';
import { Loading } from '../components/ui/Loading';
import { useForm, Controller } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { z } from 'zod';
import { useNotification } from '../contexts/NotificationContext';

const schema = z.object({
  name: z.string().min(1, 'Nome é obrigatório'),
  currentPrice: z.coerce.number().positive('Preço deve ser positivo'),
  categoryId: z.coerce.number().min(1, 'Selecione uma categoria'),
  groupId: z.coerce.number().min(1, 'Selecione um grupo'),
  status: z.enum(['ACTIVE', 'INACTIVE']),
});

export function ProductForm() {
  const { id } = useParams();
  const navigate = useNavigate();
  const isEditing = id && id !== 'new';

  const [groups, setGroups] = useState<GroupDetailsDTO[]>([]);
  const [categories, setCategories] = useState<CategoryDetailsDTO[]>([]);
  const [loading, setLoading] = useState(true);
  const [submitting, setSubmitting] = useState(false);
  const { notify } = useNotification();

  const {
    register, handleSubmit, control, reset, formState: { errors },
  } = useForm<ProductCreateDTO>({ resolver: zodResolver(schema) as any, defaultValues: { status: 'ACTIVE' } });

  useEffect(() => {
    Promise.all([groupApi.findAll(), categoryApi.findAll()])
      .then(([g, c]) => { setGroups(g); setCategories(c); })
      .catch((err) => notify(err.message))
      .finally(() => {
        if (!isEditing) setLoading(false);
      });
  }, [isEditing]);

  useEffect(() => {
    if (isEditing && id) {
      productApi.findById(Number(id))
        .then((product) => {
          reset({
            name: product.name,
            currentPrice: product.currentPrice,
            categoryId: product.category.id,
            groupId: product.group.id,
            status: product.status,
          });
        })
        .catch((err) => notify(err.message))
        .finally(() => setLoading(false));
    }
  }, [id, isEditing, reset]);

  const onSubmit = (data: ProductCreateDTO | ProductUpdateDTO) => {
    setSubmitting(true);
    const navigateTo = () => navigate('/products');
    const request = isEditing && id
      ? productApi.update(Number(id), data as ProductUpdateDTO)
      : productApi.create(data as ProductCreateDTO);
    request
      .then(navigateTo)
      .catch((err) => notify(err.message))
      .finally(() => setSubmitting(false));
  };

  if (loading) return <Loading />;

  return (
    <Box sx={{ maxWidth: 600 }}>
      <Typography variant="h4" sx={{ fontWeight: 700, mb: 3 }}>
        {isEditing ? 'Editar Produto' : 'Novo Produto'}
      </Typography>
      <Paper sx={{ p: 3, borderRadius: 2, boxShadow: 3 }}>
        <form onSubmit={handleSubmit(onSubmit)}>
          <Typography variant="subtitle1" sx={{ fontWeight: 600, mb: 2, color: 'primary.main' }}>
            Informações do Produto
          </Typography>
          <Divider sx={{ mb: 2 }} />
          <Grid container spacing={2}>
            <Grid size={12}>
              <TextField
                autoComplete="off"
                label="Nome"
                fullWidth
                {...register('name', { setValueAs: (v: string) => v?.toUpperCase() })}
                error={!!errors.name}
                helperText={errors.name?.message}
                slotProps={{ htmlInput: { style: { textTransform: 'uppercase' } } }}
              />
            </Grid>
            <Grid size={6}>
              <TextField
                autoComplete="off"
                label="Preço"
                type="number"
                fullWidth
                slotProps={{ htmlInput: { step: '0.01' } }}
                {...register('currentPrice')}
                error={!!errors.currentPrice}
                helperText={errors.currentPrice?.message}
              />
            </Grid>
            <Grid size={6}>
              <Controller
                name="status"
                control={control}
                render={({ field }) => (
                  <TextField {...field} select label="Status" fullWidth
                    error={!!errors.status}
                    helperText={errors.status?.message}
                  >
                    <MenuItem value="ACTIVE">Ativo</MenuItem>
                    <MenuItem value="INACTIVE">Inativo</MenuItem>
                  </TextField>
                )}
              />
            </Grid>
            <Grid size={6}>
              <Controller
                name="categoryId"
                control={control}
                render={({ field }) => (
                  <TextField {...field} select label="Categoria" fullWidth
                    error={!!errors.categoryId}
                    helperText={errors.categoryId?.message}
                  >
                    {categories.map((c) => (
                      <MenuItem key={c.id} value={c.id}>{c.name}</MenuItem>
                    ))}
                  </TextField>
                )}
              />
            </Grid>
            <Grid size={6}>
              <Controller
                name="groupId"
                control={control}
                render={({ field }) => (
                  <TextField {...field} select label="Grupo" fullWidth
                    error={!!errors.groupId}
                    helperText={errors.groupId?.message}
                  >
                    {groups.map((g) => (
                      <MenuItem key={g.id} value={g.id}>{g.name}</MenuItem>
                    ))}
                  </TextField>
                )}
              />
            </Grid>
            <Grid size={12}>
              <Box sx={{ display: 'flex', gap: 2, justifyContent: 'flex-end', mt: 2 }}>
                <Button onClick={() => navigate('/products')}>Cancelar</Button>
                <Button type="submit" variant="contained" disabled={submitting}>
                  {isEditing ? 'Atualizar' : 'Salvar'}
                </Button>
              </Box>
            </Grid>
          </Grid>
        </form>
      </Paper>
    </Box>
  );
}
