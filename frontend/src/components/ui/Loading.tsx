import { Box, CircularProgress, Typography } from '@mui/material';

interface LoadingProps {
  message?: string;
}

export function Loading({ message = 'Carregando...' }: LoadingProps) {
  return (
    <Box
      sx={{
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
        justifyContent: 'center',
        py: 10,
        animation: 'fadeIn 0.3s ease-in',
        '@keyframes fadeIn': {
          '0%': { opacity: 0 },
          '100%': { opacity: 1 },
        },
      }}
    >
      <CircularProgress size={48} thickness={4} />
      <Typography sx={{ mt: 2, fontWeight: 500 }} color="text.secondary">
        {message}
      </Typography>
    </Box>
  );
}
