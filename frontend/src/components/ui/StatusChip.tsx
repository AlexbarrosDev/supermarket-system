import { Chip } from '@mui/material';
import { STATUS_COLORS } from '../../utils/constants';

interface StatusChipProps {
  status: string;
  size?: 'small' | 'medium';
}

const STATUS_LABELS: Record<string, string> = {
  ACTIVE: 'Ativo',
  INACTIVE: 'Inativo',
  BLOCKED: 'Bloqueado',
  FINALIZED: 'Finalizado',
  CANCELED: 'Cancelado',
};

export function StatusChip({ status, size = 'small' }: StatusChipProps) {
  const color = STATUS_COLORS[status] as 'success' | 'warning' | 'error' | 'info' | undefined;
  const label = STATUS_LABELS[status] || status;
  return (
    <Chip
      label={label}
      color={color}
      size={size}
      variant="filled"
      sx={{
        fontWeight: 600,
        textTransform: 'uppercase',
        letterSpacing: 0.5,
        fontSize: size === 'small' ? '0.7rem' : '0.8rem',
      }}
    />
  );
}
