import React from 'react';
import { Card, CardContent } from '../../../../components/common/Card';
import { BarChart, Bar, XAxis, YAxis, Tooltip, ResponsiveContainer } from 'recharts';

export function OrderChart({ chartData }) {
    return (
        <Card>
            <CardContent className="chart-card-content">
                <h3 className="chart-title">Order Activity (Last 7 Days)</h3>
                <ResponsiveContainer width="100%" height={250}>
                    <BarChart data={chartData}>
                        <XAxis dataKey="day" stroke="#6b7280" />
                        <YAxis stroke="#6b7280" />
                        <Tooltip
                            contentStyle={{
                                backgroundColor: 'var(--card, #ffffff)',
                                border: '1px solid var(--border, #e5e7eb)',
                                borderRadius: '8px',
                            }}
                        />
                        <Bar dataKey="completed" fill="#10b981" radius={[4, 4, 0, 0]} />
                        <Bar dataKey="cancelled" fill="#ef4444" radius={[4, 4, 0, 0]} />
                    </BarChart>
                </ResponsiveContainer>
            </CardContent>
        </Card>
    );
}