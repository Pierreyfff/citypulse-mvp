import {
  Table, TableBody, TableCell, TableContainer, TableHead, TableRow,
  Paper, IconButton, Chip, Select, MenuItem, Box
} from '@mui/material';
import EditIcon from '@mui/icons-material/Edit';
import DeleteIcon from '@mui/icons-material/Delete';
import LocalFireDepartmentIcon from '@mui/icons-material/LocalFireDepartment';
import WarningIcon from '@mui/icons-material/Warning';
import TrafficIcon from '@mui/icons-material/Traffic';
import PowerOffIcon from '@mui/icons-material/PowerOff';
import PersonSearchIcon from '@mui/icons-material/PersonSearch';
import type { Incident, IncidentStatus, IncidentType, SeverityLevel } from '../../types/incident';
import { StatusChip, RiskChip } from '../common/StatusChip';
import { formatDate } from '../../utils/formatDate';

const TYPE_ICONS: Record<IncidentType, React.ReactNode> = {
  INCENDIO: <LocalFireDepartmentIcon fontSize="small" color="error" />,
  ROBO: <PersonSearchIcon fontSize="small" color="warning" />,
  ACCIDENTE: <WarningIcon fontSize="small" color="error" />,
  CORTE_ELECTRICO: <PowerOffIcon fontSize="small" color="disabled" />,
  CONGESTION: <TrafficIcon fontSize="small" color="info" />,
};

const SEVERITY_COLORS: Record<SeverityLevel, 'default' | 'success' | 'warning' | 'error'> = {
  BAJO: 'default',
  MEDIO: 'success',
  ALTO: 'warning',
  CRITICO: 'error',
};

interface IncidentListProps {
  incidents: Incident[];
  onEdit?: (incident: Incident) => void;
  onDelete?: (id: number) => void;
  onStatusChange?: (id: number, status: IncidentStatus) => void;
}

export default function IncidentList({ incidents, onEdit, onDelete, onStatusChange }: IncidentListProps) {
  return (
    <TableContainer component={Paper}>
      <Table>
        <TableHead>
          <TableRow>
            <TableCell>ID</TableCell>
            <TableCell>Tipo</TableCell>
            <TableCell>Severidad</TableCell>
            <TableCell>Descripción</TableCell>
            <TableCell>Estado</TableCell>
            <TableCell>Riesgo</TableCell>
            <TableCell>Fecha</TableCell>
            <TableCell>Acciones</TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {incidents.map(inc => (
            <TableRow key={inc.id}>
              <TableCell>{inc.id}</TableCell>
              <TableCell>
                <Box display="flex" alignItems="center" gap={0.5}>
                  {TYPE_ICONS[inc.type]}
                  <Chip label={inc.type.replace('_', ' ')} size="small" variant="outlined" />
                </Box>
              </TableCell>
              <TableCell>
                <Chip
                  label={inc.severityLevel}
                  size="small"
                  color={SEVERITY_COLORS[inc.severityLevel]}
                />
              </TableCell>
              <TableCell>{inc.description.slice(0, 60)}...</TableCell>
              <TableCell>
                {onStatusChange ? (
                  <Select
                    size="small"
                    value={inc.status}
                    onChange={e => onStatusChange(inc.id, e.target.value as IncidentStatus)}
                    sx={{ minWidth: 130, '& .MuiSelect-select': { py: 0.5 } }}
                  >
                    <MenuItem value="REPORTADO">REPORTADO</MenuItem>
                    <MenuItem value="EN_PROCESO">EN PROCESO</MenuItem>
                    <MenuItem value="RESUELTO">RESUELTO</MenuItem>
                  </Select>
                ) : (
                  <StatusChip status={inc.status} />
                )}
              </TableCell>
              <TableCell><RiskChip score={inc.riskScore} /></TableCell>
              <TableCell>{formatDate(inc.createdAt)}</TableCell>
              <TableCell>
                <Box display="flex" gap={0.5}>
                  {onEdit && <IconButton size="small" onClick={() => onEdit(inc)}><EditIcon fontSize="small" /></IconButton>}
                  {onDelete && <IconButton size="small" onClick={() => onDelete(inc.id)}><DeleteIcon fontSize="small" /></IconButton>}
                </Box>
              </TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>
    </TableContainer>
  );
}
