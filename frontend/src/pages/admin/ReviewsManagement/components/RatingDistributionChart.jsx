import React from 'react';
import { Card, CardContent } from '../../../../components/common/Card';
import { PieChart, Pie, Cell, Tooltip, ResponsiveContainer } from 'recharts';

export default function RatingDistributionChart({ data }) {
    return (
        <Card>
            <CardContent>
                <h3>Rating Distribution</h3>
                <ResponsiveContainer width="100%" height={300}>
                    <PieChart>
                        <Pie
                            data={data}
                            dataKey="count"
                            nameKey="rating"
                            cx="50%"
                            cy="50%"
                            outerRadius={100}
                            label={({name, percent}) =>
                                `${name} ${(percent * 100).toFixed(0)}%`
                            }
                        >
                            {data && data.map((entry, i) => (
                                <Cell key={i} fill={entry.color} />
                            ))}
                        </Pie>
                        <Tooltip />
                    </PieChart>
                </ResponsiveContainer>
            </CardContent>
        </Card>
    );
}