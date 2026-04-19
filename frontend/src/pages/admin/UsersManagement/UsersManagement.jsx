import React, { useState } from 'react';
import { Search, Edit2, MoreVertical, X, AlertCircle, TrendingUp } from 'lucide-react';
import { Badge } from '../../../components/common/Badge';
import { Button } from '../../../components/common/Button';
import { Card, CardContent } from '../../../components/common/Card';
import { Input } from '../../../components/common/Input';
import { Select } from '../../../components/common/Select';
import { LineChart, Line, XAxis, YAxis, Tooltip, ResponsiveContainer } from 'recharts';
import './UsersManagement.css';
import {StatsCard} from "../../../components/common/StatsCard.jsx";

const mockUsers = [
    {
        id: '1',
        name: 'John Doe',
        email: 'john@example.com',
        role: 'customer',
        status: 'active',
        orders: 23,
        totalSpent: 456,
        joined: '2025-12-15',
        lastActive: '2026-04-11',
    },
    {
        id: '2',
        name: 'Jane Smith',
        email: 'jane@example.com',
        role: 'customer',
        status: 'active',
        orders: 45,
        totalSpent: 892,
        joined: '2025-11-20',
        lastActive: '2026-04-10',
    },
    {
        id: '3',
        name: 'Bob Johnson',
        email: 'bob@burgerpalace.com',
        role: 'restaurant',
        status: 'active',
        orders: 0,
        totalSpent: 0,
        joined: '2025-10-10',
        lastActive: '2026-04-11',
    },
    {
        id: '4',
        name: 'Alice Williams',
        email: 'alice@example.com',
        role: 'customer',
        status: 'unactive',
        orders: 12,
        totalSpent: 234,
        joined: '2025-09-05',
        lastActive: '2026-03-15',
    },
    {
        id: '5',
        name: 'Charlie Brown',
        email: 'charlie@sushihaven.com',
        role: 'restaurant',
        status: 'active',
        orders: 0,
        totalSpent: 0,
        joined: '2025-08-12',
        lastActive: '2026-04-09',
    },
];

const userGrowthData = [
    { month: 'Jan', users: 120 },
    { month: 'Feb', users: 156 },
    { month: 'Mar', users: 189 },
    { month: 'Apr', users: 234 },
];

export function UsersManagement() {
    const [searchQuery, setSearchQuery] = useState('');
    const [roleFilter, setRoleFilter] = useState('all');
    const [statusFilter, setStatusFilter] = useState('all');
    const [editingUser, setEditingUser] = useState(null);
    const [confirmAction, setConfirmAction] = useState(null);

    const filteredUsers = mockUsers.filter((user) => {
        if (roleFilter !== 'all' && user.role !== roleFilter) return false;
        if (statusFilter !== 'all' && user.status !== statusFilter) return false;

        if (
            searchQuery &&
            !user.name.toLowerCase().includes(searchQuery.toLowerCase()) &&
            !user.email.toLowerCase().includes(searchQuery.toLowerCase())
        ) return false;

        return true;
    });

    const handleSaveUser = () => {
        console.log('Saving user:', editingUser);
        setEditingUser(null);
    };

    return (
        <div className="container" >
            <div className="admin-users">
                <div>
                    <h1>User Management</h1>
                    <p className="header-subtitle">Manage customer and restaurant accounts</p>
                </div>

                <div className="stats-grid">
                    <StatsCard
                        title="Total Users"
                        value={mockUsers.length}
                        subtext="+23 this week"
                        subtextClass="success"
                    />

                    <StatsCard
                        title="Customers"
                        value={mockUsers.filter(u => u.role === 'customer').length}
                        subtext="Active users"
                        subtextClass="muted"
                    />

                    <StatsCard
                        title="Restaurants"
                        value={mockUsers.filter(u => u.role === 'restaurant').length}
                        subtext="Partners"
                        subtextClass="muted"
                    />

                    <StatsCard
                        title="Active Today"
                        value="87%"
                        subtext="+5% vs yesterday"
                        subtextClass="success"
                    />
                </div>

                <div className="filters">
                    <Input
                        placeholder="Search users..."
                        icon={<Search />}
                        value={searchQuery}
                        onChange={(e) => setSearchQuery(e.target.value)}
                    />

                    <Select
                        options={[
                            { value: 'all', label: 'All Roles' },
                            { value: 'customer', label: 'Customers' },
                            { value: 'restaurant', label: 'Restaurants' },
                        ]}
                        value={roleFilter}
                        onChange={(e) => setRoleFilter(e.target.value)}
                    />

                    <Select
                        options={[
                            { value: 'all', label: 'All Status' },
                            { value: 'active', label: 'Active' },
                            { value: 'unactive', label: 'Unactive' },
                        ]}
                        value={statusFilter}
                        onChange={(e) => setStatusFilter(e.target.value)}
                    />
                </div>

                <Card>
                    <CardContent className="table-wrapper">
                        <table className="user-table">
                            <thead>
                            <tr>
                                <th>User</th>
                                <th>Role</th>
                                <th>Orders</th>
                                <th>Total</th>
                                <th>Joined</th>
                                <th>Status</th>
                                <th></th>
                            </tr>
                            </thead>

                            <tbody>
                            {filteredUsers.map((user) => (
                                <tr key={user.id}>
                                    <td>
                                        <div>
                                            <div className="name">{user.name}</div>
                                            <div className="email">{user.email}</div>
                                        </div>
                                    </td>

                                    <td>
                                        <span className="role-badge">{user.role}</span>
                                    </td>

                                    <td>{user.orders}</td>

                                    <td>{user.totalSpent || '-'}</td>

                                    <td>{user.joined}</td>

                                    <td>
                                        <Badge status={user.status}>
                                            {user.status}
                                        </Badge>
                                    </td>

                                    <td>
                                        <div className="actions">
                                            <button onClick={() => setEditingUser(user)}>
                                                <Edit2 size={16} />
                                            </button>
                                            <button>
                                                <MoreVertical size={16} />
                                            </button>
                                        </div>
                                    </td>
                                </tr>
                            ))}
                            </tbody>
                        </table>
                    </CardContent>
                </Card>

                <Card>
                    <CardContent className="chart">
                        <div className="chart-header">
                            <h3>User Growth</h3>
                            <span className="success"><TrendingUp size={16}/> +18%</span>
                        </div>

                        <ResponsiveContainer width="100%" height={200}>
                            <LineChart data={userGrowthData}>
                                <XAxis dataKey="month" />
                                <YAxis />
                                <Tooltip />
                                <Line dataKey="users" />
                            </LineChart>
                        </ResponsiveContainer>
                    </CardContent>
                </Card>

                {editingUser && (
                    <div className="modal">
                        <div className="modal-box">
                            <div className="modal-header">
                                <h2>Edit User</h2>
                                <button onClick={() => setEditingUser(null)}>
                                    <X />
                                </button>
                            </div>

                            <Input
                                value={editingUser.name}
                                onChange={(e) => setEditingUser({ ...editingUser, name: e.target.value })}
                            />

                            <Input
                                value={editingUser.email}
                                onChange={(e) => setEditingUser({ ...editingUser, email: e.target.value })}
                            />

                            <div className="modal-actions">
                                <Button onClick={() => setEditingUser(null)}>Cancel</Button>
                                <Button onClick={handleSaveUser}>Save</Button>
                            </div>
                        </div>
                    </div>
                )}

                {confirmAction && (
                    <div className="modal">
                        <div className="modal-box">
                            <h3>Confirm</h3>
                            <p>Are you sure?</p>

                            <div className="modal-actions">
                                <Button onClick={() => setConfirmAction(null)}>Cancel</Button>
                                <Button onClick={() => setConfirmAction(null)}>Confirm</Button>
                            </div>
                        </div>
                    </div>
                )}
            </div>
        </div>
    );
}