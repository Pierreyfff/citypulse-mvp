import { Typography, Grid } from '@mui/material';
import AppLayout from '../components/layout/AppLayout';
import StatsChart from '../components/stats/StatsChart';
import StatsTable from '../components/stats/StatsTable';
import LoadingSpinner from '../components/common/LoadingSpinner';
import ErrorAlert from '../components/common/ErrorAlert';
import { useStats } from '../hooks/useDashboard';

export default function StatsPage() {
  const { data: stats, isLoading, error } = useStats();

  if (isLoading) return <AppLayout><LoadingSpinner /></AppLayout>;
  if (error) return <AppLayout><ErrorAlert message="Error al cargar estadisticas" /></AppLayout>;
  if (!stats) return <AppLayout><Typography>Sin datos</Typography></AppLayout>;

  return (
    <AppLayout>
      <Typography variant="h4" gutterBottom>Estadisticas</Typography>
      <Grid container spacing={3}>
        <Grid item xs={12} md={6}>
          <StatsChart title="Por Tipo" data={stats.incidentsByType} />
        </Grid>
        <Grid item xs={12} md={6}>
          <StatsChart title="Por Estado" data={stats.incidentsByStatus} />
        </Grid>
        <Grid item xs={12} md={6}>
          <StatsTable title="Por Dia" data={stats.incidentsByDay} />
        </Grid>
        <Grid item xs={12} md={6}>
          <StatsTable title="Por Mes" data={stats.incidentsByMonth} />
        </Grid>
      </Grid>
    </AppLayout>
  );
}
