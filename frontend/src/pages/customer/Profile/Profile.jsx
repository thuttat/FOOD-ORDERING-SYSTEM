import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { OrderService } from '../../../apis/OrderService';
import UserService from '../../../apis/UserService';
import './Profile.css';

export function Profile() {
    const navigate = useNavigate();
    const [orders, setOrders] = useState([]);
    const [profile, setProfile] = useState(null);
    const [loading, setLoading] = useState(true);

    const authUser = JSON.parse(localStorage.getItem('user')) || {};

    useEffect(() => {
        const loadData = async () => {
            try {
                const ordersRes = await OrderService.getMyOrders();
                setOrders(ordersRes.data || []);

                if (authUser && authUser.id) {
                    const userRes = await UserService.getUserProfile(authUser.id);
                    setProfile(userRes.data);
                } else {
                    setProfile(authUser);
                }
            } catch (err) {
                console.error(err);
                setProfile(authUser);
            } finally {
                setLoading(false);
            }
        };

        loadData();
    }, []);

    if (loading) {
        return (
            <div className="profile-container container" style={{ textAlign: 'center', padding: '50px' }}>
                <p>Loading profile data...</p>
            </div>
        );
    }

    return (
        <div className="profile-container container">
            <div className="user-card" style={{ background: '#f8f9fa', padding: '25px', borderRadius: '12px', marginBottom: '30px', boxShadow: '0 2px 8px rgba(0,0,0,0.05)' }}>
                <h3 style={{ marginBottom: '20px' }}>👤 Personal Information</h3>
                <div className="user-details">
                    <p style={{ marginBottom: '10px' }}>
                        <strong>Full Name:</strong> {profile?.fullname || authUser?.fullname || 'Not Provided'}
                    </p>
                    <p style={{ marginBottom: '10px' }}>
                        <strong>Email:</strong> {profile?.email || authUser?.email || 'Not Provided'}
                    </p>
                    <p style={{ marginBottom: '10px' }}>
                        <strong>Phone:</strong> {profile?.phone || authUser?.phone || 'Not Updated'}
                    </p>
                </div>
            </div>

            <div className="order-history">
                <h3 style={{ marginBottom: '20px' }}>📦 Order History</h3>
                <div className="order-list">
                    {orders && orders.length > 0 ? (
                        orders.map(order => (
                            <div key={order.id} className="order-item-row" style={{ borderBottom: '1px solid #eee', padding: '15px 0', display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                                <div>
                                    <span style={{ fontWeight: 'bold' }}>#{order.id}</span>
                                    <p style={{ fontSize: '13px', color: '#666' }}>
                                        {order.createdAt ? new Date(order.createdAt).toLocaleDateString('en-GB') : 'N/A'}
                                    </p>
                                </div>
                                <div style={{ fontWeight: 'bold', color: '#ff4757' }}>
                                    {order.totalAmount?.toLocaleString()}đ
                                </div>
                                <div>
                                    <span className={`badge-status status-${order.status?.toLowerCase()}`} style={{ textTransform: 'capitalize' }}>
                                        {order.status}
                                    </span>
                                </div>
                                <button
                                    onClick={() => navigate(`/order-detail/${order.id}`)}
                                    className="btn-detail"
                                    style={{ padding: '6px 15px', border: '1px solid #ddd', borderRadius: '4px', background: '#fff', cursor: 'pointer' }}
                                >
                                    Detail
                                </button>
                            </div>
                        ))
                    ) : (
                        <div style={{ textAlign: 'center', padding: '40px', color: '#95a5a6' }}>
                            <p>No orders found.</p>
                        </div>
                    )}
                </div>
            </div>
        </div>
    );
}