import React from 'react';
import { ArrowUp, ArrowDown } from 'lucide-react';
import { clsx } from 'clsx';

export default function KPIGrid({ data }) {
    return (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 mb-8">
            {data.map((metric, index) => (
                <div key={index} className="bg-white rounded-3xl p-6 shadow-xl shadow-slate-200/50 hover:shadow-2xl hover:shadow-slate-300/50 transition-all duration-300">
                    <div className="flex items-start justify-between mb-4">
                        <div className={clsx('w-14 h-14 rounded-2xl flex items-center justify-center', metric.bgColor)}>
                            <metric.icon size={28} className={metric.iconColor} />
                        </div>
                    </div>
                    <h3 className="text-sm text-slate-500 mb-2">{metric.title}</h3>
                    <p className="text-2xl font-bold text-slate-800 mb-3">{metric.value}</p>
                    <div className="flex items-center gap-1.5">
                        {metric.trend > 0 ? (
                            <>
                                <ArrowUp size={16} className="text-emerald-600" />
                                <span className="text-sm font-semibold text-emerald-600">+{metric.trend}%</span>
                            </>
                        ) : (
                            <>
                                <ArrowDown size={16} className="text-red-600" />
                                <span className="text-sm font-semibold text-red-600">{metric.trend}%</span>
                            </>
                        )}
                        <span className="text-xs text-slate-500 ml-1">{metric.trendLabel}</span>
                    </div>
                </div>
            ))}
        </div>
    );
}