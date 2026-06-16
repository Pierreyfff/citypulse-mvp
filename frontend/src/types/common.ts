export interface User {
  id: number;
  username: string;
  email: string;
  role: string;
  active: boolean;
  createdAt: string;
}

export interface Pageable {
  page: number;
  size: number;
}
