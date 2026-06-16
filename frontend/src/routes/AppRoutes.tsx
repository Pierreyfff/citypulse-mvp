import { Routes, Route, Navigate } from 'react-router-dom';
import LoginPage from '../pages/LoginPage';
import RegisterPage from '../pages/RegisterPage';
import DashboardPage from '../pages/DashboardPage';
import IncidentsPage from '../pages/IncidentsPage';
import MapPage from '../pages/MapPage';
import StatsPage from '../pages/StatsPage';
import UsersPage from '../pages/UsersPage';
import ProtectedRoute from '../components/common/ProtectedRoute';

export default function AppRoutes() {
  return (
    <Routes>
      <Route path="/login" element={<LoginPage />} />
      <Route path="/register" element={<RegisterPage />} />
      <Route path="/dashboard" element={
        <ProtectedRoute><DashboardPage /></ProtectedRoute>
      } />
      <Route path="/incidents" element={
        <ProtectedRoute><IncidentsPage /></ProtectedRoute>
      } />
      <Route path="/map" element={
        <ProtectedRoute roles={['ADMIN', 'OPERADOR', 'CIUDADANO']}><MapPage /></ProtectedRoute>
      } />
      <Route path="/stats" element={
        <ProtectedRoute roles={['ADMIN', 'OPERADOR']}><StatsPage /></ProtectedRoute>
      } />
      <Route path="/users" element={
        <ProtectedRoute roles={['ADMIN']}><UsersPage /></ProtectedRoute>
      } />
      <Route path="*" element={<Navigate to="/dashboard" replace />} />
    </Routes>
  );
}
