import React, { useState, useEffect } from 'react';
import { Calendar, ChevronDown, Download } from 'lucide-react';

export default function AnalyticsHeader({ dateRange, comparePeriod,onDateRangeChange,onCompareChange,onExport}) {

    const [isDateOpen, setIsDateOpen] = useState(false);
    const [isCompareOpen, setIsCompareOpen] = useState(false);

   
    const dateOptions = ["Last 7 Days", "Last 30 Days", "This Month", "This Year"];
    const compareOptions = ["Previous Period", "Previous Year", "None"];

    return (
        <div className="mb-8">
            <div className="flex items-center justify-between mb-6">
                <div>
                    <h1 className="text-3xl font-bold text-slate-800 mb-2">Restaurant Analytics</h1>
                    <p className="text-slate-500">Comprehensive performance insights and metrics</p>
                </div>
                <div className="flex items-center gap-3">
                    <div className="relative">
                        <button 
                            onClick={() => {
                                setIsDateOpen(!isDateOpen);
                                setIsCompareOpen(false); 
                            }}
                            className="flex items-center gap-2 px-5 py-3 bg-white border border-slate-200 rounded-2xl hover:bg-slate-50 transition-colors shadow-sm"
                        >
                            <Calendar size={20} className="text-slate-600" />
                            <span className="font-medium text-slate-700">{dateRange}</span>
                            <ChevronDown size={18} className={`text-slate-400 transition-transform ${isDateOpen ? 'rotate-180' : ''}`} />
                        </button>
                        {isDateOpen && (
                            <div className="absolute right-0 mt-2 w-48 bg-white border border-slate-100 rounded-xl shadow-lg py-2 z-50">
                                {dateOptions.map((option) => (
                                    <button
                                        key={option}
                                        onClick={() => {
                                            onDateRangeChange(option); 
                                            setIsDateOpen(false);      
                                        }}
                                        className="w-full text-left px-4 py-2 text-sm text-slate-700 hover:bg-emerald-50 hover:text-emerald-600"
                                    >
                                        {option}
                                    </button>
                                ))}
                            </div>
                        )}
                    </div>

                    <div className="relative">
                        <button 
                            onClick={() => {
                                setIsCompareOpen(!isCompareOpen);
                                setIsDateOpen(false); 
                            }}
                            className="flex items-center gap-2 px-5 py-3 bg-white border border-slate-200 rounded-2xl hover:bg-slate-50 transition-colors shadow-sm"
                        >
                            <span className="text-sm text-slate-600">Compare to:</span>
                            <span className="font-medium text-slate-700">{comparePeriod}</span>
                            <ChevronDown size={18} className={`text-slate-400 transition-transform ${isCompareOpen ? 'rotate-180' : ''}`} />
                        </button>
                        {isCompareOpen && (
                            <div className="absolute right-0 mt-2 w-48 bg-white border border-slate-100 rounded-xl shadow-lg py-2 z-50">
                                {compareOptions.map((option) => (
                                    <button
                                        key={option}
                                        onClick={() => {
                                            onCompareChange(option);
                                            setIsCompareOpen(false);
                                        }}
                                        className="w-full text-left px-4 py-2 text-sm text-slate-700 hover:bg-emerald-50 hover:text-emerald-600"
                                    >
                                        {option}
                                    </button>
                                ))}
                            </div>
                        )}
                    </div>
                    <button 
                        onClick={onExport} 
                        className="flex items-center gap-2 px-5 py-3 bg-emerald-600 text-white rounded-2xl hover:bg-emerald-700 transition-colors shadow-lg shadow-emerald-200/50 active:scale-95"
                    >
                        <Download size={20} />
                        <span className="font-medium">Export Report</span>
                    </button>

                </div>
            </div>
        </div>
    );
}