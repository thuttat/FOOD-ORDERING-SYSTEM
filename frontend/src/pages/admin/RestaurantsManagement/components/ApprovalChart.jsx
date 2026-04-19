import React from 'react';
import { Card, CardContent } from '../../../../components/common/Card';
import { BarChart, Bar, XAxis, YAxis, Tooltip, ResponsiveContainer } from 'recharts';

export function ApprovalChart({ data }) {
    return (
        <Card>
            <CardContent className="chart-card-content">
                <h3 className="chart-title">Approval Trends</h3>
                <ResponsiveContainer width="100%" height={200}>
                    <BarChart data={data}>
                        <XAxis dataKey="month" stroke="#6b7280" />
                        <YAxis stroke="#6b7280" />
                        <Tooltip
                            contentStyle={{
                                backgroundColor: 'var(--bg-card, #ffffff)',
                                border: '1px solid var(--border-color, #e5e7eb)',
                                borderRadius: '8px',
                            }}
                        />
                        <Bar dataKey="approved" fill="#10b981" radius={[4, 4, 0, 0]} />
                        <Bar dataKey="rejected" fill="#ef4444" radius={[4, 4, 0, 0]} />
                    </BarChart>
                </ResponsiveContainer>
            </CardContent>
        </Card>
    );
}