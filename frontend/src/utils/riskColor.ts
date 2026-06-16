export const getRiskColor = (score: number): string => {
  if (score >= 70) return '#d32f2f';
  if (score >= 40) return '#f57c00';
  if (score >= 20) return '#fbc02d';
  return '#388e3c';
};

export const getRiskLabel = (score: number): string => {
  if (score >= 70) return 'CRÍTICO';
  if (score >= 40) return 'ALTO';
  if (score >= 20) return 'MEDIO';
  return 'BAJO';
};

export const getStatusColor = (status: string): string => {
  switch (status) {
    case 'REPORTADO': return '#1976d2';
    case 'EN_PROCESO': return '#f57c00';
    case 'RESUELTO': return '#388e3c';
    default: return '#757575';
  }
};
