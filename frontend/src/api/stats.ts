import api from './axios';
import type { StatsResponse } from '../types/dashboard';

export const getStats = () =>
  api.get<StatsResponse>('/stats').then(r => r.data);
