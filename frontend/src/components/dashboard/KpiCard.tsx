import { Card, CardContent, Typography } from '@mui/material';

interface KpiCardProps {
  title: string;
  value: number;
  color: string;
}

export default function KpiCard({ title, value, color }: KpiCardProps) {
  return (
    <Card sx={{ borderLeft: 4, borderColor: color, height: '100%', display: 'flex', flexDirection: 'column' }}>
      <CardContent sx={{ flex: 1 }}>
        <Typography variant="subtitle2" color="text.secondary">{title}</Typography>
        <Typography variant="h3" sx={{ color, fontWeight: 'bold' }}>{value}</Typography>
      </CardContent>
    </Card>
  );
}
