import { Clock, User, Phone, Package } from 'lucide-react';
import { clsx } from 'clsx';

export default function OrderCard({ order, onUpdateStatus }) {
 const statusConfig = {
    PENDING: { label: 'New Order', color: 'bg-orange-100 text-orange-600', pulse: true },
    CONFIRMED: { label: 'Confirmed', color: 'bg-blue-100 text-blue-600', pulse: false },
    PREPARING: { label: 'Cooking', color: 'bg-indigo-100 text-indigo-600', pulse: true },
    READY: { label: 'Ready', color: 'bg-yellow-100 text-yellow-600', pulse: true }, 
    OUT_FOR_DELIVERY: { label: 'On Delivery', color: 'bg-purple-100 text-purple-600', pulse: false },
    DELIVERED: { label: 'Delivered', color: 'bg-emerald-100 text-emerald-600', pulse: false },
    CANCELLED: { label: 'Cancelled', color: 'bg-red-100 text-red-600', pulse: false },
  };

 
  const renderActions = () => {
    switch (order.status) {
      case 'PENDING':
        return (
          <div className="grid grid-cols-2 gap-3">
            <button
              onClick={() => onUpdateStatus(order.id, 'CONFIRMED')}
              className="px-4 py-2.5 bg-teal-500 text-white rounded-xl font-bold hover:bg-teal-600 transition-all shadow-lg shadow-teal-200"
            >
              Accept
            </button>
            <button
              onClick={() => {
                if (window.confirm(`Are you sure you want to REJECT order #${order.id}?`)) {
                  onUpdateStatus(order.id, 'CANCELLED');
                }
              }}
              className="px-4 py-2.5 bg-white text-red-600 border-2 border-red-500 rounded-xl font-bold hover:bg-red-50 transition-all"
            >
              Reject
            </button>
          </div>
        );
      case 'CONFIRMED':
        return (
          <button
            onClick={() => onUpdateStatus(order.id, 'PREPARING')}
            className="w-full px-4 py-2.5 bg-blue-500 text-white rounded-xl font-bold hover:bg-blue-600 transition-all shadow-lg shadow-blue-200"
          >
            Start Cooking
          </button>
        );
      case 'PREPARING':
        return (
          <button
            onClick={() => onUpdateStatus(order.id, 'READY')}
            className="w-full px-4 py-2.5 bg-yellow-500 text-white rounded-xl font-bold hover:bg-yellow-600 transition-all shadow-lg shadow-yellow-200"
          >
            Finish Cooking
          </button>
        );
      case 'READY':
        return (
          <button
            onClick={() => onUpdateStatus(order.id, 'OUT_FOR_DELIVERY')}
            className="w-full px-4 py-2.5 bg-purple-500 text-white rounded-xl font-bold hover:bg-purple-600 transition-all shadow-lg shadow-purple-200"
          >
            Hand to Driver
          </button>
        );
      case 'OUT_FOR_DELIVERY':
        return (
          <button
            onClick={() => {
              if (window.confirm(`Confirm that order #${order.id} has been DELIVERED?`)) {
                onUpdateStatus(order.id, 'DELIVERED');
              }
            }}
            className="w-full px-4 py-2.5 bg-emerald-500 text-white rounded-xl font-bold hover:bg-emerald-600 transition-all shadow-lg shadow-emerald-200"
          >
            Complete Order
          </button>
        );
      default:
        return null;
    }
  };

  const status = statusConfig[order.status] || statusConfig.PENDING;

  return (
    <div className="bg-white rounded-3xl shadow-xl shadow-slate-200/50 p-6 space-y-4 hover:shadow-2xl hover:shadow-slate-300/50 transition-all duration-300 border border-slate-50">
      <div className="flex items-start justify-between">
        <div>
          <h3 className="font-bold text-slate-900 text-lg">#{order.id}</h3>
          <div className="flex items-center gap-1.5 text-slate-500 text-sm mt-1">
            <Clock size={14} />
            <span>{new Date(order.createdAt).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })}</span>
          </div>
        </div>
        <span
          className={clsx(
            'px-3 py-1 rounded-full text-[10px] font-bold uppercase tracking-widest',
            status.color,
            status.pulse && 'animate-pulse'
          )}
        >
          {status.label}
        </span>
      </div>

      <div className="space-y-2 pt-2 border-t border-slate-50">
        <div className="flex items-center gap-2 text-sm">
          <User size={16} className="text-slate-400" />
          <span className="text-slate-700 font-semibold">{order.customerName}</span>
        </div>
        <div className="flex items-center gap-2 text-sm">
          <Phone size={16} className="text-slate-400" />
          <span className="text-slate-700 font-mono text-xs">{order.customerPhone || '09xx.xxx.xxx'}</span>
        </div>
      </div>

      <div className="space-y-2 pt-2 border-t border-slate-50">
        <div className="flex items-center gap-2 text-xs font-bold text-slate-400 uppercase tracking-tighter">
          <Package size={14} />
          <span>Order Items</span>
        </div>
        <ul className="space-y-2 pl-1">
          {order.items?.map((item, index) => (
            <li key={index} className="text-sm text-slate-600 flex justify-between items-center">
              <span className="flex-1 truncate pr-2">{item.itemName}</span>
              <span className="font-bold text-slate-900 bg-slate-100 px-2 py-0.5 rounded-md text-xs">x{item.quantity}</span>
            </li>
          ))}
        </ul>
      </div>

      <div className="pt-2 border-t border-slate-50">
        <div className="flex items-center justify-between">
          <span className="text-slate-400 text-sm font-medium">Total Amount</span>
          <span className="font-extrabold text-xl text-slate-900">
            {order.totalAmount?.toLocaleString()} <span className="text-sm font-normal text-slate-500">VND</span>
          </span>
        </div>
      </div>

      <div className="pt-2">
        {renderActions()}
      </div>
    </div>
  );
}