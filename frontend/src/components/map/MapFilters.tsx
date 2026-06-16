import { Box, TextField, MenuItem, Button } from '@mui/material';
import { useState } from 'react';
import type { IncidentStatus, IncidentType } from '../../types/incident';

interface MapFiltersProps {
  onFilter: (filters: { status?: IncidentStatus; type?: IncidentType }) => void;
}

const TYPES: IncidentType[] = ['ROBO', 'INCENDIO', 'ACCIDENTE', 'CORTE_ELECTRICO', 'CONGESTION'];
const STATUSES: IncidentStatus[] = ['REPORTADO', 'EN_PROCESO', 'RESUELTO'];

export default function MapFilters({ onFilter }: MapFiltersProps) {
  const [status, setStatus] = useState<string>('');
  const [type, setType] = useState<string>('');

  return (
    <Box display="flex" gap={2} mb={2}>
      <TextField
        select label="Estado" size="small" value={status}
        onChange={e => { setStatus(e.target.value); onFilter({ ...(e.target.value ? { status: e.target.value as IncidentStatus } : {}), ...(type ? { type: type as IncidentType } : {}) }); }}
        sx={{ minWidth: 150 }}
      >
        <MenuItem value="">Todos</MenuItem>
        {STATUSES.map(s => <MenuItem key={s} value={s}>{s.replace('_', ' ')}</MenuItem>)}
      </TextField>
      <TextField
        select label="Tipo" size="small" value={type}
        onChange={e => { setType(e.target.value); onFilter({ ...(status ? { status: status as IncidentStatus } : {}), ...(e.target.value ? { type: e.target.value as IncidentType } : {}) }); }}
        sx={{ minWidth: 150 }}
      >
        <MenuItem value="">Todos</MenuItem>
        {TYPES.map(t => <MenuItem key={t} value={t}>{t.replace('_', ' ')}</MenuItem>)}
      </TextField>
    </Box>
  );
}
