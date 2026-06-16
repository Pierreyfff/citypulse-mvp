import { MapContainer, TileLayer, Marker, Popup } from 'react-leaflet';
import L from 'leaflet';
import type { Incident } from '../../types/incident';
import { StatusChip, RiskChip } from '../common/StatusChip';
import { getRiskColor, getRiskLabel } from '../../utils/riskColor';

const createIcon = (score: number) => L.divIcon({
  className: '',
  html: `<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 36" width="28" height="36">
    <path d="M12 0C5.4 0 0 5.4 0 12c0 9 12 24 12 24s12-15 12-24C24 5.4 18.6 0 12 0z" fill="${getRiskColor(score)}" stroke="#333" stroke-width="1.5"/>
    <circle cx="12" cy="12" r="5" fill="white"/>
  </svg>`,
  iconSize: [28, 36],
  iconAnchor: [14, 36],
  popupAnchor: [0, -36]
});

interface IncidentMapProps {
  incidents: Incident[];
  center?: [number, number];
}

export default function IncidentMap({ incidents, center = [-12.0464, -77.0428] }: IncidentMapProps) {
  return (
    <MapContainer center={center} zoom={12} style={{ height: 500, width: '100%', borderRadius: 8 }}>
      <TileLayer url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png" />
      {incidents.map(inc => (
        <Marker key={inc.id} position={[inc.latitude, inc.longitude]} icon={createIcon(inc.riskScore)}>
          <Popup>
            <strong>{inc.type.replace('_', ' ')}</strong><br />
            {inc.description.slice(0, 100)}<br />
            <StatusChip status={inc.status} />
            <RiskChip score={inc.riskScore} />
          </Popup>
        </Marker>
      ))}
    </MapContainer>
  );
}
