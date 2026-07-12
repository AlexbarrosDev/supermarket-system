import { Box, Toolbar, useTheme } from '@mui/material';
import { Sidebar } from './Sidebar';
import { Outlet } from 'react-router-dom';

export function AppShell() {
  const theme = useTheme();

  return (
    <Box sx={{ display: 'flex' }}>
      <Sidebar />
      <Box
        component="main"
        sx={{
          flexGrow: 1,
          p: 4,
          bgcolor: theme.palette.background.default,
          minHeight: '100vh',
          animation: 'fadeIn 0.3s ease-in',
          '@keyframes fadeIn': {
            '0%': { opacity: 0, transform: 'translateY(8px)' },
            '100%': { opacity: 1, transform: 'translateY(0)' },
          },
        }}
      >
        <Toolbar />
        <Outlet />
      </Box>
    </Box>
  );
}
