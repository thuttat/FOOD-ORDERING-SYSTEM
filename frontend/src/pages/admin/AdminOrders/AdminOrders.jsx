import React, { useState } from 'react';
import { Search, Filter, ChevronDown, ChevronUp, XCircle } from 'lucide-react';
import { Badge } from '../../../components/common/Badge';
import { Button } from '../../../components/common/Button';
import { Card, CardContent } from '../../../components/common/Card';
import { Input } from '../../../components/common/Input';
import { Select } from '../../../components/common/Select';
import { StatsCard } from '../../../components/common/StatsCard';
import { BarChart, Bar, XAxis, YAxis, Tooltip, ResponsiveContainer } from 'recharts';
import './AdminOrders.css';

const mockOrders = [
    {
        id: 'ORD-1234',
        customer: { name: 'John Doe', email: 'john@example.com' },
        restaurant: 'Burger Palace',
        items: [
            { name: 'Burger', quantity: 2, price: 10 }
        ],
        total: 20,
        status: 'delivering',
        timeline: [
            { status: 'Placed', time: '14:30', completed: true },
            { status: 'Preparing', time: '14:35', completed: true },
            { status: 'Delivered', time: '-', completed: false },
        ]
    }
];

const flaggedOrders = [
    { id: 'ORD-1220' }
];

const orderStatsData = [
    { day: 'Mon', completed: 45, cancelled: 3 },
    { day: 'Tue', completed: 52, cancelled: 2 },
];

export function AdminOrders() {
    const [searchQuery, setSearchQuery] = useState('');
    const [statusFilter, setStatusFilter] = useState('all');
    const [expandedOrder, setExpandedOrder] = useState(null);
    const [actionModal, setActionModal] = useState(null);

    const filteredOrders = mockOrders.filter(order => {
        if (statusFilter !== 'all' && order.status !== statusFilter) return false;

        if (
            searchQuery &&
            !order.id.toLowerCase().includes(searchQuery.toLowerCase()) &&
            !order.customer.name.toLowerCase().includes(searchQuery.toLowerCase())
        ) return false;

        return true;
    });

    const toggleExpand = (id) => {
        setExpandedOrder(expandedOrder === id ? null : id);
    };

    const getStatusBadge = (status) => {
        const map = {
            pending: 'pending',
            preparing: 'warning',
            delivering: 'info',
            completed: 'active',
            cancelled: 'inactive',
        };
        return map[status] || 'pending';
    };

    return (
        <div className="container">
            <div className="admin-orders">
                <div>
                    <h1>Order Management</h1>
                    <p className="header-subtitle">Monitor and manage all platform orders</p>
                </div>

                <div className="stats-grid">
                    <StatsCard
                        title="Total Orders"
                        value="5,678"
                        subtext="+12% today"
                        subtextClass="success"
                    />
                    <StatsCard
                        title="Pending"
                        value={mockOrders.filter(o => o.status === 'pending').length}
                        subtext="Needs attention"
                        subtextClass="warning"
                    />
                    <StatsCard
                        title="In Progress"
                        value={mockOrders.filter(o => o.status === 'preparing' || o.status === 'delivering').length}
                        subtext="Active"
                        subtextClass="muted"
                    />
                    <StatsCard
                        title="Flagged"
                        value={flaggedOrders.length}
                        subtext="Requires review"
                        subtextClass="destructive"
                    />
                    <StatsCard
                        title="Avg. Time"
                        value="32m"
                        subtext="-5m vs avg"
                        subtextClass="success"
                    />
                </div>

                <div className="filter-bar">
                    <Input
                        placeholder="Search..."
                        icon={<Search />}
                        value={searchQuery}
                        onChange={(e) => setSearchQuery(e.target.value)}
                    />

                    <Select
                        options={[
                            { value: 'all', label: 'All Status' },
                            { value: 'pending', label: 'Pending' }
                        ]}
                        value={statusFilter}
                        onChange={(e) => setStatusFilter(e.target.value)}
                    />
                </div>

                <Card>
                    <CardContent className="table-wrapper">
                        <table className="orders-table">
                            <thead>
                            <tr>
                                <th></th>
                                <th>Order</th>
                                <th>Customer</th>
                                <th>Total</th>
                                <th>Status</th>
                                <th></th>
                            </tr>
                            </thead>

                            <tbody>
                            {filteredOrders.map(order => (
                                <React.Fragment key={order.id}>
                                    <tr className="orders-row">
                                        <td>
                                            <button
                                                className="expand-btn"
                                                onClick={() => toggleExpand(order.id)}
                                            >
                                                {expandedOrder === order.id ? <ChevronUp /> : <ChevronDown />}
                                            </button>
                                        </td>

                                        <td>{order.id}</td>
                                        <td>{order.customer.name}</td>
                                        <td>${order.total}</td>

                                        <td>
                                            <Badge status={getStatusBadge(order.status)}>
                                                {order.status}
                                            </Badge>
                                        </td>

                                        <td>
                                            <Button
                                                size="sm"
                                                variant="danger"
                                                onClick={() => setActionModal({ order })}
                                            >
                                                Cancel
                                            </Button>
                                        </td>
                                    </tr>

                                    {expandedOrder === order.id && (
                                        <tr>
                                            <td colSpan={6} className="expand-row">
                                                <div className="expand-grid">
                                                    <div>
                                                        {order.items.map((item, i) => (
                                                            <div key={i} className="order-item">
                                                                <span>{item.name} x{item.quantity}</span>
                                                                <span>${(item.price * item.quantity).toFixed(2)}</span>
                                                            </div>
                                                        ))}
                                                    </div>

                                                    <div>
                                                        {order.timeline.map((step, i) => (
                                                            <div key={i} className="timeline-step">
                                                                <div className={`dot ${step.completed ? 'active' : ''}`} />
                                                                <div>
                                                                    <div>{step.status}</div>
                                                                    <small>{step.time}</small>
                                                                </div>
                                                            </div>
                                                        ))}
                                                    </div>
                                                </div>
                                            </td>
                                        </tr>
                                    )}
                                </React.Fragment>
                            ))}
                            </tbody>
                        </table>
                    </CardContent>
                </Card>

                <Card>
                    <CardContent>
                        <div className="chart-wrapper">
                            <ResponsiveContainer width="100%" height={200}>
                                <BarChart data={orderStatsData}>
                                    <XAxis dataKey="day" />
                                    <YAxis />
                                    <Tooltip />
                                    <Bar dataKey="completed" />
                                    <Bar dataKey="cancelled" />
                                </BarChart>
                            </ResponsiveContainer>
                        </div>
                    </CardContent>
                </Card>

                {actionModal && (
                    <div className="modal-overlay">
                        <div className="modal-box">

                            <div className="modal-header">
                                <XCircle />
                                <h3>Cancel Order</h3>
                            </div>

                            <textarea
                                className="modal-textarea"
                                placeholder="Reason..."
                            />

                            <div className="modal-actions">
                                <Button onClick={() => setActionModal(null)}>Cancel</Button>
                                <Button variant="danger" onClick={() => setActionModal(null)}>
                                    Confirm
                                </Button>
                            </div>
                        </div>
                    </div>
                )}
            </div>
        </div>
    );
}