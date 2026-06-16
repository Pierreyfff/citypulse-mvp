import api from './axios';
import type { Incident, IncidentRequest, IncidentStatus, IncidentType } from '../types/incident';

export const getIncidents = (params?: { status?: IncidentStatus; type?: IncidentType }) =>
  api.get<Incident[]>('/incidents', { params }).then(r => r.data);

export const getIncidentById = (id: number) =>
  api.get<Incident>(`/incidents/${id}`).then(r => r.data);

export const createIncident = (data: IncidentRequest) =>
  api.post<Incident>('/incidents', data).then(r => r.data);

export const updateIncident = (id: number, data: IncidentRequest) =>
  api.put<Incident>(`/incidents/${id}`, data).then(r => r.data);

export const updateIncidentStatus = (id: number, status: IncidentStatus) =>
  api.patch<Incident>(`/incidents/${id}/status?status=${status}`).then(r => r.data);

export const deleteIncident = (id: number) =>
  api.delete(`/incidents/${id}`);
