import React from 'react';
import { TrendingUp } from 'lucide-react';
import { ResponsiveContainer, ComposedChart, Line, Area, XAxis, YAxis, CartesianGrid, Tooltip, Legend} from 'recharts';

const CustomTooltip = ({ active, payload }) => {
  if (active && payload && payload.length) {
    return (
      <div className="bg-white p-4 rounded-xl shadow-lg border border-slate-200">
        <p className="text-sm font-semibold text-slate-800 mb-2">{payload[0].payload.date}</p>
        <p className="text-sm text-emerald-600 mb-1">
          Revenue: {payload[0].value.toLocaleString()} VND
        </p>
        <p className="text-sm text-blue-600">Orders: {payload[1]?.value || 0}</p>
      </div>
    );
  }
  return null;
};

export default function RevenueChart({ data }) {
  return (
    <div className="bg-white rounded-3xl p-8 shadow-xl shadow-slate-200/50 mb-8">
      <div className="flex items-center justify-between mb-6">
        <div>
          <h2 className="text-xl font-bold text-slate-800 mb-1">Revenue & Order Volume</h2>
          <p className="text-sm text-slate-500">Last 30 days performance trend</p>
        </div>
        <div className="flex items-center gap-2 text-emerald-600">
          <TrendingUp size={20} />
          <span className="text-sm font-semibold">+18.5% growth</span>
        </div>
      </div>
      <ResponsiveContainer width="100%" height={400}>
        <ComposedChart data={data}>
          <defs>
            <linearGradient id="colorRevenue" x1="0" y1="0" x2="0" y2="1">
              <stop offset="5%" stopColor="#10B981" stopOpacity={0.3} />
              <stop offset="95%" stopColor="#10B981" stopOpacity={0} />
            </linearGradient>
          </defs>
          <CartesianGrid strokeDasharray="3 3" stroke="#E2E8F0" />
          <XAxis dataKey="date" stroke="#94A3B8" style={{ fontSize: '12px' }} />
          <YAxis
            yAxisId="left"
            stroke="#10B981"
            style={{ fontSize: '12px' }}
            tickFormatter={(value) => `${(value / 1000000).toFixed(1)}M`}
          />
          <YAxis
            yAxisId="right"
            orientation="right"
            stroke="#3B82F6"
            style={{ fontSize: '12px' }}
          />
          <Tooltip content={<CustomTooltip />} />
          <Legend />
          <Area
            yAxisId="left"
            type="monotone"
            dataKey="revenue"
            stroke="#10B981"
            strokeWidth={3}
            fillOpacity={1}
            fill="url(#colorRevenue)"
            name="Revenue (VND)"
          />
          <Line
            yAxisId="right"
            type="monotone"
            dataKey="orders"
            stroke="#3B82F6"
            strokeWidth={2}
            strokeDasharray="5 5"
            dot={{ fill: '#3B82F6', r: 4 }}
            name="Number of Orders"
          />
        </ComposedChart>
      </ResponsiveContainer>
    </div>
  );
}