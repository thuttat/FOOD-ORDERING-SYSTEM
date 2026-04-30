import React from 'react';
import { PieChart, Pie, Cell, Tooltip, ResponsiveContainer } from 'recharts';

export default function CategoryPieChart({ data }) {
    const totalCategoryRevenue = data.reduce((sum, cat) => sum + cat.amount, 0);

    return (
        <div className="bg-white rounded-3xl p-8 shadow-xl shadow-slate-200/50">
            <div className="mb-6">
                <h2 className="text-xl font-bold text-slate-800 mb-1">Sales by Category</h2>
                <p className="text-sm text-slate-500">Revenue distribution breakdown</p>
            </div>
            <div className="relative">
                <ResponsiveContainer width="100%" height={280}>
                    <PieChart>
                        <Pie data={data} cx="50%" cy="50%" innerRadius={80} outerRadius={120} paddingAngle={5} dataKey="value">
                            {data.map((entry, index) => (
                                <Cell key={`cell-${index}`} fill={entry.color} />
                            ))}
                        </Pie>
                        <Tooltip contentStyle={{ borderRadius: '12px', border: 'none', boxShadow: '0 4px 6px -1px rgb(0 0 0 / 0.1)' }} />
                    </PieChart>
                </ResponsiveContainer>
                <div className="absolute top-1/2 left-1/2 -translate-x-1/2 -translate-y-1/2 text-center">
                    <p className="text-xs text-slate-500 mb-1">Total</p>
                    <p className="text-lg font-bold text-slate-800">{(totalCategoryRevenue / 1000000).toFixed(1)}M</p>
                    <p className="text-xs text-slate-500">VND</p>
                </div>
            </div>
            <div className="grid grid-cols-2 gap-3 mt-6">
                {data.map((item, index) => (
                    <div key={index} className="flex items-center justify-between">
                        <div className="flex items-center gap-2">
                            <div className="w-3 h-3 rounded-full" style={{ backgroundColor: item.color }}></div>
                            <span className="text-sm text-slate-700">{item.name}</span>
                        </div>
                        <span className="text-sm font-semibold text-slate-800">{item.value}%</span>
                    </div>
                ))}
            </div>
        </div>
    );
}