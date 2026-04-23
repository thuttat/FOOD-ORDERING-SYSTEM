import React from "react";
import { StatsCard } from "../../../../components/common/StatsCard.jsx";
import { UserPlus, UserCheck, ChefHat } from "lucide-react";

export default function UsersStats({ systemStats }) {
    return (
        <div className="stats-grid">
            <StatsCard
                title="Total Users"
                value={systemStats.total}
                subtext="Across the system"
                subtextClass="success"
                icon={<UserPlus />}
            />

            <StatsCard
                title="Customers"
                value={systemStats.activeCustomers}
                subtext="Active users"
                subtextClass="muted"
                icon={<UserCheck />}
            />

            <StatsCard
                title="Restaurants"
                value={systemStats.activeRestaurants}
                subtext="Partners"
                subtextClass="muted"
                icon={<ChefHat />}
            />
        </div>
    );
}