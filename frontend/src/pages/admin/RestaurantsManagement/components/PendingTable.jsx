import React from 'react';
import { Eye, Check, X } from 'lucide-react';
import { Badge } from '../../../../components/common/Badge';
import { Button } from '../../../../components/common/Button';
import { Card, CardContent } from '../../../../components/common/Card';

export function PendingTable({
                                           restaurants, selectedForApproval, isAllSelected,
                                           onToggleSelection, onSelectAll, onSelect, onApprove, onRejectClick, formatDate
                                       }) {
    return (
        <Card>
            <CardContent className="table-card-content">
                <div className="table-responsive">
                    <table className="data-table">
                        <thead>
                        <tr>
                            <th className="checkbox-cell">
                                <input
                                    type="checkbox"
                                    className="custom-checkbox"
                                    checked={isAllSelected}
                                    onChange={onSelectAll}
                                />
                            </th>
                            <th>Name</th>
                            <th>Owner</th>
                            <th>Description</th>
                            <th>Submitted</th>
                            <th>Status</th>
                            <th>Actions</th>
                        </tr>
                        </thead>
                        <tbody>
                        {restaurants.map((restaurant) => (
                            <tr key={restaurant.id}>
                                <td className="checkbox-cell">
                                    <input
                                        type="checkbox"
                                        className="custom-checkbox"
                                        checked={selectedForApproval.has(restaurant.id)}
                                        onChange={() => onToggleSelection(restaurant.id)}
                                    />
                                </td>
                                <td>{restaurant.name}</td>
                                <td>
                                    <div className="owner-info">
                                        <div className="owner-name">{restaurant.ownerName}</div>
                                        <div className="owner-email">ID: {restaurant.ownerId}</div>
                                    </div>
                                </td>
                                <td className="truncate max-w-xs">{restaurant.description}</td>
                                <td className="text-muted">{formatDate(restaurant.createdAt)}</td>
                                <td>
                                    <Badge status="pending">Pending</Badge>
                                </td>
                                <td>
                                    <div className="table-actions">
                                        <Button
                                            className="btn-outline"
                                            onClick={() => onSelect(restaurant)}
                                        >
                                            <Eye className="action-icon" /> View
                                        </Button>
                                        <Button
                                            className="btn-approve"
                                            onClick={() => onApprove(restaurant.id)}
                                        >
                                            <Check className="action-icon" />
                                        </Button>
                                        <Button
                                            className="btn-danger"
                                            onClick={() => onRejectClick(restaurant.id)}
                                        >
                                            <X className="action-icon" />
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