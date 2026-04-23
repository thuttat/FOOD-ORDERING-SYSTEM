import React from "react";
import { StatsCard } from "../../../../components/common/StatsCard.jsx";
import { BadgeCheck, BadgeAlert, BadgeX, TrendingUp } from "lucide-react";

export function StatsSection({ active, pending, locked, rate }) {
    return (
        <div className="stats-grid">
            <StatsCard
                title="Active"
                value={active}
                subtext="Active restaurants"
                subtextClass="success"
                icon={<BadgeCheck />}
                iconClass="primary"
            />

            <StatsCard
                title="Pending Approval"
                value={pending}
                subtext="Needs review"
                subtextClass="warning"
                icon={<BadgeAlert />}
                iconClass="warning"
            />

            <StatsCard
                title="Suspended"
                value={locked}
                subtext="Requires action"
                subtextClass="destructive"
                icon={<BadgeX />}
                iconClass="destructive"
            />

            <StatsCard
                title="Approval Rate"
                value={`${rate}%`}
                subtext="Out of the total"
                subtextClass="muted"
                icon={<TrendingUp />}
                iconClass="success"
            />
        </div>
    );
}