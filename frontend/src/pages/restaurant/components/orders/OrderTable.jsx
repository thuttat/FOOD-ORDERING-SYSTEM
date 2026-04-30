import React from 'react';

export default function OrderTable({ history, loading }) {
  if (loading) {
    return <div className="text-slate-500 py-4">Loading...</div>;
  }

  if (!history || history.length === 0) {
    return <div className="text-center py-10 text-slate-400">No orders found.</div>;
  }

  return (
    <div className="overflow-x-auto">
      <table className="w-full text-left border-collapse">
        <thead>
          <tr className="border-b border-slate-100 text-slate-400 text-sm uppercase">
            <th className="py-4 px-2">Order ID</th>
            <th className="py-4 px-2">Customer</th>
            <th className="py-4 px-2">Date</th>
            <th className="py-4 px-2">Total</th>
            <th className="py-4 px-2">Status</th>
          </tr>
        </thead>
        <tbody>
          {history.map(order => (
            <tr key={order.id} className="border-b border-slate-50 hover:bg-slate-50 transition-colors">
              <td className="py-4 px-2 font-bold text-slate-700">#{order.id}</td>
              <td className="py-4 px-2 text-slate-600">{order.customerName}</td>
              <td className="py-4 px-2 text-slate-500 text-sm">
                {new Date(order.createdAt).toLocaleDateString()}
              </td>
              <td className="py-4 px-2 font-semibold text-slate-900">
                {order.totalAmount?.toLocaleString()} VND
              </td>
              <td className="py-4 px-2">
                <span className={`px-3 py-1 rounded-full text-[10px] font-bold ${
                  order.status === 'DELIVERED' ? 'bg-emerald-100 text-emerald-600' : 'bg-red-100 text-red-600'
                }`}>
                  {order.status}
                </span>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}