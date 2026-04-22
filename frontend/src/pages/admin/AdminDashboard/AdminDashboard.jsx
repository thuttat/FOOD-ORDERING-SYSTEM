import React, {useEffect, useState} from "react";
import { Users, Store, ShoppingBag, DollarSign } from "lucide-react";
import { StatsCard } from "../../../components/common/StatsCard.jsx";
import { RevenueChart } from "./components/RevenueChart.jsx";
import { OrdersPieChart } from "./components/OrdersPieChart.jsx";
import {TopRestaurantsTable} from "./components/TopRestaurantsTable.jsx";
import "./AdminDashboard.css"
import AdminDashboardService from "../../../apis/AdminDashboardService.js";


export function AdminDashboard() {
    const [stats, setStats] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchStats = async () => {
            try {
                setLoading(true);
                const response = await AdminDashboardService.getDashboardStats();
                const data = response.data ? response.data : response;
                setStats(data);
            } catch (error) {
                console.error("Error fetching dashboard stats: ", error);
                setError("Failed to load dashboard stats. Please try again later.");
            } finally {
                setLoading(false);
            }
        };
        fetchStats();
    }, []);

    if (loading) {
        return (
            <div className="container">
                <div className="admin-dashboard">
                    <h1>Loading...</h1>
                </div>
            </div>
        );
    }

    if (error) {
        return (
            <div className="container">
                <div className="admin-dashboard">
                    <h1 style={{ color: "var(--danger-color)" }}>Error</h1>
                    <p>{error}</p>
                </div>
            </div>
        );
    }

    if (!stats) return null;

    const statusColors = {
        "DELIVERED": "#10b981",
        "PENDING": "#f59e0b",
        "PREPARING": "#ff6b35",
        "CANCELLED": "#ef4444"
    };

    const formattedRevenueData = stats.monthlyRevenueChart.map(item => ({
        month: item.label,
        revenue: item.value
    }));

    const formattedOrderData = stats.orderStatusChart.map(item => ({
        name: item.status,
        value: item.count,
        color: statusColors[item.status] || "#9ca3af"
    }));

    return (
        <div className="container">
            <div className="admin-dashboard">
                <h1>Admin Dashboard</h1>
                <div className="stats-grid">
                    <StatsCard
                        title="Total Active Users"
                        value={stats.totalActiveUsers.toLocaleString()}
                        icon={<Users />}
                        iconClass="primary"
                        subtext="Across the system"
                        subtextClass="success"
                    />

                    <StatsCard
                        title="Active Restaurants"
                        value={stats.totalActiveRestaurants.toLocaleString()}
                        subtext="Verified partners"
                        icon={<Store />}
                        iconClass="primary"
                        subtextClass="success"
                    />

                    <StatsCard
                        title="Orders This Month"
                        value={stats.totalOrdersThisMonth.toLocaleString()}
                        subtext="Completed & Processing"
                        icon={<ShoppingBag />}
                        iconClass="primary"
                        subtextClass="success"
                    />

                    <StatsCard
                        title="Monthly Revenue"
                        value={`${stats.revenueThisMonth.toLocaleString()}₫`}
                        subtext="Gross volume"
                        icon={<DollarSign />}
                        iconClass="success"
                        subtextClass="success"
                    />
                </div>

                <div className="chart-grid">
                    <RevenueChart data={formattedRevenueData} />
                    <OrdersPieChart data={formattedOrderData} />
                </div>

                <TopRestaurantsTable data={stats.topRestaurants} />
            </div>
        </div>
    );
}