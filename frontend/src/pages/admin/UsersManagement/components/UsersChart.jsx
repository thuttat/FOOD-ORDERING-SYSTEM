import React from "react";
import { Card, CardContent } from "../../../../components/common/Card";
import {
    LineChart,
    Line,
    XAxis,
    YAxis,
    Tooltip,
    ResponsiveContainer
} from "recharts";

export default function UsersChart({ growthData }) {
    return (
        <Card>
            <CardContent className="chart">
                <div className="chart-header">
                    <h3>User Growth (System Wide)</h3>
                </div>

                <ResponsiveContainer width="100%" height={200}>
                    <LineChart data={growthData}>
                        <XAxis dataKey="month" />
                        <YAxis />
                        <Tooltip />
                        <Line
                            dataKey="users"
                            type="monotone"
                            stroke="#8884d8"
                        />
                    </LineChart>
                </ResponsiveContainer>
            </CardContent>
        </Card>
    );
}