import React, { useState } from 'react';
import {
    Search, Eye, Trash2, EyeOff, CheckCircle,
    AlertTriangle, Star
} from 'lucide-react';

import { Badge } from '../../../components/common/Badge';
import { Button } from '../../../components/common/Button';
import { Card, CardContent } from '../../../components/common/Card';
import { Input } from '../../../components/common/Input';
import { Select } from '../../../components/common/Select';

import {
    PieChart, Pie, Cell, Tooltip,
    ResponsiveContainer, BarChart, Bar, XAxis, YAxis
} from 'recharts';

import './ReviewsManagement.css';
import {StatsCard} from "../../../components/common/StatsCard.jsx";

const mockReviews = [
    {
        id: 'REV-001',
        user: 'John Doe',
        userEmail: 'john@example.com',
        restaurant: 'Burger Palace',
        rating: 5,
        comment: 'Amazing burgers! Fresh ingredients and fast delivery. Highly recommend!',
        date: '2026-04-11 14:30',
        status: 'published',
        helpful: 12,
        orderCount: 23,
    },
    {
        id: 'REV-002',
        user: 'Jane Smith',
        userEmail: 'jane@example.com',
        restaurant: 'Sushi Haven',
        rating: 4,
        comment: 'Great sushi, but delivery took a bit longer than expected.',
        date: '2026-04-11 13:15',
        status: 'published',
        helpful: 8,
        orderCount: 45,
    },
    {
        id: 'REV-003',
        user: 'Bob Johnson',
        userEmail: 'bob@example.com',
        restaurant: 'Pizza Corner',
        rating: 1,
        comment: 'Terrible experience. Food was cold and took 2 hours. Never ordering again!',
        date: '2026-04-11 12:00',
        status: 'published',
        helpful: 5,
        orderCount: 3,
    },
];

const reportedReviews = [
    {
        id: 'REV-101',
        user: 'Fake Account',
        userEmail: 'fake@spam.com',
        restaurant: 'Thai Delight',
        rating: 1,
        comment: 'This place is horrible! Visit our competitor instead at www.scam-site.com for better food!',
        date: '2026-04-10 18:30',
        reportReason: 'Contains spam/promotional links',
        reportCount: 5,
        flaggedKeywords: ['competitor', 'scam-site.com'],
        userOrderCount: 0,
    },
    {
        id: 'REV-102',
        user: 'Angry User',
        userEmail: 'angry@example.com',
        restaurant: 'Burger Palace',
        rating: 1,
        comment: 'The owner is a terrible person! This is the worst restaurant ever. Everyone who works here should be fired!',
        date: '2026-04-10 16:45',
        reportReason: 'Abusive language / Personal attacks',
        reportCount: 3,
        flaggedKeywords: ['terrible person', 'fired'],
        userOrderCount: 1,
    },
    {
        id: 'REV-103',
        user: 'Suspicious User',
        userEmail: 'suspicious@temp.com',
        restaurant: 'Sushi Haven',
        rating: 5,
        comment: 'Best sushi ever! Amazing! Perfect! Incredible! Outstanding! Five stars!',
        date: '2026-04-10 14:20',
        reportReason: 'Suspected fake positive review',
        reportCount: 2,
        flaggedKeywords: ['repetitive praise'],
        userOrderCount: 0,
    },
];

const ratingDistribution = [
    { rating: '5 Stars', count: 234, color: '#10b981' },
    { rating: '4 Stars', count: 156, color: '#3b82f6' },
    { rating: '3 Stars', count: 89, color: '#f59e0b' },
    { rating: '2 Stars', count: 45, color: '#ff6b35' },
    { rating: '1 Star', count: 32, color: '#ef4444' },
];

const moderationActivity = [
    { day: 'Mon', deleted: 3, hidden: 5 },
    { day: 'Tue', deleted: 2, hidden: 4 },
    { day: 'Wed', deleted: 5, hidden: 3 },
    { day: 'Thu', deleted: 1, hidden: 2 },
    { day: 'Fri', deleted: 4, hidden: 6 },
    { day: 'Sat', deleted: 2, hidden: 3 },
    { day: 'Sun', deleted: 1, hidden: 2 },
];

export function ReviewsManagement() {
    const [activeTab, setActiveTab] = useState('all');
    const [searchQuery, setSearchQuery] = useState('');
    const [ratingFilter, setRatingFilter] = useState('all');
    const [selectedReview, setSelectedReview] = useState(null);
    const [actionModal, setActionModal] = useState(null);

    const filteredReviews = mockReviews.filter((review) => {
        if (ratingFilter !== 'all' && review.rating.toString() !== ratingFilter) return false;
        if (
            searchQuery &&
            !review.user.toLowerCase().includes(searchQuery.toLowerCase()) &&
            !review.restaurant.toLowerCase().includes(searchQuery.toLowerCase())
        ) return false;
        return true;
    });

    const renderStars = (rating) => {
        return Array.from({ length: 5 }).map((_, i) => (
            <Star
                key={i}
                className={`star ${i < rating ? 'filled' : ''}`}
                fill={i < rating ? 'currentColor' : 'none'}
            />
        ));
    };

    return (
        <div className="container">
            <div className="admin-reviews">
                <div className="header-section">
                    <div>
                        <h1>Review Moderation</h1>
                        <p className="header-subtitle">Monitor and moderate user reviews</p>
                    </div>
                </div>

                <div className="stats-grid">
                    <StatsCard
                        title="Total Reviews"
                        value="556" subtext="+18 today"
                        subtextClass="success"
                    />
                    <StatsCard
                        title="Avg Rating"
                        value="4.3"
                        subtext="Platform-wide"
                        subtextClass="muted"
                    />
                    <StatsCard
                        title="Reported"
                        value={reportedReviews.length}
                        subtext="Needs review"
                        subtextClass="warning"
                    />
                    <StatsCard
                        title="Hidden"
                        value="12"
                        subtext="This week"
                        subtextClass="muted"
                    />
                    <StatsCard
                        title="Deleted"
                        value="8"
                        subtext="This week"
                        subtextClass="muted"
                    />
                </div>

                <div className="tabs">
                    {['all', 'reported'].map(tab => (
                        <button
                            key={tab}
                            onClick={() => setActiveTab(tab)}
                            className={activeTab === tab ? 'active' : ''}
                        >
                            {tab}
                        </button>
                    ))}
                </div>

                <div className="filter-bar">
                    <Input
                        placeholder="Search reviews..."
                        icon={<Search size={16} />}
                        value={searchQuery}
                        onChange={(e) => setSearchQuery(e.target.value)}
                    />

                    {activeTab === 'all' && (
                        <Select
                            options={[
                                { value: 'all', label: 'All Ratings' },
                                { value: '5', label: '5 Stars' },
                                { value: '4', label: '4 Stars' },
                                { value: '3', label: '3 Stars' },
                                { value: '2', label: '2 Stars' },
                                { value: '1', label: '1 Star' },
                            ]}
                            value={ratingFilter}
                            onChange={(e) => setRatingFilter(e.target.value)}
                        />
                    )}
                </div>

                {activeTab === 'all' && (
                    <Card>
                        <CardContent>
                            <table className="table">
                                <thead>
                                <tr>
                                    <th>User</th>
                                    <th>Restaurant</th>
                                    <th>Rating</th>
                                    <th>Comment</th>
                                    <th>Date</th>
                                    <th>Actions</th>
                                </tr>
                                </thead>

                                <tbody>
                                {filteredReviews.map(r => (
                                    <tr key={r.id}>
                                        <td>
                                            <div>{r.user}</div>
                                            <div className="text-muted">{r.userEmail}</div>
                                        </td>

                                        <td>{r.restaurant}</td>

                                        <td>
                                            <div className="stars">
                                                {renderStars(r.rating)}
                                            </div>
                                        </td>

                                        <td className="truncate">{r.comment}</td>

                                        <td className="text-muted">{r.date}</td>

                                        <td className="actions">
                                            <Button onClick={() => setSelectedReview(r)}><Eye size={16} /></Button>
                                            <Button onClick={() => setActionModal({ type: 'hide', review: r })}><EyeOff size={16} /></Button>
                                            <Button className="danger" onClick={() => setActionModal({ type: 'delete', review: r })}>
                                                <Trash2 size={16} />
                                            </Button>
                                        </td>
                                    </tr>
                                ))}
                                </tbody>
                            </table>
                        </CardContent>
                    </Card>
                )}

                {activeTab === 'reported' && (
                    <div className="reported-list">
                        {reportedReviews.map(r => (
                            <Card key={r.id}>
                                <CardContent>
                                    <div className="reported-header">
                                        <AlertTriangle size={18} />
                                        <strong>{r.id}</strong>
                                        <Badge status="warning">{r.reportCount} Reports</Badge>
                                    </div>

                                    <div className="reported-meta">
                                        <span>{r.user}</span>
                                        <span>{r.restaurant}</span>
                                        <span>{r.date}</span>
                                    </div>

                                    <div className="stars">{renderStars(r.rating)}</div>

                                    <p>{r.comment}</p>

                                    <div className="report-box">
                                        <p><strong>Reason:</strong> {r.reportReason}</p>
                                    </div>

                                    <div className="actions">
                                        <Button className="btn-primary">View</Button>
                                        <Button className="btn-outline">Hide</Button>
                                        <Button className="btn-danger">Delete</Button>
                                        <Button className="btn-approve">Safe</Button>
                                    </div>
                                </CardContent>
                            </Card>
                        ))}
                    </div>
                )}

                <div className="charts">
                    <Card>
                        <CardContent>
                            <h3>Rating Distribution</h3>
                            <ResponsiveContainer width="100%" height={250}>
                                <PieChart>
                                    <Pie data={ratingDistribution} dataKey="count">
                                        {ratingDistribution.map((entry, i) => (
                                            <Cell key={i} fill={entry.color} />
                                        ))}
                                    </Pie>
                                    <Tooltip />
                                </PieChart>
                            </ResponsiveContainer>
                        </CardContent>
                    </Card>

                    <Card>
                        <CardContent>
                            <h3>Moderation Activity</h3>
                            <ResponsiveContainer width="100%" height={250}>
                                <BarChart data={moderationActivity}>
                                    <XAxis dataKey="day" />
                                    <YAxis />
                                    <Tooltip />
                                    <Bar dataKey="deleted" />
                                    <Bar dataKey="hidden" />
                                </BarChart>
                            </ResponsiveContainer>
                        </CardContent>
                    </Card>
                </div>
            </div>
        </div>
    );
}