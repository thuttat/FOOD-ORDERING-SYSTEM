import React from 'react';
import { StatsCard } from '../../../../components/common/StatsCard';

export function OrderStats({ stats }) {
    return (
        <div className="stats-grid">
            <StatsCard
                title="Total Orders"
                value={stats.total}
                subtext="All time"
                subtextClass="success"
            />
            <StatsCard
                title="Pending"
                value={stats.pending}
                subtext="Needs attention"
                subtextClass="warning"
            />
            <StatsCard
                title="In Progress"
                value={stats.inProgress}
                subtext="Active"
                subtextClass="muted"
            />
            <StatsCard
                title="Completed"
                value={stats.delivered}
                subtext="In this month"
                subtextClass="blue"
            />
        </div>
    );
}