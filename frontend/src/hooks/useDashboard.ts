import { useQuery } from '@tanstack/react-query';
import { getDashboard } from '../api/dashboard';
import { getStats } from '../api/stats';

export const useDashboard = () =>
  useQuery({
    queryKey: ['dashboard'],
    queryFn: getDashboard,
    refetchInterval: 10000
  });

export const useStats = () =>
  useQuery({
    queryKey: ['stats'],
    queryFn: getStats,
    refetchInterval: 30000
  });
