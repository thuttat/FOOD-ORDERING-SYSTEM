import React, { useEffect, useState } from 'react';
import { Search } from 'lucide-react';
import { Card, CardContent } from '../../../components/common/Card';
import { Input } from '../../../components/common/Input';
import { Select } from '../../../components/common/Select';
import { StatsCard } from "../../../components/common/StatsCard.jsx";
import { ReviewService } from "../../../apis/ReviewService.js";
import './ReviewsManagement.css';
import ReviewsTable from './components/ReviewsTable.jsx';
import RatingDistributionChart from './components/RatingDistributionChart.jsx';
import ReviewActivityChart from './components/ReviewActivityChart.jsx';

export function ReviewsManagement() {
    const [reviews, setReviews] = useState([]);
    const [searchQuery, setSearchQuery] = useState('');
    const [ratingFilter, setRatingFilter] = useState('all');
    const [stats, setStats] = useState({ totalReviews: 0, averageRating: "0.0", ratingDistribution: [] });
    const [isLoading, setIsLoading] = useState(true);
    const [page, setPage] = useState(0);
    const [totalPages, setTotalPages] = useState(0);

    const fetchStats = async () => {
        try {
            const res = await ReviewService.getStats();
            setStats(res.data);
        } catch (error) {
            console.error("Failed to fetch stats", error);
        }
    };

    const fetchReviews = async (page = 0) => {
        try {
            setIsLoading(true);
            const params = {
                page: page,
                size: 10,
                search: searchQuery || null
            };
            const response = await ReviewService.getAllReviews(params);
            setReviews(response.data.content || []);
            setTotalPages(response.data.totalPages || 0);
        } catch (error) {
            console.error("Failed to fetch reviews: ", error);
        } finally {
            setIsLoading(false);
        }
    };

    useEffect(() => {
        fetchStats();
    }, []);

    useEffect(() => {
        fetchReviews(page);
    }, [page, searchQuery]);

    const handleDelete = async (id) => {
        if (!window.confirm("Are you sure you want to permanently delete this review from the system?")) return;
        try {
            await ReviewService.deleteReview(id);
            alert("Review deleted successfully!");
            fetchReviews();
            fetchStats();
        } catch (error) {
            alert("Error deleting review: " + error);
        }
    };

    const filteredReviews = reviews.filter((review) => {
        if (ratingFilter !== 'all' && review.rating.toString() !== ratingFilter) return false;
        return true;
    });

    return (
        <div className="container">
            <div className="admin-reviews">
                <div className="header-section">
                    <div>
                        <h1>Review Management</h1>
                        <p className="header-subtitle">
                            Manage comments and reviews across the entire system.
                        </p>
                    </div>
                </div>

                <div className="stats-grid" style={{ gridTemplateColumns: 'repeat(2, 1fr)' }}>
                    <StatsCard title="Total Reviews" value={stats.totalReviews} subtext="Total number of reviews" subtextClass="success" />
                    <StatsCard title="Avg Rating" value={stats.averageRating} subtext="System-wide average score" subtextClass="muted" />
                </div>

                <div className="filter-bar" style={{ display: 'flex', gap: '16px', marginTop: '10px', maxWidth: '600px' }}>
                    <Input
                        placeholder="Search by customer name or restaurant name..."
                        icon={<Search size={16} />}
                        value={searchQuery}
                        onChange={(e) => setSearchQuery(e.target.value)}
                    />

                    <Select
                        options={[
                            { value: 'all', label: 'All rating' },
                            { value: '5', label: '5 Stars' },
                            { value: '4', label: '4 Stars' },
                            { value: '3', label: '3 Stars' },
                            { value: '2', label: '2 Stars' },
                            { value: '1', label: '1 Star' },
                        ]}
                        value={ratingFilter}
                        onChange={(e) => setRatingFilter(e.target.value)}
                    />
                </div>

                <ReviewsTable
                    reviews={filteredReviews}
                    isLoading={isLoading}
                    handleDelete={handleDelete}
                    page={page}
                    totalPages={totalPages}
                    setPage={setPage}
                />

                <div className="charts">
                    <RatingDistributionChart data={stats.ratingDistribution} />
                    <ReviewActivityChart data={stats.reviewActivity} />
                </div>
            </div>
        </div>
    );
}