export type Role = 'ADMIN' | 'OPERADOR' | 'CIUDADANO';

export interface AuthResponse {
  token: string;
  refreshToken: string;
  username: string;
  email: string;
  role: Role;
}

export interface LoginRequest {
  username: string;
  password: string;
}

export interface RegisterRequest {
  username: string;
  email: string;
  password: string;
  role?: Role;
}
