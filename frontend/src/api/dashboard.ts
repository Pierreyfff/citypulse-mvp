import api from './axios';
import type { DashboardResponse } from '../types/dashboard';

export const getDashboard = () =>
  api.get<DashboardResponse>('/dashboard').then(r => r.data);
