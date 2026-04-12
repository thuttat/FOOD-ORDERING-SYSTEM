import React from "react";
import { Card, CardContent } from "./Card";
import "../styles/StatsCard.css";

export function StatsCard({ title, value, icon, subtext, subtextClass, iconClass }) {
    return (
        <Card>
            <CardContent className="stat-card-content">
                <div className="stat-header">
                    <span className="stat-label">{title}</span>
                    {icon && <div className={`stat-icon ${iconClass}`}>{icon}</div>}
                </div>
                <p className="stat-value">{value}</p>
                {subtext && <span className={`stat-subtext ${subtextClass}`}>{subtext}</span>}
            </CardContent>
        </Card>
    );
}