import { Card, CardContent, Typography, List, ListItem, ListItemText } from '@mui/material';
import { StatusChip, RiskChip } from '../common/StatusChip';
import type { Incident } from '../../types/incident';
import { formatDate } from '../../utils/formatDate';

interface RecentIncidentsProps {
  incidents: Incident[];
}

export default function RecentIncidents({ incidents }: RecentIncidentsProps) {
  const recent = [...incidents]
    .sort((a, b) => new Date(b.createdAt).getTime() - new Date(a.createdAt).getTime())
    .slice(0, 5);

  return (
    <Card sx={{ height: '100%', display: 'flex', flexDirection: 'column' }}>
      <CardContent sx={{ flex: 1 }}>
        <Typography variant="h6" gutterBottom>Incidentes Recientes</Typography>
        <List dense>
          {recent.map(inc => (
            <ListItem key={inc.id} divider>
              <ListItemText
                primary={`${inc.type.replace('_', ' ')} - ${inc.description.slice(0, 50)}...`}
                secondary={formatDate(inc.createdAt)}
              />
              <StatusChip status={inc.status} />
              <RiskChip score={inc.riskScore} />
            </ListItem>
          ))}
        </List>
      </CardContent>
    </Card>
  );
}
