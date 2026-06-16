import { Card, CardContent, Typography, Box } from '@mui/material';
import { StatusChip, RiskChip } from '../common/StatusChip';
import { formatDate } from '../../utils/formatDate';
import type { Incident } from '../../types/incident';

interface IncidentCardProps {
  incident: Incident;
  onClick?: () => void;
}

export default function IncidentCard({ incident, onClick }: IncidentCardProps) {
  return (
    <Card sx={{ cursor: onClick ? 'pointer' : 'default' }} onClick={onClick}>
      <CardContent>
        <Box display="flex" justifyContent="space-between" alignItems="center">
          <Typography variant="h6">{incident.type.replace('_', ' ')}</Typography>
          <Box display="flex" gap={1}>
            <StatusChip status={incident.status} />
            <RiskChip score={incident.riskScore} />
          </Box>
        </Box>
        <Typography variant="body2" color="text.secondary" mt={1}>
          {incident.description.slice(0, 100)}
        </Typography>
        <Typography variant="caption" color="text.secondary" display="block" mt={1}>
          {formatDate(incident.createdAt)}
        </Typography>
      </CardContent>
    </Card>
  );
}
