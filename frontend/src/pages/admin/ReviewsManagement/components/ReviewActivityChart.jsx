import React from 'react';
import { Card, CardContent } from '../../../../components/common/Card';
import {BarChart, CartesianGrid, XAxis, YAxis, Tooltip, Bar, ResponsiveContainer} from 'recharts';

export default function ReviewActivityChart({ data }) {
    return (
        <Card>
            <CardContent>
                <h3>Review Activity (Monthly)</h3>
                <ResponsiveContainer width="100%" height={300}>
                    <BarChart data={data}>
                        <CartesianGrid strokeDasharray="3 3" vertical={false} />
                        <XAxis dataKey="month" />
                        <YAxis />
                        <Tooltip />
                        <Bar dataKey="count" fill="#3b82f6" radius={[4, 4, 0, 0]} />
                    </BarChart>
                </ResponsiveContainer>
            </CardContent>
        </Card>
    );
}