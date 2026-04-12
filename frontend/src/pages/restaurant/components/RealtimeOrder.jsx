import { Volume2, VolumeX, AlertCircle } from 'lucide-react';
import { useState } from 'react';
import ConnectionStatus from './ConnectionStatus';
import OrderCard from './OrderCard';


export default function RealtimeOrders() {
  const [soundEnabled, setSoundEnabled] = useState(true);
  const [connected, setConnected] = useState(true);

  
  const orders = [
    {
      id: '#ORD-1234',
      timeAgo: '2 mins ago',
      status: 'new',
      customerName: 'Nguyễn Văn A',
      customerPhone: '+84 90 123 4567',
      items: [
        { quantity: 2, name: 'Classic Burger' },
        { quantity: 1, name: 'Cheese Pizza' }
      ],
      total: '250.000đ'
    },
    {
      id: '#ORD-1235',
      timeAgo: '5 mins ago',
      status: 'preparing',
      customerName: 'Trần Thị B',
      customerPhone: '+84 91 234 5678',
      items: [
        { quantity: 1, name: 'Design Apuurion' },
        { quantity: 3, name: 'Classic Burger' }
      ],
      total: '370.000đ'
    },
    {
      id: '#ORD-1236',
      timeAgo: '8 mins ago',
      status: 'ready',
      customerName: 'Lê Văn C',
      customerPhone: '+84 92 345 6789',
      items: [
        { quantity: 2, name: 'Cheese Pizza' }
      ],
      total: '240.000đ'
    },
    {
      id: '#ORD-1237',
      timeAgo: '1 min ago',
      status: 'new',
      customerName: 'Phạm Thị D',
      customerPhone: '+84 93 456 7890',
      items: [
        { quantity: 1, name: 'Classic Burger' },
        { quantity: 1, name: 'Cheese Pizza' },
        { quantity: 1, name: 'Design Apuurion' }
      ],
      total: '360.000đ'
    }
  ];

  return (
    <div className="space-y-6">
      
      <div className="flex items-center justify-between">
        <div>
          <h2 className="font-bold text-slate-900 mb-1">Real-time Orders</h2>
          <p className="text-sm text-slate-500">Kitchen Display System</p>
        </div>
        <div className="flex items-center gap-4">
          
          <button
            onClick={() => setSoundEnabled(!soundEnabled)}
            className="p-3 bg-white rounded-xl shadow-md hover:shadow-lg transition-all"
            title={soundEnabled ? 'Mute notifications' : 'Unmute notifications'}
          >
            {soundEnabled ? (
              <Volume2 size={20} className="text-slate-600" />
            ) : (
              <VolumeX size={20} className="text-slate-400" />
            )}
          </button>

         
          <ConnectionStatus connected={connected} />
        </div>
      </div>

      
      {orders.length > 0 ? (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {orders.map((order) => (
            <OrderCard key={order.id} order={order} />
          ))}
        </div>
      ) : (
       
        <div className="flex flex-col items-center justify-center py-20 bg-white rounded-3xl shadow-xl shadow-slate-200/50">
          <div className="w-24 h-24 bg-slate-100 rounded-full flex items-center justify-center mb-4">
            <AlertCircle size={48} className="text-slate-400" />
          </div>
          <h3 className="font-bold text-slate-900 mb-2">No Active Orders</h3>
          <p className="text-slate-500 text-sm">
            New orders will appear here in real-time
          </p>
        </div>
      )}
    </div>
  );
}
