import React from "react";
import { Users, Store, ShoppingBag, DollarSign } from "lucide-react";
import { StatsCard } from "../../../components/common/StatsCard.jsx";
import { RevenueChart } from "./components/RevenueChart.jsx";
import { OrdersPieChart } from "./components/OrdersPieChart.jsx";
import {TopRestaurantsTable} from "./components/TopRestaurantsTable.jsx";
import "./AdminDashboard.css"


const ordersByStatus = [
    { name: "Completed", value: 45, color: "#10b981" },
    { name: "Preparing", value: 20, color: "#ff6b35" },
    { name: "Pending", value: 15, color: "#f59e0b" },
    { name: "Cancelled", value: 5, color: "#ef4444" },
];

const revenueData = [
    { month: "Jan", revenue: 15000 },
    { month: "Feb", revenue: 18000 },
    { month: "Mar", revenue: 22000 },
    { month: "Apr", revenue: 25000 },
];

const topRestaurants = [
    { name: "Burger Palace", orders: 234, revenue: 5678, rating: 4.5, status: "active" },
    { name: "Sushi Haven", orders: 189, revenue: 4321, rating: 4.8, status: "active" },
    { name: "Pizza Corner", orders: 156, revenue: 3456, rating: 4.3, status: "active" },
    { name: "Thai Delight", orders: 123, revenue: 2890, rating: 4.6, status: "active" },
];

export function AdminDashboard() {
    return (
        <div className="container">
            <div className="admin-dashboard">
                <h1>Admin Dashboard</h1>
                <div className="stats-grid">
                    <StatsCard
                        title="Total Users"
                        value="1,234"
                        icon={<Users />}
                        iconClass="primary"
                        subtext="+156 this month"
                        subtextClass="success"
                    />

                    <StatsCard
                        title="Restaurants"
                        value="89"
                        subtext="+12 this month"
                        icon={<Store />}
                        iconClass="primary"
                        subtextClass="success"
                    />

                    <StatsCard
                        title="Total Orders"
                        value="5,678"
                        subtext="+432 this week"
                        icon={<ShoppingBag />}
                        iconClass="primary"
                        subtextClass="success"
                    />

                    <StatsCard
                        title="Revenue"
                        value="$125.4K"
                        subtext="+18% this month"
                        icon={<DollarSign />}
                        iconClass="success"
                        subtextClass="success"
                    />
                </div>

                <div className="chart-grid">
                    <RevenueChart data={revenueData} />
                    <OrdersPieChart data={ordersByStatus} />
                </div>

                <TopRestaurantsTable data={topRestaurants} />
            </div>
        </div>
    );
}