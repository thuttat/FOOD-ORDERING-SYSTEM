import React, { useEffect, useState, useRef } from 'react';
import { Volume2, VolumeX, AlertCircle } from 'lucide-react';
import { Client } from '@stomp/stompjs';
import SockJS from 'sockjs-client';
import axiosClient from '../../../apis/AxiosClient'; 
import ConnectionStatus from '../components/shared/ConnectionStatus';
import OrderCard from '../components/orders/OrderCard';

import { useAuth } from '../../../services/AuthContext';


export default function RealtimeOrders() {
  // const [soundEnabled, setSoundEnabled] = useState(true);
  const [connected, setConnected] = useState(false);
  const [orders, setOrders] = useState([]);
  // const soundRef = useRef(soundEnabled);
  // useEffect(() => { soundRef.current = soundEnabled; }, [soundEnabled]);

  const { user } = useAuth();

  const fetchOrders = async () => {
    try {
      const response = await axiosClient.get("/orders/active");
      setOrders(Array.isArray(response) ? response : response.data || []);
    } catch (error) {
      console.error("Failed to fetch orders:", error);
    }
  };

  useEffect(() => {
    fetchOrders();
    const socket = new SockJS('http://localhost:8081/ws');
    const stompClient = new Client({
      webSocketFactory: () => socket,
      reconnectDelay: 5000,
      heartbeatIncoming: 4000,
      heartbeatOutgoing: 4000,
      onConnect: () => {
        setConnected(true);
        stompClient.subscribe('/topic/orders', (message) => {
          const newOrder = JSON.parse(message.body);
          if (newOrder.restaurantId === user?.restaurantId) {
            setOrders(prev => {
              if (prev.find(o => o.id === newOrder.id)) return prev;
              return [newOrder, ...prev];
            });
          
          // if (soundRef.current) {
          //   const audio = new Audio('/assets/notification.mp3');
          //   audio.play().catch(() => console.log("Audio blocked"));
          // }
        } else {
             console.log("Orders from other restaurants have been ignored.:", newOrder.id);
          }
        });
      },
      onDisconnect: () => setConnected(false),
      onStompError: () => setConnected(false)
    });

    stompClient.activate();

    return () => stompClient.deactivate();
  }, [user]);

  const handleUpdateStatus = async (orderId, newStatus) => {
    if (['DELIVERED', 'CANCELLED'].includes(newStatus)) {
      if (!window.confirm(`Update order #${orderId} to ${newStatus}?`)) return;
    }

    try {
      await axiosClient.patch(`/orders/${orderId}/status`, null, {
        params: { status: newStatus }
      });

      if (['DELIVERED', 'CANCELLED'].includes(newStatus)) {
        setOrders(prev => prev.filter(o => o.id !== orderId));
      } else {
        setOrders(prev => prev.map(o => o.id === orderId ? { ...o, status: newStatus } : o));
      }
    } catch (err) {
      alert("Error updating order status.");
    }
  };

  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between">
        <div>
          <h2 className="font-bold text-slate-900 mb-1 text-2xl">Real-time Orders</h2>
          <p className="text-sm text-slate-500">Kitchen Display System (KDS)</p>
        </div>
        <div className="flex items-center gap-4">
          {/* <button
            onClick={() => setSoundEnabled(!soundEnabled)}
            className="p-3 bg-white rounded-xl shadow-sm border hover:bg-slate-50 transition-all"
          >
            {soundEnabled ? <Volume2 size={20} className="text-emerald-500" /> : <VolumeX size={20} className="text-slate-400" />}
          </button> */}
          <ConnectionStatus connected={connected} />
        </div>
      </div>

      {orders.length > 0 ? (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {orders.map((order) => (
            <OrderCard key={order.id} order={order} onUpdateStatus={handleUpdateStatus} />
          ))}
        </div>
      ) : (
        <div className="flex flex-col items-center justify-center py-20 bg-white rounded-3xl border border-dashed border-slate-200">
          <AlertCircle size={48} className="text-slate-300 mb-4" />
          <h3 className="font-bold text-slate-900">No Active Orders</h3>
          <p className="text-slate-500 text-sm">Waiting for new orders...</p>
        </div>
      )}
    </div>
  );
}