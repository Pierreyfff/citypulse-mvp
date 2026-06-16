import { Box, Card, CardContent, TextField, Button, Typography, Alert } from '@mui/material';
import { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { useAuth } from '../hooks/useAuth';

export default function LoginPage() {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const { login } = useAuth();
  const navigate = useNavigate();

  const handleSubmit = async () => {
    try {
      setError('');
      await login({ username, password });
      navigate('/dashboard');
    } catch {
      setError('Credenciales invalidas');
    }
  };

  return (
    <Box display="flex" justifyContent="center" alignItems="center" minHeight="100vh" bgcolor="#f5f5f5">
      <Card sx={{ width: 400 }}>
        <CardContent>
          <Typography variant="h4" textAlign="center" gutterBottom>CityPulse</Typography>
          <Typography variant="body2" textAlign="center" color="text.secondary" mb={3}>
            Iniciar Sesion
          </Typography>
          {error && <Alert severity="error" sx={{ mb: 2 }}>{error}</Alert>}
          <TextField fullWidth label="Usuario" margin="normal" value={username} onChange={e => setUsername(e.target.value)} />
          <TextField fullWidth label="Contrasena" type="password" margin="normal" value={password} onChange={e => setPassword(e.target.value)} onKeyDown={e => e.key === 'Enter' && handleSubmit()} />
          <Button fullWidth variant="contained" size="large" sx={{ mt: 2 }} onClick={handleSubmit}>Ingresar</Button>
          <Typography textAlign="center" mt={2}>
            <Link to="/register">Crear cuenta</Link>
          </Typography>
        </CardContent>
      </Card>
    </Box>
  );
}
