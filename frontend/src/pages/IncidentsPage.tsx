import { useState } from 'react';
import { Typography, Button, Box, Menu, MenuItem, IconButton } from '@mui/material';
import AddIcon from '@mui/icons-material/Add';
import MoreVertIcon from '@mui/icons-material/MoreVert';
import AppLayout from '../components/layout/AppLayout';
import IncidentList from '../components/incidents/IncidentList';
import IncidentForm from '../components/incidents/IncidentForm';
import IncidentFilters from '../components/incidents/IncidentFilters';
import LoadingSpinner from '../components/common/LoadingSpinner';
import ErrorAlert from '../components/common/ErrorAlert';
import { useIncidents, useCreateIncident, useUpdateIncident, useUpdateIncidentStatus, useDeleteIncident } from '../hooks/useIncidents';
import { useAuth } from '../hooks/useAuth';
import type { Incident, IncidentRequest, IncidentStatus, IncidentType } from '../types/incident';

export default function IncidentsPage() {
  const [filters, setFilters] = useState<{ status?: IncidentStatus; type?: IncidentType }>({});
  const [formOpen, setFormOpen] = useState(false);
  const [editingIncident, setEditingIncident] = useState<Incident | null>(null);
  const { data: incidents, isLoading, error } = useIncidents(filters);
  const createMutation = useCreateIncident();
  const updateMutation = useUpdateIncident();
  const statusMutation = useUpdateIncidentStatus();
  const deleteMutation = useDeleteIncident();
  const { hasRole, user } = useAuth();
  const isAdmin = hasRole('ADMIN');
  const isOperador = hasRole('OPERADOR');

  if (isLoading) return <AppLayout><LoadingSpinner /></AppLayout>;
  if (error) return <AppLayout><ErrorAlert message="Error al cargar incidentes" /></AppLayout>;

  const handleEdit = (incident: Incident) => {
    setEditingIncident(incident);
    setFormOpen(true);
  };

  const handleSubmit = (data: IncidentRequest) => {
    if (editingIncident) {
      updateMutation.mutate({ id: editingIncident.id, data });
    } else {
      createMutation.mutate(data);
    }
    setEditingIncident(null);
  };

  const handleStatusChange = (id: number, status: IncidentStatus) => {
    statusMutation.mutate({ id, status });
  };

  return (
    <AppLayout>
      <Box display="flex" justifyContent="space-between" alignItems="center" mb={2}>
        <Typography variant="h4">Incidentes</Typography>
        <Button variant="contained" startIcon={<AddIcon />} onClick={() => { setEditingIncident(null); setFormOpen(true); }}>
          Nuevo Incidente
        </Button>
      </Box>

      <IncidentFilters onFilter={setFilters} />
      <IncidentList
        incidents={incidents || []}
        onEdit={isAdmin || isOperador ? handleEdit : undefined}
        onStatusChange={isAdmin || isOperador ? handleStatusChange : undefined}
        onDelete={isAdmin ? (id) => deleteMutation.mutate(id) : undefined}
      />

      <IncidentForm
        open={formOpen}
        onClose={() => { setFormOpen(false); setEditingIncident(null); }}
        onSubmit={handleSubmit}
        initial={editingIncident ? {
          type: editingIncident.type,
          description: editingIncident.description,
          address: editingIncident.address,
          latitude: editingIncident.latitude,
          longitude: editingIncident.longitude,
          severity: editingIncident.severityLevel,
          sensitiveZone: editingIncident.sensitiveZone
        } : undefined}
      />
    </AppLayout>
  );
}
