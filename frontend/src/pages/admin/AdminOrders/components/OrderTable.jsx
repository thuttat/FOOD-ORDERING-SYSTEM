import React, { useState } from 'react';
import { ChevronDown, ChevronUp, Mail } from 'lucide-react';
import { Badge } from '../../../../components/common/Badge';
import { Button } from '../../../../components/common/Button';
import { Card, CardContent } from '../../../../components/common/Card';
import { Pagination } from '../../../../components/common/Pagination';

export function OrderTable({ orders, isLoading, setActionModal, currentPage, totalPages, setCurrentPage }) {
    const [expandedOrder, setExpandedOrder] = useState(null);

    const toggleExpand = (orderId) => {
        setExpandedOrder(expandedOrder === orderId ? null : orderId);
    };

    const getStatusBadge = (status) => {
        const statusMap = {
            PENDING: 'pending',
            CONFIRMED: 'info',
            PREPARING: 'warning',
            OUT_FOR_DELIVERY: 'info',
            DELIVERED: 'active',
            CANCELLED: 'inactive',
            COMPLETED: 'active'
        };
        return statusMap[status] || 'pending';
    };

    const formatDate = (isoString) => {
        return new Date(isoString).toLocaleString('vi-VN', { hour: '2-digit', minute: '2-digit', day: '2-digit', month: '2-digit', year: 'numeric' });
    };

    const generateTimeline = (status, dateStr) => {
        const time = new Date(dateStr).toLocaleTimeString('vi-VN', { hour: '2-digit', minute: '2-digit' });
        const isCancelled = status === 'CANCELLED';
        return [
            { status: 'Placed', time: time, completed: true },
            { status: 'Preparing', time: (!['PENDING', 'CONFIRMED', 'CANCELLED'].includes(status)) ? time : '-', completed: !['PENDING', 'CONFIRMED', 'CANCELLED'].includes(status) },
            { status: 'Delivering', time: ['OUT_FOR_DELIVERY', 'DELIVERED', 'COMPLETED'].includes(status) ? time : '-', completed: ['OUT_FOR_DELIVERY', 'DELIVERED', 'COMPLETED'].includes(status) },
            { status: isCancelled ? 'Cancelled' : 'Completed', time: ['DELIVERED', 'COMPLETED', 'CANCELLED'].includes(status) ? time : '-', completed: ['DELIVERED', 'COMPLETED', 'CANCELLED'].includes(status) },
        ];
    };

    return (
        <Card>
            <CardContent className="table-card-content">
                {isLoading ? (
                    <div className="p-8 text-center text-muted">Loading orders...</div>
                ) : (
                    <div className="table-responsive">
                        <table className="orders-table">
                            <thead>
                            <tr>
                                <th className="expand-cell"></th>
                                <th>Order ID</th>
                                <th>Customer</th>
                                <th>Restaurant</th>
                                <th>Total</th>
                                <th>Status</th>
                                <th>Time</th>
                                <th>Actions</th>
                            </tr>
                            </thead>
                            <tbody>
                            {orders.length === 0 ? (
                                <tr><td colSpan={8} className="text-center p-4">No orders found.</td></tr>
                            ) : orders.map((order) => (
                                <React.Fragment key={order.id}>
                                    <tr className="orders-row">
                                        <td className="expand-cell">
                                            <button onClick={() => toggleExpand(order.id)} className="expand-btn">
                                                {expandedOrder === order.id ? <ChevronUp className="action-icon" /> : <ChevronDown className="action-icon" />}
                                            </button>
                                        </td>
                                        <td className="fw-600">#{order.id}</td>
                                        <td>
                                            <div className="customer-info">
                                                <div>{order.customerName}</div>
                                                <div className="text-muted text-sm">{order.customerEmail}</div>
                                            </div>
                                        </td>
                                        <td>{order.restaurantName}</td>
                                        <td className="fw-600">{order.totalAmount.toLocaleString()}₫</td>
                                        <td>
                                            <Badge status={getStatusBadge(order.status)}>
                                                {order.status.replace(/_/g, ' ')}
                                            </Badge>
                                        </td>
                                        <td className="text-muted">{formatDate(order.createdAt)}</td>
                                        <td>
                                            <div className="table-actions">
                                                {order.status !== 'CANCELLED' && order.status !== 'DELIVERED' && order.status !== 'COMPLETED' && (
                                                    <Button size="sm" className="btn-danger" onClick={() => setActionModal({ type: 'cancel', order })}>
                                                        Cancel
                                                    </Button>
                                                )}

                                                {(order.status === 'DELIVERED' || order.status === 'COMPLETED') && (
                                                    <Button size="sm" className="btn-outline" onClick={() => setActionModal({ type: 'refund', order })}>
                                                        Refund
                                                    </Button>
                                                )}
                                            </div>
                                        </td>
                                    </tr>

                                    {expandedOrder === order.id && (
                                        <tr>
                                            <td colSpan={8} className="expand-row">
                                                <div className="expand-grid">
                                                    <div className="expand-col">
                                                        <h4 className="expand-title">Order Items</h4>
                                                        <div className="items-list">
                                                            {order.items.map((item, idx) => (
                                                                <div key={idx} className="order-item">
                                                                    <div>
                                                                        <span>{item.itemName}</span>
                                                                        <span className="text-muted ml-2">x{item.quantity}</span>
                                                                    </div>
                                                                    <span>${item.unitPrice.toFixed(2)}</span>
                                                                </div>
                                                            ))}
                                                            <div className="order-total-row">
                                                                <span className="fw-600">Delivery Fee</span>
                                                                <span className="fw-600">${order.deliveryFee.toFixed(2)}</span>
                                                            </div>
                                                            <div className="order-total-row">
                                                                <span className="fw-600">Total</span>
                                                                <span className="fw-600">${order.totalAmount.toFixed(2)}</span>
                                                            </div>
                                                        </div>
                                                        <div className="payment-box">
                                                            <div className="text-muted text-sm mb-1">Delivery Address & Note</div>
                                                            <div className="fw-500 text-sm mb-2">{order.deliveryAddress}</div>
                                                            {order.customerNote && <div className="text-muted text-xs italic">Note: {order.customerNote}</div>}
                                                        </div>
                                                    </div>

                                                    <div className="expand-col">
                                                        <h4 className="expand-title">Order Timeline</h4>
                                                        <div className="timeline-list">
                                                            {generateTimeline(order.status, order.createdAt).map((step, idx) => (
                                                                <div key={idx} className="timeline-step">
                                                                    <div className={`dot ${step.completed ? 'active' : ''}`} />
                                                                    <div className="timeline-content">
                                                                        <div className={step.completed ? '' : 'text-muted'}>
                                                                            {step.status}
                                                                        </div>
                                                                        <div className="text-sm text-muted">
                                                                            {step.time !== '-' ? step.time : 'Pending'}
                                                                        </div>
                                                                    </div>
                                                                </div>
                                                            ))}
                                                        </div>

                                                        <div className="contact-actions">
                                                            <Button size="sm" className="btn-outline flex-1" onClick={() => window.location.href = `mailto:${order.customerEmail}`}>
                                                                <Mail className="action-icon mr-2" /> Email
                                                            </Button>
                                                        </div>
                                                    </div>
                                                </div>
                                            </td>
                                        </tr>
                                    )}
                                </React.Fragment>
                            ))}
                            </tbody>
                        </table>
                    </div>
                )}

                {!isLoading && totalPages > 1 && (
                    <div style={{ marginTop: '24px', display: 'flex', justifyContent: 'center' }}>
                        <Pagination
                            currentPage={currentPage}
                            totalPages={totalPages}
                            setCurrentPage={setCurrentPage}
                        />
                    </div>
                )}
            </CardContent>
        </Card>
    );
}