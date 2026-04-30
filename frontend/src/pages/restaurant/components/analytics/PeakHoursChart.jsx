import React from 'react';
import {ResponsiveContainer, BarChart, Bar, Cell, XAxis, YAxis, CartesianGrid, Tooltip} from 'recharts';

export default function PeakHoursChart({ data }) {
  return (
    <div className="bg-white rounded-3xl p-8 shadow-xl shadow-slate-200/50">
      <div className="mb-6">
        <h2 className="text-xl font-bold text-slate-800 mb-1">Busiest Hours</h2>
        <p className="text-sm text-slate-500">Order volume by hour of day</p>
      </div>
      <ResponsiveContainer width="100%" height={320}>
        <BarChart data={data}>
          <CartesianGrid strokeDasharray="3 3" stroke="#E2E8F0" />
          <XAxis dataKey="hour" stroke="#94A3B8" style={{ fontSize: '11px' }} />
          <YAxis stroke="#94A3B8" style={{ fontSize: '12px' }} />
          <Tooltip
            contentStyle={{
              backgroundColor: '#fff',
              border: '1px solid #E2E8F0',
              borderRadius: '12px'
            }}
          />
          <Bar dataKey="orders" radius={[8, 8, 0, 0]}>
            {data.map((entry, index) => {
              const isPeakHour =
                (entry.hour >= '11:00' && entry.hour <= '13:00') ||
                (entry.hour >= '18:00' && entry.hour <= '20:00');
              return (
                <Cell
                  key={`cell-${index}`}
                  fill={isPeakHour ? '#14B8A6' : '#CBD5E1'}
                />
              );
            })}
          </Bar>
        </BarChart>
      </ResponsiveContainer>
    </div>
  );
}