import { Typography, Box } from '@mui/material';
import AppLayout from '../components/layout/AppLayout';
import IncidentMap from '../components/map/IncidentMap';
import MapFilters from '../components/map/MapFilters';
import LoadingSpinner from '../components/common/LoadingSpinner';
import { useIncidents } from '../hooks/useIncidents';
import { useState } from 'react';
import type { IncidentStatus, IncidentType } from '../types/incident';

export default function MapPage() {
  const [filters, setFilters] = useState<{ status?: IncidentStatus; type?: IncidentType }>({});
  const { data: incidents, isLoading } = useIncidents(filters);

  if (isLoading) return <AppLayout><LoadingSpinner /></AppLayout>;

  return (
    <AppLayout>
      <Typography variant="h4" gutterBottom>Mapa de Incidentes</Typography>
      <MapFilters onFilter={setFilters} />
      <Box sx={{ height: 500 }}>
        <IncidentMap incidents={incidents || []} />
      </Box>
    </AppLayout>
  );
}
