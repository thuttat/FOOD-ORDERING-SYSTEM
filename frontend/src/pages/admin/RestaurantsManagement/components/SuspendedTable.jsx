import React from 'react';
import { Badge } from '../../../../components/common/Badge';
import { Button } from '../../../../components/common/Button';
import { Card, CardContent } from '../../../../components/common/Card';

export function SuspendedTable({ restaurants, onSelect, onReinstate, formatDate }) {
    return (
        <Card>
            <CardContent className="table-card-content">
                <div className="table-responsive">
                    <table className="data-table">
                        <thead>
                        <tr>
                            <th>Name</th>
                            <th>Owner</th>
                            <th>Suspended Date</th>
                            <th>Address</th>
                            <th>Status</th>
                            <th>Actions</th>
                        </tr>
                        </thead>
                        <tbody>
                        {restaurants.map((restaurant) => (
                            <tr key={restaurant.id}>
                                <td>{restaurant.name}</td>
                                <td>{restaurant.ownerName}</td>
                                <td className="text-muted">{formatDate(restaurant.updatedAt)}</td>
                                <td className="reason-cell">{restaurant.address}</td>
                                <td>
                                    <Badge status="suspended">Locked</Badge>
                                </td>
                                <td>
                                    <div className="table-actions">
                                        <Button
                                            className="btn-outline"
                                            onClick={() => onSelect(restaurant)}
                                        >
                                            Review
                                        </Button>
                                        <Button
                                            className="btn-approve"
                                            onClick={() => onReinstate(restaurant.id)}
                                        >
                                            Reinstate
                                        </Button>
                                    </div>
                                </td>
                            </tr>
                        ))}
                        </tbody>
                    </table>
                </div>
            </CardContent>
        </Card>
    );
}