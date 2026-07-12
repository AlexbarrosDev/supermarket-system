import { useState } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import {
  Drawer, List, ListItemButton, ListItemIcon, ListItemText, Toolbar, Box, Typography,
  Menu, MenuItem, ListItem, Divider,
} from '@mui/material';
import HomeIcon from '@mui/icons-material/Home';
import CategoryIcon from '@mui/icons-material/Category';
import GroupIcon from '@mui/icons-material/Group';
import InventoryIcon from '@mui/icons-material/Inventory';
import PeopleIcon from '@mui/icons-material/People';
import PointOfSaleIcon from '@mui/icons-material/PointOfSale';
import PaletteIcon from '@mui/icons-material/Palette';
import StorefrontIcon from '@mui/icons-material/Storefront';
import { useThemeContext } from '../../contexts/ThemeContext';

const DRAWER_WIDTH = 250;

const menuItems = [
  { label: 'Dashboard', path: '/', icon: <HomeIcon /> },
  { label: 'Grupos', path: '/groups', icon: <GroupIcon /> },
  { label: 'Categorias', path: '/categories', icon: <CategoryIcon /> },
  { label: 'Produtos', path: '/products', icon: <InventoryIcon /> },
  { label: 'Clientes', path: '/clients', icon: <PeopleIcon /> },
  { label: 'Vendas', path: '/sales', icon: <PointOfSaleIcon /> },
];

export function Sidebar() {
  const navigate = useNavigate();
  const location = useLocation();
  const { themeName, themeOptions, setTheme } = useThemeContext();
  const [anchorEl, setAnchorEl] = useState<HTMLElement | null>(null);

  return (
    <Drawer
      variant="permanent"
      sx={{
        width: DRAWER_WIDTH,
        '& .MuiDrawer-paper': { width: DRAWER_WIDTH, boxSizing: 'border-box', borderRight: '1px solid', borderColor: 'divider' },
      }}
    >
      <Toolbar>
        <Box sx={{ display: 'flex', alignItems: 'center', gap: 1.5 }}>
          <StorefrontIcon color="primary" sx={{ fontSize: 32 }} />
          <Typography variant="h6" sx={{ fontWeight: 700, whiteSpace: 'nowrap', letterSpacing: 1 }}>
            Supermarket
          </Typography>
        </Box>
      </Toolbar>
      <Divider />
      <List sx={{ flex: 1, pt: 1 }}>
        {menuItems.map((item) => {
          const selected = location.pathname === item.path ||
            (item.path !== '/' && location.pathname.startsWith(item.path));
          return (
            <ListItemButton
              key={item.path}
              selected={selected}
              onClick={() => navigate(item.path)}
              sx={{
                mx: 1,
                borderRadius: 2,
                my: 0.5,
                '&.Mui-selected': {
                  bgcolor: 'primary.main',
                  color: 'primary.contrastText',
                  '&:hover': { bgcolor: 'primary.dark' },
                  '& .MuiListItemIcon-root': { color: 'primary.contrastText' },
                },
              }}
            >
              <ListItemIcon sx={{ minWidth: 40 }}>{item.icon}</ListItemIcon>
              <ListItemText primary={item.label} />
            </ListItemButton>
          );
        })}
      </List>
      <Divider />
      <List>
        <ListItem disablePadding>
          <ListItemButton onClick={(e) => setAnchorEl(e.currentTarget)} sx={{ mx: 1, borderRadius: 2, my: 0.5 }}>
            <ListItemIcon sx={{ minWidth: 40 }}>
              <PaletteIcon />
            </ListItemIcon>
            <ListItemText
              primary={themeName}
              secondary="Tema"
            />
          </ListItemButton>
        </ListItem>
      </List>

      <Menu
        anchorEl={anchorEl}
        open={!!anchorEl}
        onClose={() => setAnchorEl(null)}
        anchorOrigin={{ vertical: 'top', horizontal: 'right' }}
        transformOrigin={{ vertical: 'bottom', horizontal: 'right' }}
      >
        {themeOptions.map((opt) => (
          <MenuItem
            key={opt.name}
            selected={themeName === opt.name}
            onClick={() => { setTheme(opt.name); setAnchorEl(null); }}
            sx={{ fontWeight: themeName === opt.name ? 600 : 400 }}
          >
            {opt.name}
          </MenuItem>
        ))}
      </Menu>
    </Drawer>
  );
}
