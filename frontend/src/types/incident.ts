export type IncidentType = 'ROBO' | 'INCENDIO' | 'ACCIDENTE' | 'CORTE_ELECTRICO' | 'CONGESTION';
export type IncidentStatus = 'REPORTADO' | 'EN_PROCESO' | 'RESUELTO';
export type SeverityLevel = 'BAJO' | 'MEDIO' | 'ALTO' | 'CRITICO';

export interface Incident {
  id: number;
  type: IncidentType;
  status: IncidentStatus;
  description: string;
  address: string;
  latitude: number;
  longitude: number;
  severity: number;
  severityLevel: SeverityLevel;
  sensitiveZone: boolean;
  riskScore: number;
  userId: number;
  createdAt: string;
  updatedAt: string;
}

export interface IncidentRequest {
  type: IncidentType;
  description: string;
  address?: string;
  latitude: number;
  longitude: number;
  severity: SeverityLevel;
  sensitiveZone?: boolean;
}
