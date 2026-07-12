import { createContext, useContext, useMemo, useState, type ReactNode } from 'react';
import { createTheme, type Theme } from '@mui/material';

interface ThemeConfig {
  name: string;
  mode: 'light' | 'dark';
  primary: string;
  secondary: string;
  bg: string;
}

const THEME_OPTIONS: ThemeConfig[] = [
  { name: 'Padrão', mode: 'light', primary: '#1976d2', secondary: '#9c27b0', bg: '#f5f5f5' },
  { name: 'Preto/Branco', mode: 'light', primary: '#000000', secondary: '#333333', bg: '#ffffff' },
  { name: 'Branco/Preto', mode: 'dark', primary: '#ffffff', secondary: '#bbbbbb', bg: '#121212' },
  { name: 'Vermelho/Branco', mode: 'light', primary: '#d32f2f', secondary: '#f57c00', bg: '#ffffff' },
  { name: 'Verde/Claro', mode: 'light', primary: '#2e7d32', secondary: '#00897b', bg: '#f5f5f5' },
  { name: 'Roxo/Escuro', mode: 'dark', primary: '#ce93d8', secondary: '#f48fb1', bg: '#1a1a2e' },
];

function buildTheme(config: ThemeConfig): Theme {
  return createTheme({
    palette: {
      mode: config.mode,
      primary: { main: config.primary },
      secondary: { main: config.secondary },
      background: {
        default: config.bg,
        paper: config.mode === 'dark' ? '#1e1e1e' : '#ffffff',
      },
    },
    shape: { borderRadius: 8 },
    typography: {
      fontFamily: '"Roboto", "Helvetica", "Arial", sans-serif',
    },
    components: {
      MuiButton: {
        styleOverrides: {
          root: {
            textTransform: 'none',
            fontWeight: 600,
            borderRadius: 8,
          },
        },
      },
      MuiPaper: {
        styleOverrides: {
          root: {
            borderRadius: 8,
          },
        },
      },
    },
  });
}

interface ThemeContextValue {
  theme: Theme;
  themeName: string;
  themeOptions: ThemeConfig[];
  setTheme: (name: string) => void;
}

const ThemeContext = createContext<ThemeContextValue | null>(null);

const STORAGE_KEY = 'supermarket-theme';

export function ThemeContextProvider({ children }: { children: ReactNode }) {
  const [themeName, setThemeName] = useState(() => {
    const saved = localStorage.getItem(STORAGE_KEY);
    return THEME_OPTIONS.some((t) => t.name === saved) ? saved! : THEME_OPTIONS[0].name;
  });

  const theme = useMemo(() => {
    const config = THEME_OPTIONS.find((t) => t.name === themeName) || THEME_OPTIONS[0];
    return buildTheme(config);
  }, [themeName]);

  const setTheme = (name: string) => {
    localStorage.setItem(STORAGE_KEY, name);
    setThemeName(name);
  };

  return (
    <ThemeContext.Provider value={{ theme, themeName, themeOptions: THEME_OPTIONS, setTheme }}>
      {children}
    </ThemeContext.Provider>
  );
}

export function useThemeContext() {
  const ctx = useContext(ThemeContext);
  if (!ctx) throw new Error('useThemeContext must be used within ThemeContextProvider');
  return ctx;
}
