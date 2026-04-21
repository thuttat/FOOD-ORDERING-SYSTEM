import { Clock, User, Phone, Package } from 'lucide-react';
import { clsx } from 'clsx';

export default function OrderCard({ order }) {
  const statusConfig = {
    new: {
      label: 'New',
      color: 'bg-orange-100 text-orange-600',
      pulse: true
    },
    preparing: {
      label: 'Preparing',
      color: 'bg-blue-100 text-blue-600',
      pulse: false
    },
    ready: {
      label: 'Ready for Pickup',
      color: 'bg-emerald-100 text-emerald-600',
      pulse: false
    }
  };

  const status = statusConfig[order.status] || statusConfig.new;

  return (
    <div className="bg-white rounded-3xl shadow-xl shadow-slate-200/50 p-6 space-y-4 hover:shadow-2xl hover:shadow-slate-300/50 transition-all duration-300">
      
      <div className="flex items-start justify-between">
        <div>
          <h3 className="font-bold text-slate-900">{order.id}</h3>
          <div className="flex items-center gap-1.5 text-slate-500 text-sm mt-1">
            <Clock size={14} />
            <span>{order.timeAgo}</span>
          </div>
        </div>
        <span
          className={clsx(
            'px-3 py-1 rounded-full text-xs font-semibold',
            status.color,
            status.pulse && 'animate-pulse'
          )}
        >
          {status.label}
        </span>
      </div>

      
      <div className="space-y-2 pt-2 border-t border-slate-100">
        <div className="flex items-center gap-2 text-sm">
          <User size={16} className="text-slate-400" />
          <span className="text-slate-700">{order.customerName}</span>
        </div>
        <div className="flex items-center gap-2 text-sm">
          <Phone size={16} className="text-slate-400" />
          <span className="text-slate-700">{order.customerPhone}</span>
        </div>
      </div>

      
      <div className="space-y-2 pt-2 border-t border-slate-100">
        <div className="flex items-center gap-2 text-sm font-semibold text-slate-600">
          <Package size={16} />
          <span>Items</span>
        </div>
        <ul className="space-y-1.5 pl-6">
          {order.items.map((item, index) => (
            <li key={index} className="text-sm text-slate-600">
              <span className="font-medium">{item.quantity}x</span> {item.name}
            </li>
          ))}
        </ul>
      </div>

      
      <div className="pt-2 border-t border-slate-100">
        <div className="flex items-center justify-between">
          <span className="text-slate-600">Total</span>
          <span className="font-bold text-lg text-slate-900">{order.total}</span>
        </div>
      </div>

      
      <div className="pt-2">
        {order.status === 'new' && (
          <div className="grid grid-cols-2 gap-3">
            <button className="px-4 py-2.5 bg-teal-500 text-white rounded-xl hover:bg-teal-600 transition-colors shadow-lg shadow-teal-200/50 font-medium">
              Accept
            </button>
            <button className="px-4 py-2.5 bg-white text-red-600 border-2 border-red-500 rounded-xl hover:bg-red-50 transition-colors font-medium">
              Reject
            </button>
          </div>
        )}
        {order.status === 'preparing' && (
          <button className="w-full px-4 py-2.5 bg-blue-500 text-white rounded-xl hover:bg-blue-600 transition-colors shadow-lg shadow-blue-200/50 font-medium">
            Mark as Ready
          </button>
        )}
        {order.status === 'ready' && (
          <button className="w-full px-4 py-2.5 bg-emerald-500 text-white rounded-xl hover:bg-emerald-600 transition-colors shadow-lg shadow-emerald-200/50 font-medium">
            Complete Order
          </button>
        )}
      </div>
    </div>
  );
}
