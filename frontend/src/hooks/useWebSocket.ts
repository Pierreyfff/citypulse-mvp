import { useContext } from 'react';
import { WebSocketContext } from '../context/WebSocketContext';

export const useWebSocket = () => useContext(WebSocketContext);
