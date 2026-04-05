import React from "react";
import { Card, CardContent } from "../../../components/common/Card.jsx";

export function StatsCard({ title, value, icon, growth, iconClass }) {
    return (
        <Card>
            <CardContent className="stat-card">
                <div className="stat-header">
                    <span>{title}</span>
                    <div className={iconClass}>{icon}</div>
                </div>

                <p className="stat-value">{value}</p>
                <p className="stat-growth success">{growth}</p>
            </CardContent>
        </Card>
    );
}