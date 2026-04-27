import React from 'react';
import { Trash2, Star } from 'lucide-react';
import { Button } from '../../../../components/common/Button';
import { Card, CardContent } from '../../../../components/common/Card';
import { Pagination } from "../../../../components/common/Pagination.jsx";

export default function ReviewsTable({
                                         reviews,
                                         isLoading,
                                         handleDelete,
                                         page,
                                         totalPages,
                                         setPage
                                     }) {
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
        <Card>
            <CardContent>
                <div style={{ overflowX: 'auto' }}>
                    <table className="table">
                        <thead>
                        <tr>
                            <th>Customer</th>
                            <th>Restaurant</th>
                            <th>Rating</th>
                            <th>Comment</th>
                            <th>Date</th>
                            <th>Actions</th>
                        </tr>
                        </thead>

                        <tbody>
                        {isLoading ? (
                            <tr>
                                <td colSpan="6" style={{ textAlign: 'center', padding: '20px' }}>
                                    Loading...
                                </td>
                            </tr>
                        ) : reviews.map(r => (
                            <tr key={r.id}>
                                <td><div style={{ fontWeight: 500 }}>{r.customerName}</div></td>
                                <td>{r.restaurantName}</td>
                                <td><div className="stars">{renderStars(r.rating)}</div></td>
                                <td className="truncate" style={{ maxWidth: '300px' }}>{r.comment}</td>
                                <td className="text-muted">
                                    {new Date(r.createdAt).toLocaleDateString('vi-VN')}
                                </td>
                                <td className="actions">
                                    <Button className="danger" onClick={() => handleDelete(r.id)}>
                                        <Trash2 size={16} />
                                    </Button>
                                </td>
                            </tr>
                        ))}
                        </tbody>
                    </table>
                </div>

                <div style={{ marginTop: '20px', display: 'flex', justifyContent: 'center' }}>
                    <Pagination currentPage={page} totalPages={totalPages} setCurrentPage={setPage} />
                </div>
            </CardContent>
        </Card>
    );
}