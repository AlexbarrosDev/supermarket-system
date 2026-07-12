import { useState, useEffect, useMemo } from 'react';
import {
  Dialog, DialogTitle, DialogContent, TextField, Table, TableBody,
  TableCell, TableContainer, TableHead, TableRow, Paper,
  Box, MenuItem, InputAdornment,
} from '@mui/material';
import SearchIcon from '@mui/icons-material/Search';
import { clientApi } from '../../api/clients.api';
import type { ClientSummaryDTO } from '../../models';
import { Loading } from './Loading';
import { useNotification } from '../../contexts/NotificationContext';

interface ClientSearchDialogProps {
  open: boolean;
  onClose: () => void;
  onSelect: (client: ClientSummaryDTO) => void;
}

type FilterField = 'id' | 'name';

export function ClientSearchDialog({ open, onClose, onSelect }: ClientSearchDialogProps) {
  const [clients, setClients] = useState<ClientSummaryDTO[]>([]);
  const [loading, setLoading] = useState(false);
  const [search, setSearch] = useState('');
  const [filterField, setFilterField] = useState<FilterField>('name');
  const { notify } = useNotification();

  useEffect(() => {
    if (open) {
      setLoading(true);
      setSearch('');
      setFilterField('name');
      clientApi.findAll()
        .then((data) => setClients(data.toSorted((a, b) => a.name.localeCompare(b.name))))
        .catch((err) => notify(err.message))
        .finally(() => setLoading(false));
    }
  }, [open]);

  const filtered = useMemo(() => {
    if (!search) return clients;
    const q = search.toLowerCase();
    return clients.filter((c) => {
      const value = filterField === 'id' ? String(c.id) : c.name.toLowerCase();
      return value.includes(q);
    });
  }, [clients, search, filterField]);

  return (
    <Dialog open={open} onClose={onClose} maxWidth="sm" fullWidth>
      <DialogTitle sx={{ fontWeight: 600 }}>Selecionar Cliente</DialogTitle>
      <DialogContent>
        <Box sx={{ display: 'flex', gap: 1, mb: 2, mt: 1 }}>
          <TextField
            select
            value={filterField}
            onChange={(e) => setFilterField(e.target.value as FilterField)}
            size="small"
            sx={{ width: 120 }}
          >
            <MenuItem value="id">ID</MenuItem>
            <MenuItem value="name">Nome</MenuItem>
          </TextField>
          <TextField
            autoComplete="off"
            placeholder="Pesquisar..."
            value={search}
            onChange={(e) => setSearch(e.target.value.toUpperCase())}
            size="small"
            fullWidth
            slotProps={{
              htmlInput: { style: { textTransform: 'uppercase' } },
              input: {
                startAdornment: (
                  <InputAdornment position="start">
                    <SearchIcon />
                  </InputAdornment>
                ),
              },
            }}
          />
        </Box>
        {loading ? (
          <Loading />
        ) : (
          <TableContainer component={Paper} variant="outlined" sx={{ borderRadius: 2 }}>
            <Table size="small">
              <TableHead>
                <TableRow sx={{ bgcolor: 'primary.main' }}>
                  <TableCell width={80} sx={{ color: 'primary.contrastText', fontWeight: 600 }}>ID</TableCell>
                  <TableCell sx={{ color: 'primary.contrastText', fontWeight: 600 }}>Nome</TableCell>
                </TableRow>
              </TableHead>
              <TableBody>
                {filtered.map((c) => (
                  <TableRow
                    key={c.id}
                    hover
                    sx={{ cursor: 'pointer', '&:hover': { bgcolor: 'action.hover', transition: 'background-color 0.15s' } }}
                    onClick={() => { onSelect(c); onClose(); }}
                  >
                    <TableCell>{c.id}</TableCell>
                    <TableCell sx={{ fontWeight: 500 }}>{c.name}</TableCell>
                  </TableRow>
                ))}
                {filtered.length === 0 && (
                  <TableRow>
                    <TableCell colSpan={2} align="center" sx={{ py: 4, color: 'text.secondary' }}>
                      Nenhum cliente encontrado
                    </TableCell>
                  </TableRow>
                )}
              </TableBody>
            </Table>
          </TableContainer>
        )}
      </DialogContent>
    </Dialog>
  );
}
