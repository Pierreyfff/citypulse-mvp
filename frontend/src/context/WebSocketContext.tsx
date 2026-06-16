import { createContext, useEffect, useState, type ReactNode } from 'react';

interface WebSocketContextType {
  connected: boolean;
  lastMessage: string | null;
}

export const WebSocketContext = createContext<WebSocketContextType>({
  connected: false,
  lastMessage: null
});

export function WebSocketProvider({ children }: { children: ReactNode }) {
  const [connected, setConnected] = useState(false);
  const [lastMessage, setLastMessage] = useState<string | null>(null);
  const [ws, setWs] = useState<WebSocket | null>(null);

  useEffect(() => {
    const protocol = window.location.protocol === 'https:' ? 'wss:' : 'ws:';
    const host = window.location.host;
    const socket = new WebSocket(`${protocol}//${host}/ws/incidents`);

    socket.onopen = () => setConnected(true);
    socket.onclose = () => setConnected(false);
    socket.onmessage = (event) => setLastMessage(event.data);

    setWs(socket);
    return () => socket.close();
  }, []);

  return (
    <WebSocketContext.Provider value={{ connected, lastMessage }}>
      {children}
    </WebSocketContext.Provider>
  );
}
