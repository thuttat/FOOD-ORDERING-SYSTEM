import React, { useState, useEffect } from 'react';
import AnalyticsHeader from '../components/analytics/AnalyticsHeader';
import KPIGrid from '../components/analytics/KPIGrid';
import RevenueChart from '../components/analytics/RevenueChart';
import PeakHoursChart from '../components/analytics/PeakHoursChart';
import CategoryPieChart from '../components/analytics/CategoryPieChart';
import TopItemsTable from '../components/analytics/TopItemsTable';
import { DollarSign, ShoppingBag, Receipt, AlertCircle } from 'lucide-react';
import axiosClient from '../../../apis/AxiosClient';

export default function AdvancedRestaurantAnalytics() {
    const [dateRange, setDateRange] = useState('Last 7 Days');
    const [comparePeriod, setComparePeriod] = useState('Previous Period');
    const [analyticsData, setAnalyticsData] = useState(null);
    const [loading, setLoading] = useState(true);
   

    useEffect(() => {
        const fetchAnalytics = async () => {
            try {
                const res = await axiosClient.get('/restaurants/analytics');
                console.log("DỮ LIỆU BACKEND TRẢ VỀ:", res.data);
                setAnalyticsData(res.data || res);
            } catch (error) {
                console.error("Error loading analytics data:", error);
            } finally {
                setLoading(false);
            }
        };
        fetchAnalytics();
    }, []);


    const handleExport = async () => {
    try {
        const response = await axiosClient.get('/analytics/export', {
            responseType: 'blob', 
            headers: {
                Authorization: `Bearer ${localStorage.getItem('token')}` 
            }
        });
        const url = window.URL.createObjectURL(new Blob([response.data]));
        const link = document.createElement('a');
        link.href = url;
        link.setAttribute('download', `Report_${new Date().toLocaleDateString()}.xlsx`);
        document.body.appendChild(link);
        link.click();
        link.parentNode.removeChild(link);
        window.URL.revokeObjectURL(url);
    } catch (error) {
        console.error("Error download:", error);
        alert("Unable to export report!");
    }
};

   
    if (loading || !analyticsData) {
        return (
            <div className="min-h-screen bg-[#F8FAFC] p-8 flex items-center justify-center">
                <div className="text-xl font-semibold text-slate-500 animate-pulse">
                    Loading analytics data...
                </div>
            </div>
        );
    }

    const dynamicKpiMetrics = [
        { title: 'Gross Revenue', value: `${analyticsData.totalRevenue?.toLocaleString() || 0} VND`, trend: analyticsData.revenueTrend, trendLabel: 'vs last week', icon: DollarSign, bgColor: 'bg-emerald-100', iconColor: 'text-emerald-600' },
        { title: 'Total Orders', value: analyticsData.totalOrders || 0, trend: analyticsData.ordersTrend, trendLabel: 'vs last week', icon: ShoppingBag, bgColor: 'bg-blue-100', iconColor: 'text-blue-600' },
        { title: 'Average Order Value', value: `${analyticsData.averageOrderValue?.toLocaleString() || 0} VND`, trend: analyticsData.aovTrend, trendLabel: 'vs last week', icon: Receipt, bgColor: 'bg-teal-100', iconColor: 'text-teal-600' },
        { title: 'Cancellation Rate', value: `${analyticsData.cancellationRate || 0}%`, trend: analyticsData.cancellationTrend, trendLabel: 'vs last week', icon: AlertCircle, bgColor: 'bg-red-100', iconColor: 'text-red-600' }
    ];

    return (
        <div className="min-h-screen bg-[#F8FAFC] p-8">
            <AnalyticsHeader
                dateRange={dateRange}
                comparePeriod={comparePeriod}
                onDateRangeChange={(newDate) => setDateRange(newDate)} 
                onCompareChange={(newCompare) => setComparePeriod(newCompare)} 
                onExport={handleExport} 
            />
            
            <KPIGrid data={dynamicKpiMetrics} />

            <RevenueChart data={analyticsData.revenueOrderData || []} />

            <div className="grid grid-cols-1 lg:grid-cols-2 gap-6 mb-8 w-full">
                <PeakHoursChart data={analyticsData.peakHoursData || []} />
                <CategoryPieChart data={analyticsData.salesByCategory || []} />
            </div>

            <TopItemsTable data={analyticsData.topMenuItems || []} />
        </div>
    );
}