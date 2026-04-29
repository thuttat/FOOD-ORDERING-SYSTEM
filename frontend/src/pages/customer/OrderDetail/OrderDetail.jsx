import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { OrderService } from '../../../apis/OrderService';
import { ReviewService } from '../../../apis/ReviewService';
import './OrderDetail.css';

export default function OrderDetail() {
    const { id } = useParams();
    const navigate = useNavigate();
    const [order, setOrder] = useState(null);
    const [loading, setLoading] = useState(true);
    const [selectedItem, setSelectedItem] = useState(null);
    const [rating, setRating] = useState(5);
    const [comment, setComment] = useState("");

    useEffect(() => {
        fetchOrderDetail();
    }, [id]);

    const fetchOrderDetail = async () => {
        try {
            setLoading(true);
            const res = await OrderService.getOrderDetail(id);
            setOrder(res.data);
        } catch (error) {
            console.error("Fetch order detail error:", error);
        } finally {
            setLoading(false);
        }
    };

    const handleConfirmReceived = async () => {
        if (window.confirm("Pls confirm?")) {
            try {
                await OrderService.updateOrderStatus(id, 'COMPLETED');
                alert("Successfully");
                fetchOrderDetail();
            } catch (error) {
                console.error("Error:", error);
                alert("Cannot update.");
            }
        }
    };

    const handleReviewSubmit = async (e) => {
        e.preventDefault();
        try {
            await ReviewService.submitReview({
                orderId: Number(id),
                restaurantId: order?.restaurantId || order?.restaurant?.id,
                menuItemId: selectedItem.menuItemId || selectedItem.id,
                rating: rating,
                comment: comment
            });
            alert("Thank you for your review!");
            setSelectedItem(null);
            setComment("");
        } catch (error) {
            console.error("Submit review error:", error);
            alert("Failed to submit review.");
        }
    };

    if (loading) return <div className="text-center py-5">Loading...</div>;
    if (!order) return <div className="text-center py-5">Order not found.</div>;

    return (
        <div className="container py-4">
            <button className="btn btn-link mb-3" onClick={() => navigate(-1)} style={{ color: '#000', textDecoration: 'none' }}>
                ← Back
            </button>

            <div className="card shadow-sm mb-4 border-0">
                <div className="card-body d-flex justify-content-between align-items-center">
                    <div>
                        <h3 className="fw-bold">Order #{order?.id}</h3>
                        <p className="text-muted mb-2">Placed at: {new Date(order?.createdAt).toLocaleString()}</p>
                        <span className={`badge rounded-pill px-3 py-2 bg-${order?.status === 'COMPLETED' ? 'success' : 'light text-dark border'}`}>
                            {order?.status}
                        </span>
                    </div>

                    {order?.status !== 'COMPLETED' && order?.status !== 'CANCELLED' && (
                        <button className="btn btn-success fw-bold px-4 rounded-pill" onClick={handleConfirmReceived}>
                            ✅ Confirm Received
                        </button>
                    )}
                </div>
            </div>

            <h4 className="mb-3 fw-bold">Order Items</h4>
            <div className="list-group mb-4 border-0 shadow-sm">
                {order.items?.map((item) => {
                    const itemName = item.name || item.menuItemName || item.menuItem?.name || "Delicious Food";
                    const itemImg = item.imageUrl || item.menuItem?.imageUrl || 'https://placehold.co/100x100';

                    return (
                        <div key={item.id} className="list-group-item d-flex justify-content-between align-items-center py-3 border-0 border-bottom">
                            <div className="d-flex align-items-center gap-3">
                                <img
                                    src={itemImg}
                                    alt={itemName}
                                    style={{ width: '80px', height: '80px', borderRadius: '12px', objectFit: 'cover' }}
                                />
                                <div>
                                    <h6 className="mb-1 fw-bold">{itemName}</h6>
                                    <small className="text-muted">Quantity: {item.quantity} x {item.unitPrice?.toLocaleString()}đ</small>
                                </div>
                            </div>

                            {order.status === 'COMPLETED' && (
                                <button className="btn btn-dark btn-sm rounded-pill px-3" onClick={() => setSelectedItem({ ...item, name: itemName })}>
                                    Review
                                </button>
                            )}
                        </div>
                    );
                })}
            </div>

            {selectedItem && (
                <div className="review-overlay">
                    <div className="review-modal p-4 shadow-lg bg-white rounded-4">
                        <h5 className="fw-bold mb-3">Review: {selectedItem.name}</h5>
                        <form onSubmit={handleReviewSubmit}>
                            <div className="mb-3">
                                <label className="form-label text-muted">Rating</label>
                                <select className="form-select border-0 bg-light" value={rating} onChange={(e) => setRating(Number(e.target.value))}>
                                    {[5, 4, 3, 2, 1].map(s => <option key={s} value={s}>{s} Stars</option>)}
                                </select>
                            </div>
                            <div className="mb-4">
                                <label className="form-label text-muted">Your Comment</label>
                                <textarea
                                    className="form-control border-0 bg-light"
                                    rows="4"
                                    placeholder="Tell us about your experience..."
                                    value={comment}
                                    onChange={(e) => setComment(e.target.value)}
                                    required
                                ></textarea>
                            </div>
                            <div className="d-grid gap-2">
                                <button type="submit" className="btn btn-danger py-2 rounded-pill">Submit Review</button>
                                <button type="button" className="btn btn-light py-2 rounded-pill" onClick={() => setSelectedItem(null)}>Cancel</button>
                            </div>
                        </form>
                    </div>
                </div>
            )}
        </div>
    );
}