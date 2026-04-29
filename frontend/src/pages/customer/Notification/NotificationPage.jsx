import React, { useEffect, useState } from 'react';
import { NotificationService } from '../../../apis/NotificationService';
import './Notification.css';

export default function NotificationPage() {
    const [notifications, setNotifications] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        fetchNotifications();
    }, []);

    const fetchNotifications = async () => {
        try {
            const res = await NotificationService.getMyNotifications();
            setNotifications(res.data);
        } catch (error) {
            console.error("Không thể lấy thông báo:", error);
        } finally {
            setLoading(false);
        }
    };

    if (loading) return <div className="text-center py-5">Loading...</div>;

    return (
        <div className="container py-4">
            <h2 className="mb-4">Your notification</h2>
            {notifications.length === 0 ? (
                <div className="alert alert-info">You have nothing.</div>
            ) : (
                <div className="list-group">
                    {notifications.map((noti) => (
                        <div key={noti.id} className={`list-group-item list-group-item-action ${!noti.isRead ? 'bg-light' : ''}`}>
                            <div className="d-flex w-100 justify-content-between">
                                <h5 className="mb-1">{noti.type === 'ORDER' ? '🛒 Đơn hàng' : 'Hệ thống'}</h5>
                                <small className="text-muted">{new Date(noti.createdAt).toLocaleString()}</small>
                            </div>
                            <p className="mb-1">{noti.message}</p>
                            {!noti.isRead && <span className="badge bg-primary">Mới</span>}
                        </div>
                    ))}
                </div>
            )}
        </div>
    );
}