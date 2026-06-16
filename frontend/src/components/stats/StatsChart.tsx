import { Card, CardContent, Typography } from '@mui/material';
import { BarChart, Bar, XAxis, YAxis, Tooltip, ResponsiveContainer, Cell } from 'recharts';

const COLORS = ['#1976d2', '#f57c00', '#388e3c', '#d32f2f', '#7b1fa2'];

interface StatsChartProps {
  title: string;
  data: Record<string, number>;
}

export default function StatsChart({ title, data }: StatsChartProps) {
  const chartData = Object.entries(data).map(([name, value]) => ({ name: name.replace('_', ' '), value }));

  if (chartData.length === 0) return <Typography>Sin datos</Typography>;

  return (
    <Card sx={{ height: '100%', display: 'flex', flexDirection: 'column' }}>
      <CardContent sx={{ flex: 1 }}>
        <Typography variant="h6" gutterBottom>{title}</Typography>
        <ResponsiveContainer width="100%" height={300}>
          <BarChart data={chartData}>
            <XAxis dataKey="name" />
            <YAxis />
            <Tooltip />
            <Bar dataKey="value">
              {chartData.map((_, i) => <Cell key={i} fill={COLORS[i % COLORS.length]} />)}
            </Bar>
          </BarChart>
        </ResponsiveContainer>
      </CardContent>
    </Card>
  );
}
