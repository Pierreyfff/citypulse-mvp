import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import * as incidentsApi from '../api/incidents';
import type { IncidentRequest, IncidentStatus, IncidentType } from '../types/incident';

export const useIncidents = (params?: { status?: IncidentStatus; type?: IncidentType }) =>
  useQuery({
    queryKey: ['incidents', params],
    queryFn: () => incidentsApi.getIncidents(params)
  });

export const useIncident = (id: number) =>
  useQuery({
    queryKey: ['incident', id],
    queryFn: () => incidentsApi.getIncidentById(id),
    enabled: !!id
  });

export const useCreateIncident = () => {
  const qc = useQueryClient();
  return useMutation({
    mutationFn: (data: IncidentRequest) => incidentsApi.createIncident(data),
    onSuccess: () => qc.invalidateQueries({ queryKey: ['incidents'] })
  });
};

export const useUpdateIncident = () => {
  const qc = useQueryClient();
  return useMutation({
    mutationFn: ({ id, data }: { id: number; data: IncidentRequest }) =>
      incidentsApi.updateIncident(id, data),
    onSuccess: () => qc.invalidateQueries({ queryKey: ['incidents'] })
  });
};

export const useUpdateIncidentStatus = () => {
  const qc = useQueryClient();
  return useMutation({
    mutationFn: ({ id, status }: { id: number; status: IncidentStatus }) =>
      incidentsApi.updateIncidentStatus(id, status),
    onSuccess: () => qc.invalidateQueries({ queryKey: ['incidents'] })
  });
};

export const useDeleteIncident = () => {
  const qc = useQueryClient();
  return useMutation({
    mutationFn: (id: number) => incidentsApi.deleteIncident(id),
    onSuccess: () => qc.invalidateQueries({ queryKey: ['incidents'] })
  });
};
