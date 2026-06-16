import { createContext, useState, useCallback, type ReactNode } from 'react';
import type { AuthResponse, Role } from '../types/auth';
import { login as loginApi, register as registerApi } from '../api/auth';
import type { LoginRequest, RegisterRequest } from '../types/auth';

interface AuthContextType {
  user: AuthResponse | null;
  isAuthenticated: boolean;
  hasRole: (role: Role) => boolean;
  login: (data: LoginRequest) => Promise<void>;
  register: (data: RegisterRequest) => Promise<void>;
  logout: () => void;
}

export const AuthContext = createContext<AuthContextType>({} as AuthContextType);

export function AuthProvider({ children }: { children: ReactNode }) {
  const [user, setUser] = useState<AuthResponse | null>(() => {
    const stored = localStorage.getItem('user');
    return stored ? JSON.parse(stored) : null;
  });

  const login = useCallback(async (data: LoginRequest) => {
    const response = await loginApi(data);
    localStorage.setItem('token', response.token);
    localStorage.setItem('refreshToken', response.refreshToken);
    localStorage.setItem('user', JSON.stringify(response));
    setUser(response);
  }, []);

  const register = useCallback(async (data: RegisterRequest) => {
    const response = await registerApi(data);
    localStorage.setItem('token', response.token);
    localStorage.setItem('refreshToken', response.refreshToken);
    localStorage.setItem('user', JSON.stringify(response));
    setUser(response);
  }, []);

  const logout = useCallback(() => {
    localStorage.removeItem('token');
    localStorage.removeItem('refreshToken');
    localStorage.removeItem('user');
    setUser(null);
  }, []);

  const hasRole = useCallback((role: Role) => user?.role === role, [user]);

  return (
    <AuthContext.Provider value={{
      user,
      isAuthenticated: !!user,
      hasRole,
      login,
      register,
      logout
    }}>
      {children}
    </AuthContext.Provider>
  );
}
