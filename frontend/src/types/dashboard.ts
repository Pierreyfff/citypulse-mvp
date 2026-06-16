export interface DashboardResponse {
  totalIncidents: number;
  activeIncidents: number;
  resolvedIncidents: number;
  criticalIncidents: number;
}

export interface StatsResponse {
  incidentsByType: Record<string, number>;
  incidentsByStatus: Record<string, number>;
  incidentsByDay: Record<string, number>;
  incidentsByMonth: Record<string, number>;
}
