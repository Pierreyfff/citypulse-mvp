import { Dialog, DialogTitle, DialogContent, TextField, DialogActions, Button, MenuItem, Typography, Box } from '@mui/material';
import { useEffect, useState, useCallback } from 'react';
import type { IncidentRequest, IncidentType, SeverityLevel } from '../../types/incident';

const INCIDENT_TYPES: IncidentType[] = ['ROBO', 'INCENDIO', 'ACCIDENTE', 'CORTE_ELECTRICO', 'CONGESTION'];

const SEVERITY_LEVELS: { value: SeverityLevel; label: string; desc: string }[] = [
  { value: 'BAJO', label: 'Bajo', desc: 'Daños menores, sin peligro' },
  { value: 'MEDIO', label: 'Medio', desc: 'Requiere atención moderada' },
  { value: 'ALTO', label: 'Alto', desc: 'Situación peligrosa, acción urgente' },
  { value: 'CRITICO', label: 'Crítico', desc: 'Peligro inminente, emergencia mayor' },
];

interface IncidentFormProps {
  open: boolean;
  onClose: () => void;
  onSubmit: (data: IncidentRequest) => void;
  initial?: Partial<IncidentRequest>;
}

const emptyForm: IncidentRequest = {
  type: 'ROBO',
  description: '',
  address: '',
  latitude: 0,
  longitude: 0,
  severity: 'MEDIO',
  sensitiveZone: false,
};

export default function IncidentForm({ open, onClose, onSubmit, initial }: IncidentFormProps) {
  const [form, setForm] = useState<IncidentRequest>(emptyForm);
  const [geoState, setGeoState] = useState<'idle' | 'loading' | 'error' | 'done'>('idle');

  const resetForm = useCallback(() => {
    setForm(initial ? { ...emptyForm, ...initial } : emptyForm);
    setGeoState('idle');
  }, [initial]);

  useEffect(() => {
    if (open) {
      resetForm();
    }
  }, [open, resetForm]);

  useEffect(() => {
    if (!open || initial) return;
    if (!navigator.geolocation) return;
    setGeoState('loading');
    navigator.geolocation.getCurrentPosition(
      (pos) => {
        setForm(prev => ({ ...prev, latitude: pos.coords.latitude, longitude: pos.coords.longitude }));
        setGeoState('done');
      },
      (err) => {
        setGeoState(err.code === err.PERMISSION_DENIED ? 'error' : 'error');
      },
      { enableHighAccuracy: true, timeout: 8000, maximumAge: 30000 }
    );
  }, [open, initial]);

  const handleGetLocation = () => {
    if (!navigator.geolocation) return;
    setGeoState('loading');
    navigator.geolocation.getCurrentPosition(
      (pos) => {
        setForm(prev => ({ ...prev, latitude: pos.coords.latitude, longitude: pos.coords.longitude }));
        setGeoState('done');
      },
      () => setGeoState('error'),
      { enableHighAccuracy: true, timeout: 8000, maximumAge: 0 }
    );
  };

  const handleSubmit = () => {
    onSubmit(form);
    onClose();
  };

  const isEditing = !!initial;

  return (
    <>
      <Dialog open={open} onClose={onClose} maxWidth="sm" fullWidth>
        <DialogTitle>{isEditing ? 'Editar Incidente' : 'Nuevo Incidente'}</DialogTitle>
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

          <Typography variant="subtitle2" sx={{ mt: 2, mb: 0.5 }}>Ubicación</Typography>
          <Box display="flex" gap={2} alignItems="flex-start">
            <TextField
              label="Latitud" fullWidth margin="dense" type="number"
              value={form.latitude} onChange={e => setForm({ ...form, latitude: +e.target.value })}
              inputProps={{ step: 0.0001 }}
            />
            <TextField
              label="Longitud" fullWidth margin="dense" type="number"
              value={form.longitude} onChange={e => setForm({ ...form, longitude: +e.target.value })}
              inputProps={{ step: 0.0001 }}
            />
          </Box>
          {!isEditing && (
            <Box display="flex" alignItems="center" gap={1} mt={0.5}>
              <Button size="small" variant="outlined" onClick={handleGetLocation} disabled={geoState === 'loading'}>
                {geoState === 'loading' ? 'Obteniendo...' : 'Obtener ubicación'}
              </Button>
              {geoState === 'done' && <Typography variant="caption" color="success.main">Ubicación capturada</Typography>}
              {geoState === 'error' && <Typography variant="caption" color="error">No se pudo obtener la ubicación. Ingresa las coordenadas manualmente.</Typography>}
            </Box>
          )}

          <Typography variant="subtitle2" sx={{ mt: 2, mb: 0.5 }}>Severidad</Typography>
          <TextField
            select label="Nivel de severidad" fullWidth margin="dense"
            value={form.severity} onChange={e => setForm({ ...form, severity: e.target.value as SeverityLevel })}
          >
            {SEVERITY_LEVELS.map(sl => (
              <MenuItem key={sl.value} value={sl.value}>
                <Box display="flex" justifyContent="space-between" width={1}>
                  <Typography component="span" variant="body2" fontWeight="medium">{sl.label}</Typography>
                  <Typography component="span" variant="caption" color="text.secondary">— {sl.desc}</Typography>
                </Box>
              </MenuItem>
            ))}
          </TextField>
        </DialogContent>
        <DialogActions>
          <Button onClick={onClose}>Cancelar</Button>
          <Button onClick={handleSubmit} variant="contained">Guardar</Button>
        </DialogActions>
      </Dialog>
    </>
  );
}
