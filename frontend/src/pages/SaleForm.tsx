import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import {
  Box, Typography, TextField, Button, MenuItem, Paper, Grid, IconButton,
  Table, TableBody, TableCell, TableContainer, TableHead, TableRow, InputAdornment,
} from '@mui/material';
import DeleteIcon from '@mui/icons-material/Delete';
import AddIcon from '@mui/icons-material/Add';
import SearchIcon from '@mui/icons-material/Search';
import { saleApi } from '../api/sales.api';
import { clientApi } from '../api/clients.api';
import { productApi } from '../api/products.api';
import type { SaleCreateDTO, ClientSummaryDTO, ProductSummaryDTO } from '../models';
import { Loading } from '../components/ui/Loading';
import { ClientSearchDialog } from '../components/ui/ClientSearchDialog';
import { useForm, Controller, useFieldArray } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { z } from 'zod';
import { useNotification } from '../contexts/NotificationContext';

const schema = z.object({
  clientId: z.coerce.number().min(1, 'Selecione um cliente'),
  items: z.array(z.object({
    productId: z.coerce.number().min(1, 'Selecione um produto'),
    quantity: z.coerce.number().min(1, 'Mínimo 1'),
  })).min(1, 'Adicione pelo menos um item'),
});

export function SaleForm() {
  const navigate = useNavigate();
  const [products, setProducts] = useState<ProductSummaryDTO[]>([]);
  const [loading, setLoading] = useState(true);
  const [inputValue, setInputValue] = useState('');
  const [clientError, setClientError] = useState('');
  const [dialogOpen, setDialogOpen] = useState(false);
  const [submitting, setSubmitting] = useState(false);
  const { notify } = useNotification();

  const {
    control, handleSubmit, setValue, formState: { errors },
  } = useForm<SaleCreateDTO>({
    resolver: zodResolver(schema) as any,
    defaultValues: { clientId: undefined as any, items: [{ productId: undefined as any, quantity: 1 }] },
  });

  const { fields, append, remove } = useFieldArray({ control, name: 'items' as any });

  useEffect(() => {
    productApi.findAll()
      .then(setProducts)
      .catch((err) => notify(err.message))
      .finally(() => setLoading(false));
  }, []);

  const lookupClientById = (value: string) => {
    const id = parseInt(value, 10);
    if (!value) {
      setValue('clientId', undefined as any);
      setClientError('');
      return;
    }
    if (isNaN(id)) {
      setValue('clientId', undefined as any);
      setClientError('ID inválido');
      return;
    }
    clientApi.findById(id)
      .then((client) => {
        setValue('clientId', client.id, { shouldValidate: true });
        setInputValue(client.name + ` (ID: ${client.id})`);
        setClientError('');
      })
      .catch((err) => {
        setValue('clientId', undefined as any);
        setClientError(err.message);
      });
  };

  const handleClientSelect = (client: ClientSummaryDTO) => {
    setValue('clientId', client.id, { shouldValidate: true });
    setInputValue(client.name + ` (ID: ${client.id})`);
    setClientError('');
  };

  const onSubmit = (data: SaleCreateDTO) => {
    setSubmitting(true);
    saleApi.create(data)
      .then(() => navigate('/sales'))
      .catch((err) => notify(err.message))
      .finally(() => setSubmitting(false));
  };

  if (loading) return <Loading />;

  return (
    <Box sx={{ maxWidth: 800 }}>
      <Typography variant="h4" sx={{ fontWeight: 700, mb: 3 }}>Nova Venda</Typography>

      <Paper sx={{ p: 3, borderRadius: 2, boxShadow: 3 }}>
        <form onSubmit={handleSubmit(onSubmit)}>
          <Grid container spacing={2} sx={{ mb: 3 }}>
            <Grid size={6}>
              <TextField
                autoComplete="off"
                label="Cliente"
                value={inputValue}
                onChange={(e) => setInputValue(e.target.value.toUpperCase())}
                onBlur={(e) => lookupClientById(e.target.value)}
                onKeyDown={(e) => {
                  if (e.key === 'Enter') {
                    e.preventDefault();
                    lookupClientById((e.target as HTMLInputElement).value);
                  }
                }}
                placeholder="Digite o ID ou clique na lupa"
                fullWidth
                slotProps={{
                  htmlInput: { style: { textTransform: 'uppercase' } },
                  input: {
                    endAdornment: (
                      <InputAdornment position="end">
                        <IconButton onClick={() => setDialogOpen(true)}>
                          <SearchIcon />
                        </IconButton>
                      </InputAdornment>
                    ),
                  },
                }}
                error={!!errors.clientId || !!clientError}
                helperText={clientError || errors.clientId?.message}
              />
            </Grid>
          </Grid>

          <ClientSearchDialog
            open={dialogOpen}
            onClose={() => setDialogOpen(false)}
            onSelect={handleClientSelect}
          />

          <Typography variant="subtitle1" sx={{ fontWeight: 600, mb: 2, color: 'primary.main' }}>
            Itens da Venda
          </Typography>

          <TableContainer component={Paper} variant="outlined" sx={{ mb: 2, borderRadius: 1 }}>
            <Table size="small">
              <TableHead>
                <TableRow sx={{ bgcolor: 'grey.100' }}>
                  <TableCell sx={{ fontWeight: 600 }}>Produto</TableCell>
                  <TableCell width={120} sx={{ fontWeight: 600 }}>Quantidade</TableCell>
                  <TableCell width={60}></TableCell>
                </TableRow>
              </TableHead>
              <TableBody>
                {fields.map((field, index) => (
                  <TableRow key={field.id}>
                    <TableCell>
                      <Controller
                        name={`items.${index}.productId` as any}
                        control={control}
                        render={({ field }) => (
                          <TextField {...field} select fullWidth size="small"
                            error={!!errors.items?.[index]?.productId}
                          >
                            {products.map((p) => (
                              <MenuItem key={p.id} value={p.id}>{p.name}</MenuItem>
                            ))}
                          </TextField>
                        )}
                      />
                    </TableCell>
                    <TableCell>
                      <Controller
                        name={`items.${index}.quantity` as any}
                        control={control}
                        render={({ field }) => (
                          <TextField {...field} type="number" fullWidth size="small"
                            slotProps={{ htmlInput: { min: 1 } }}
                            error={!!errors.items?.[index]?.quantity}
                          />
                        )}
                      />
                    </TableCell>
                    <TableCell>
                      <IconButton onClick={() => remove(index)} color="error" disabled={fields.length === 1}>
                        <DeleteIcon />
                      </IconButton>
                    </TableCell>
                  </TableRow>
                ))}
              </TableBody>
            </Table>
          </TableContainer>

          <Button
            variant="outlined"
            startIcon={<AddIcon />}
            onClick={() => append({ productId: undefined, quantity: 1 } as any)}
            sx={{ mb: 3 }}
          >
            Adicionar Item
          </Button>

          <Box sx={{ display: 'flex', gap: 2, justifyContent: 'flex-end' }}>
            <Button onClick={() => navigate('/sales')}>Cancelar</Button>
            <Button type="submit" variant="contained" disabled={submitting}>
              Finalizar Venda
            </Button>
          </Box>
        </form>
      </Paper>
    </Box>
  );
}
