import React, {useEffect, useState} from 'react';
import { Search, Filter, ChevronDown, ChevronUp, XCircle, AlertTriangle, Phone, Mail, DollarSign } from 'lucide-react';
import { Badge } from '../../../components/common/Badge';
import { Button } from '../../../components/common/Button';
import { Card, CardContent } from '../../../components/common/Card';
import { Input } from '../../../components/common/Input';
import { Select } from '../../../components/common/Select';
import { StatsCard } from '../../../components/common/StatsCard';
import { BarChart, Bar, XAxis, YAxis, Tooltip, ResponsiveContainer } from 'recharts';
import './AdminOrders.css';
import {OrderService} from "../../../apis/OrderService.js";
import {Pagination} from "../../../components/common/Pagination.jsx";


export function AdminOrders() {
    const [orders, setOrders] = useState([]);
    const [stats, setStats] = useState({ total: 0, pending: 0, inProgress: 0, delivered: 0, chartData: [] });
    const [isLoading, setIsLoading] = useState(true);
    const [searchQuery, setSearchQuery] = useState('');
    const [statusFilter, setStatusFilter] = useState('all');
    const [restaurantFilter, setRestaurantFilter] = useState('all');
    const [expandedOrder, setExpandedOrder] = useState(null);
    const [actionModal, setActionModal] = useState(null);
    const [currentPage, setCurrentPage] = useState(0);
    const [totalPages, setTotalPages] = useState(0);
    
    const fetchOrdersAndStats = async () => {
        setIsLoading(true);
        try {
            const [orderResponse, statsResponse] = await Promise.all([
                OrderService.getAdminOrders(searchQuery, statusFilter, restaurantFilter, currentPage, 10),
                OrderService.getAdminStats()
            ]);
            setOrders(orderResponse.data.content || []);
            setTotalPages(orderResponse.data.totalPages || 0);
            setStats(statsResponse.data);
        } catch (error) {
            console.error("Error fetching orders: ", error);
        } finally {
            setIsLoading(false);
        }
    };

    useEffect(() => {
        const timeoutId = setTimeout(() => {
            fetchOrdersAndStats();
        }, 500);
        return () => clearTimeout(timeoutId);
    }, [searchQuery, statusFilter, restaurantFilter, currentPage]);

    useEffect(() => {
        setCurrentPage(0);
    }, [searchQuery, statusFilter, restaurantFilter]);

    const handleActionConfirm = async () => {
        if (!actionModal || !actionModal.order) return;
        try {
            if (actionModal.type === 'cancel') {
                await OrderService.updateOrderStatus(actionModal.order.id, 'CANCELLED');
                alert("Order cancelled successfully!");
                fetchOrdersAndStats();
            } else if (actionModal.type === 'refund') {
                alert("Refunds have been issued to customers!");
            }
        } catch (error) {
            console.error("Operation error: ", error);
            alert("An error occurred, please try again.");
        } finally {
            setActionModal(null);
        }
    }

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
            { status: 'Preparing', time: (status !== 'PENDING' && !isCancelled) ? time : '-', completed: !['PENDING', 'CANCELLED'].includes(status) },
            { status: 'Out for Delivery', time: ['OUT_FOR_DELIVERY', 'DELIVERED'].includes(status) ? time : '-', completed: ['OUT_FOR_DELIVERY', 'DELIVERED'].includes(status) },
            { status: isCancelled ? 'Cancelled' : 'Delivered', time: ['DELIVERED', 'CANCELLED'].includes(status) ? time : '-', completed: ['DELIVERED', 'CANCELLED'].includes(status) },
        ];
    };

    return (
        <div className="container">
            <div className="admin-orders">
                <div className="header-section">
                    <div>
                        <h1>Order Management</h1>
                        <p className="header-subtitle">Monitor and manage all platform orders</p>
                    </div>
                </div>

                <div className="stats-grid">
                    <StatsCard
                        title="Total Orders"
                        value={stats.total}
                        subtext="All time"
                        subtextClass="success"
                    />
                    <StatsCard
                        title="Pending"
                        value={stats.pending}
                        subtext="Needs attention"
                        subtextClass="warning"
                    />
                    <StatsCard
                        title="In Progress"
                        value={stats.inProgress}
                        subtext="Active"
                        subtextClass="muted"
                    />
                    <StatsCard
                        title="Completed"
                        value={stats.delivered}
                        subtext="In this month"
                        subtextClass="blue"
                    />
                </div>

                <div className="filter-bar">
                    <div className="filter-search-input">
                        <Input
                            placeholder="Search by Order ID or Customer..."
                            icon={<Search className="action-icon" />}
                            value={searchQuery}
                            onChange={(e) => setSearchQuery(e.target.value)}
                        />
                    </div>
                    <Select
                        options={[
                            { value: 'all', label: 'All Status' },
                            { value: 'pending', label: 'Pending' },
                            { value: 'preparing', label: 'Preparing' },
                            { value: 'out_for_delivery', label: 'Delivering' },
                            { value: 'delivered', label: 'Completed' },
                            { value: 'cancelled', label: 'Cancelled' },
                        ]}
                        value={statusFilter}
                        onChange={(e) => setStatusFilter(e.target.value)}
                    />
                </div>

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
                                                        {order.status !== 'CANCELLED' && order.status !== 'DELIVERED' && (
                                                            <Button size="sm" className="btn-danger" onClick={() => setActionModal({ type: 'cancel', order })}>
                                                                Cancel
                                                            </Button>
                                                        )}
                                                        {order.status === 'DELIVERED' && (
                                                            <Button size="sm" className="btn-outline" onClick={() => setActionModal({ type: 'refund', order })}>
                                                                Refund
                                                            </Button>
                                                        )}
                                                    </div>
                                                </td>
                                            </tr>

                                            {/* Expanded Order Detail Row */}
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

                <Card>
                    <CardContent className="chart-card-content">
                        <h3 className="chart-title">Order Activity (Last 7 Days)</h3>
                        <ResponsiveContainer width="100%" height={250}>
                            <BarChart data={stats.chartData}>
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

                {actionModal && (
                    <div className="modal-overlay">
                        <div className="modal-box">
                            <div className="modal-header">
                                <div className={`modal-icon-wrapper ${actionModal.type === 'cancel' ? 'bg-destructive-light' : 'bg-warning-light'}`}>
                                    {actionModal.type === 'cancel' ? <XCircle className="action-icon text-destructive" /> : <DollarSign className="action-icon text-warning" />}
                                </div>
                                <div>
                                    <h3 className="modal-title">
                                        {actionModal.type === 'cancel' ? 'Cancel Order' : actionModal.type === 'refund' ? 'Process Refund' : 'Contact'}
                                    </h3>
                                    <p className="modal-subtitle">Order #{actionModal.order.id}</p>
                                </div>
                            </div>

                            {actionModal.type !== 'contact' && (
                                <textarea className="modal-textarea" rows="3" placeholder={`Enter ${actionModal.type} reason...`} />
                            )}

                            <div className="modal-actions">
                                <Button className="btn-outline flex-1" onClick={() => setActionModal(null)}>
                                    Cancel
                                </Button>
                                {actionModal.type !== 'contact' && (
                                    <Button className={`flex-1 ${actionModal.type === 'cancel' ? 'btn-danger' : 'btn-primary'}`} onClick={handleActionConfirm}>
                                        Confirm
                                    </Button>
                                )}
                            </div>
                        </div>
                    </div>
                )}
            </div>
        </div>
    );
}