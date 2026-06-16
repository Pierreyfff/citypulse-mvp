import { Chip } from '@mui/material';
import { getStatusColor, getRiskColor } from '../../utils/riskColor';

interface StatusChipProps {
  status: string;
}

export function StatusChip({ status }: StatusChipProps) {
  return (
    <Chip
      label={status.replace('_', ' ')}
      size="small"
      sx={{
        color: 'white',
        backgroundColor: getStatusColor(status),
        fontWeight: 'bold'
      }}
    />
  );
}

interface RiskChipProps {
  score: number;
}

export function RiskChip({ score }: RiskChipProps) {
  const label = score >= 70 ? 'CRÍTICO' : score >= 40 ? 'ALTO' : score >= 20 ? 'MEDIO' : 'BAJO';
  return (
    <Chip
      label={`${label} (${score})`}
      size="small"
      sx={{
        color: 'white',
        backgroundColor: getRiskColor(score),
        fontWeight: 'bold'
      }}
    />
  );
}
