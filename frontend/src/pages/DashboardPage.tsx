  import { Grid, Typography } from '@mui/material';
import { useIncidents } from '../hooks/useIncidents';
import { useDashboard } from '../hooks/useDashboard';
import { useWebSocket } from '../hooks/useWebSocket';
import AppLayout from '../components/layout/AppLayout';
import KpiCard from '../components/dashboard/KpiCard';
import IncidentChart from '../components/dashboard/IncidentChart';
import RiskGauge from '../components/dashboard/RiskGauge';
import RecentIncidents from '../components/dashboard/RecentIncidents';
import LoadingSpinner from '../components/common/LoadingSpinner';

export default function DashboardPage() {
  const { data: dashboard, isLoading: dashLoading } = useDashboard();
  const { data: incidents, isLoading: incLoading } = useIncidents();
  const { connected } = useWebSocket();

  if (dashLoading || incLoading) return <AppLayout><LoadingSpinner /></AppLayout>;

  const avgRisk = incidents?.length
    ? Math.round(incidents.reduce((sum, i) => sum + i.riskScore, 0) / incidents.length)
    : 0;

  return (
    <AppLayout>
      <Typography variant="h4" gutterBottom>Dashboard</Typography>
      <Typography variant="body2" color="text.secondary" mb={2}>
        WebSocket: {connected ? 'Conectado' : 'Desconectado'}
      </Typography>

      <Grid container spacing={3}>
        <Grid item xs={12} sm={6} md={3}>
          <KpiCard title="Total Incidentes" value={dashboard?.totalIncidents || 0} color="#1976d2" />
        </Grid>
        <Grid item xs={12} sm={6} md={3}>
          <KpiCard title="Activos" value={dashboard?.activeIncidents || 0} color="#f57c00" />
        </Grid>
        <Grid item xs={12} sm={6} md={3}>
          <KpiCard title="Resueltos" value={dashboard?.resolvedIncidents || 0} color="#388e3c" />
        </Grid>
        <Grid item xs={12} sm={6} md={3}>
          <KpiCard title="Criticos" value={dashboard?.criticalIncidents || 0} color="#d32f2f" />
        </Grid>

        <Grid item xs={12} md={8}>
          <IncidentChart incidents={incidents || []} />
        </Grid>
        <Grid item xs={12} md={4}>
          <RiskGauge score={avgRisk} />
        </Grid>
        <Grid item xs={12}>
          <RecentIncidents incidents={incidents || []} />
        </Grid>
      </Grid>
    </AppLayout>
  );
}
