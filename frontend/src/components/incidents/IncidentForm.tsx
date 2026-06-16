import { Dialog, DialogTitle, DialogContent, TextField, DialogActions, Button, MenuItem, Typography, Box } from '@mui/material';
import { useEffect, useState } from 'react';
import type { IncidentRequest, IncidentType, SeverityLevel } from '../../types/incident';

const INCIDENT_TYPES: IncidentType[] = ['ROBO', 'INCENDIO', 'ACCIDENTE', 'CORTE_ELECTRICO', 'CONGESTION'];

const SEVERITY_LEVELS: { value: SeverityLevel; label: string; range: string }[] = [
  { value: 'BAJO', label: 'Bajo', range: '1-25' },
  { value: 'MEDIO', label: 'Medio', range: '26-50' },
  { value: 'ALTO', label: 'Alto', range: '51-75' },
  { value: 'CRITICO', label: 'Crítico', range: '76-100' },
];

interface IncidentFormProps {
  open: boolean;
  onClose: () => void;
  onSubmit: (data: IncidentRequest) => void;
  initial?: Partial<IncidentRequest>;
}

export default function IncidentForm({ open, onClose, onSubmit, initial }: IncidentFormProps) {
  const [form, setForm] = useState<IncidentRequest>({
    type: initial?.type || 'ROBO',
    description: initial?.description || '',
    address: initial?.address || '',
    latitude: initial?.latitude || 0,
    longitude: initial?.longitude || 0,
    severity: initial?.severity || 'MEDIO',
    sensitiveZone: initial?.sensitiveZone || false
  });

  const [geoLoading, setGeoLoading] = useState(false);

  useEffect(() => {
    if (!initial && navigator.geolocation) {
      setGeoLoading(true);
      navigator.geolocation.getCurrentPosition(
        (pos) => {
          setForm(prev => ({ ...prev, latitude: pos.coords.latitude, longitude: pos.coords.longitude }));
          setGeoLoading(false);
        },
        () => setGeoLoading(false),
        { timeout: 5000 }
      );
    }
  }, [open]);

  const handleSubmit = () => {
    onSubmit(form);
    onClose();
  };

  return (
    <Dialog open={open} onClose={onClose} maxWidth="sm" fullWidth>
      <DialogTitle>{initial ? 'Editar Incidente' : 'Nuevo Incidente'}</DialogTitle>
      <DialogContent>
        <TextField
          select label="Tipo" fullWidth margin="normal"
          value={form.type} onChange={e => setForm({ ...form, type: e.target.value as IncidentType })}
        >
          {INCIDENT_TYPES.map(t => <MenuItem key={t} value={t}>{t.replace('_', ' ')}</MenuItem>)}
        </TextField>
        <TextField
          label="Descripción" fullWidth margin="normal" multiline rows={3}
          value={form.description} onChange={e => setForm({ ...form, description: e.target.value })}
        />
        <TextField
          label="Dirección" fullWidth margin="normal"
          value={form.address} onChange={e => setForm({ ...form, address: e.target.value })}
        />
        <Box display="flex" gap={2}>
          <TextField
            label="Latitud" fullWidth margin="normal" type="number"
            value={form.latitude} onChange={e => setForm({ ...form, latitude: +e.target.value })}
          />
          <TextField
            label="Longitud" fullWidth margin="normal" type="number"
            value={form.longitude} onChange={e => setForm({ ...form, longitude: +e.target.value })}
          />
        </Box>
        {geoLoading && <Typography variant="caption" color="text.secondary">Obteniendo ubicación...</Typography>}
        <TextField
          select label="Severidad" fullWidth margin="normal"
          value={form.severity} onChange={e => setForm({ ...form, severity: e.target.value as SeverityLevel })}
        >
          {SEVERITY_LEVELS.map(sl => (
            <MenuItem key={sl.value} value={sl.value}>{sl.label} ({sl.range})</MenuItem>
          ))}
        </TextField>
      </DialogContent>
      <DialogActions>
        <Button onClick={onClose}>Cancelar</Button>
        <Button onClick={handleSubmit} variant="contained">Guardar</Button>
      </DialogActions>
    </Dialog>
  );
}
