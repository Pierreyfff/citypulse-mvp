import { Box, Card, CardContent, TextField, Button, Typography, Alert, MenuItem } from '@mui/material';
import { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { useAuth } from '../hooks/useAuth';
import type { Role } from '../types/auth';

export default function RegisterPage() {
  const [form, setForm] = useState({ username: '', email: '', password: '', role: 'CIUDADANO' as Role });
  const [error, setError] = useState('');
  const { register } = useAuth();
  const navigate = useNavigate();

  const handleSubmit = async () => {
    try {
      setError('');
      await register(form);
      navigate('/dashboard');
    } catch (e: any) {
      setError(e.response?.data?.detail || 'Error al registrar');
    }
  };

  return (
    <Box display="flex" justifyContent="center" alignItems="center" minHeight="100vh" bgcolor="#f5f5f5">
      <Card sx={{ width: 400 }}>
        <CardContent>
          <Typography variant="h4" textAlign="center" gutterBottom>CityPulse</Typography>
          <Typography variant="body2" textAlign="center" color="text.secondary" mb={3}>Crear Cuenta</Typography>
          {error && <Alert severity="error" sx={{ mb: 2 }}>{error}</Alert>}
          <TextField fullWidth label="Usuario" margin="normal" value={form.username} onChange={e => setForm({ ...form, username: e.target.value })} />
          <TextField fullWidth label="Email" margin="normal" value={form.email} onChange={e => setForm({ ...form, email: e.target.value })} />
          <TextField fullWidth label="Contrasena" type="password" margin="normal" value={form.password} onChange={e => setForm({ ...form, password: e.target.value })} />
          <TextField select label="Rol" fullWidth margin="normal" value={form.role} onChange={e => setForm({ ...form, role: e.target.value as Role })}>
            <MenuItem value="CIUDADANO">Ciudadano</MenuItem>
            <MenuItem value="OPERADOR">Operador</MenuItem>
          </TextField>
          <Button fullWidth variant="contained" size="large" sx={{ mt: 2 }} onClick={handleSubmit}>Registrarse</Button>
          <Typography textAlign="center" mt={2}><Link to="/login">Ya tengo cuenta</Link></Typography>
        </CardContent>
      </Card>
    </Box>
  );
}
