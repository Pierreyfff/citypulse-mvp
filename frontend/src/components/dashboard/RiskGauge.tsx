import { Card, CardContent, Typography, Box } from '@mui/material';
import { getRiskColor } from '../../utils/riskColor';

interface RiskGaugeProps {
  score: number;
}

export default function RiskGauge({ score }: RiskGaugeProps) {
  const color = getRiskColor(score);
  const percentage = Math.min(score, 100);

  return (
    <Card sx={{ height: '100%', display: 'flex', flexDirection: 'column' }}>
      <CardContent sx={{ flex: 1 }}>
        <Typography variant="h6" gutterBottom>Riesgo Global</Typography>
        <Box sx={{ position: 'relative', display: 'inline-flex' }}>
          <Box sx={{
            width: 200, height: 200, borderRadius: '50%',
            background: `conic-gradient(${color} ${percentage}%, #e0e0e0 ${percentage}%)`,
            display: 'flex', alignItems: 'center', justifyContent: 'center'
          }}>
            <Box sx={{
              width: 160, height: 160, borderRadius: '50%',
              background: 'white', display: 'flex', flexDirection: 'column',
              alignItems: 'center', justifyContent: 'center'
            }}>
              <Typography variant="h3" sx={{ color, fontWeight: 'bold' }}>{score}</Typography>
              <Typography variant="body2" color="text.secondary">/ 100</Typography>
            </Box>
          </Box>
        </Box>
      </CardContent>
    </Card>
  );
}
