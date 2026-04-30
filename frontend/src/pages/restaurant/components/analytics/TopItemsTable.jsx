import React from 'react';
import { FileText, ArrowUp, ArrowDown } from 'lucide-react';
import Sparkline from '../shared/Sparkline';

export default function TopItemsTable({ data }) {
  return (
    <div className="bg-white rounded-3xl p-8 shadow-xl shadow-slate-200/50">
      <div className="flex items-center justify-between mb-6">
        <div>
          <h2 className="text-xl font-bold text-slate-800 mb-1">Top Performing Menu Items</h2>
          <p className="text-sm text-slate-500">Best sellers with trend analysis</p>
        </div>
        {/* <button className="text-sm font-medium text-emerald-600 hover:text-emerald-700 flex items-center gap-1">
          <FileText size={16} />
          View Full Report
        </button> */}
      </div>

      <div className="overflow-x-auto">
        <table className="w-full">
          <thead>
            <tr className="border-b-2 border-slate-100">
              <th className="text-left py-4 px-4 text-sm font-semibold text-slate-600">Rank</th>
              <th className="text-left py-4 px-4 text-sm font-semibold text-slate-600">Dish</th>
              <th className="text-left py-4 px-4 text-sm font-semibold text-slate-600">Price</th>
              <th className="text-left py-4 px-4 text-sm font-semibold text-slate-600">Qty Sold</th>
              <th className="text-left py-4 px-4 text-sm font-semibold text-slate-600">Net Revenue</th>
              <th className="text-left py-4 px-4 text-sm font-semibold text-slate-600">Trend</th>
            </tr>
          </thead>
          <tbody>
            {data.map((item) => (
              <tr
                key={item.rank}
                className="border-b border-slate-50 hover:bg-slate-50 transition-colors"
              >
                <td className="py-5 px-4">
                  <div className="flex items-center justify-center w-8 h-8 bg-emerald-100 text-emerald-700 rounded-full font-bold text-sm">
                    #{item.rank}
                  </div>
                </td>
                <td className="py-5 px-4">
                  <div className="flex items-center gap-3">
                    <img
                      src={item.image}
                      alt={item.name}
                      className="w-12 h-12 rounded-2xl shadow-md"
                    />
                    <div>
                      <p className="font-semibold text-slate-800">{item.name}</p>
                      <p className="text-xs text-slate-500">{item.category}</p>
                    </div>
                  </div>
                </td>
                <td className="py-5 px-4">
                  <span className="text-sm font-medium text-slate-700">{item.price}</span>
                </td>
                <td className="py-5 px-4">
                  <span className="text-sm font-semibold text-slate-800">{item.qtySold}</span>
                </td>
                <td className="py-5 px-4">
                  <span className="font-bold text-emerald-600">{item.revenue}</span>
                </td>
                <td className="py-5 px-4">
                  <div className="flex items-center gap-2">
                    <Sparkline
                      data={item.sparklineValues} 
                      color={item.trend === 'up' ? '#10B981' : '#EF4444'} 
                    />
                    <div className={`flex items-center font-medium ${item.trend === 'up' ? 'text-emerald-600' : 'text-red-600'}`}>
                      {item.trend === 'up' ? (
                        <ArrowUp size={16} />
                      ) : (
                        <ArrowDown size={16} />
                      )}
                      <span className="ml-1">{item.trendLabel}</span>
                    </div>
                  </div>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}