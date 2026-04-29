import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { OrderService } from '../../../apis/OrderService';
import './Profile.css';

export function Profile() {
    const navigate = useNavigate();
    const [orders, setOrders] = useState([]);
    const user = JSON.parse(localStorage.getItem('user')) || {};

    useEffect(() => {
        const fetchOrders = async () => {
            try {
                const res = await OrderService.getMyOrders();
                setOrders(res.data);
            } catch (err) {
                console.error("Cannot load orders history:", err);
            }
        };
        fetchOrders();
    }, []);

    return (
        <div className="profile-container container">
            <div className="user-card" style={{ background: '#f8f9fa', padding: '20px', borderRadius: '10px', marginBottom: '30px' }}>
                <h3>Information</h3>
                <div className="user-details">
                    <p><strong>Name:</strong> {user.fullname || 'N/A'}</p>
                    <p><strong>Email:</strong> {user.email || 'N/A'}</p>
                    <p><strong>Phone:</strong> {user.phone || 'Chưa cập nhật'}</p>
                </div>
                <button className="btn-edit-profile">Edit Profile</button>
            </div>

            <div className="order-history">
                <h3>Order History</h3>
                <div className="order-list">
                    {orders.length > 0 ? orders.map(order => (
                        <div key={order.id} className="order-item-row" style={{ borderBottom: '1px solid #eee', padding: '15px 0', display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                            <div>
                                <span style={{ fontWeight: 'bold' }}>#{order.id}</span>
                                <p style={{ fontSize: '13px', color: '#666' }}>{new Date(order.createdAt).toLocaleDateString()}</p>
                            </div>
                            <div style={{ fontWeight: 'bold', color: '#ff4757' }}>
                                {order.totalAmount.toLocaleString()}đ
                            </div>
                            <div>
                                <span className={`badge-status status-${order.status?.toLowerCase()}`}>
                                    {order.status}
                                </span>
                            </div>
                            <button
                                onClick={() => navigate(`/order-detail/${order.id}`)}
                                className="btn-detail"
                            >
                                Detail
                            </button>
                        </div>
                    )) : (
                        <div className="empty-history" style={{ textAlign: 'center', padding: '20px' }}>
                            <p>You haven't placed any orders yet.</p>
                        </div>
                    )}
                </div>
            </div>
        </div>
    );
}