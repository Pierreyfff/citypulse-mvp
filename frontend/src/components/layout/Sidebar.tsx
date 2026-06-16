import { Drawer, List, ListItemButton, ListItemIcon, ListItemText } from '@mui/material';
import DashboardIcon from '@mui/icons-material/Dashboard';
import WarningIcon from '@mui/icons-material/Warning';
import MapIcon from '@mui/icons-material/Map';
import BarChartIcon from '@mui/icons-material/BarChart';
import PeopleIcon from '@mui/icons-material/People';
import { useNavigate, useLocation } from 'react-router-dom';
import { useAuth } from '../../hooks/useAuth';

const DRAWER_WIDTH = 240;

export default function Sidebar() {
  const navigate = useNavigate();
  const location = useLocation();
  const { hasRole } = useAuth();

  const items = [
    { text: 'Dashboard', icon: <DashboardIcon />, path: '/dashboard', roles: ['ADMIN', 'OPERADOR', 'CIUDADANO'] },
    { text: 'Incidentes', icon: <WarningIcon />, path: '/incidents', roles: ['ADMIN', 'OPERADOR', 'CIUDADANO'] },
    { text: 'Mapa', icon: <MapIcon />, path: '/map', roles: ['ADMIN', 'OPERADOR', 'CIUDADANO'] },
    { text: 'Estadísticas', icon: <BarChartIcon />, path: '/stats', roles: ['ADMIN', 'OPERADOR'] },
    { text: 'Usuarios', icon: <PeopleIcon />, path: '/users', roles: ['ADMIN'] },
  ];

  return (
    <Drawer
      variant="permanent"
      sx={{
        width: DRAWER_WIDTH,
        flexShrink: 0,
        '& .MuiDrawer-paper': { width: DRAWER_WIDTH, boxSizing: 'border-box' }
      }}
    >
      <ToolbarSpacer />
      <List>
        {items
          .filter(item => item.roles.some(r => hasRole(r as any)))
          .map(item => (
            <ListItemButton
              key={item.path}
              selected={location.pathname === item.path}
              onClick={() => navigate(item.path)}
            >
              <ListItemIcon>{item.icon}</ListItemIcon>
              <ListItemText primary={item.text} />
            </ListItemButton>
          ))}
      </List>
    </Drawer>
  );
}

function ToolbarSpacer() {
  return <div style={{ height: 64 }} />;
}
