import { Link, NavLink, useNavigate } from "react-router-dom";
import { Button } from "./Button.jsx";
import { ShoppingCart, Search, Bell, House, NotepadText, User } from "lucide-react";
import "./../styles/Header.css";
import { useAuth } from "../../services/AuthContext.jsx";
import { useEffect, useRef, useState } from "react";
import axiosClient from "../../apis/AxiosClient.js";

export function Header({
                           showSearch = false,
                           showNav = false,
                           showCart = false,
                           cartCount = 0,
                           // notificationCount = 0
                       }) {
    const navigate = useNavigate();
    const { user, role, logout } = useAuth();

    const [notifications, setNotifications] = useState([]);
    const [showNotifMenu, setShowNotifMenu] = useState(false);
    const notifMenuRef = useRef(null);

    const handleLogout = () => {
        logout();
        navigate("/login");
    };

    const getHomePath = () => {
        if (role === "ADMIN") return "/admin";
        if (role === "RESTAURANT") return "/restaurant";
        return "/";
    };

    const fetchNotifications = async () => {
        if (!user) return;
        try {
            const res = await axiosClient.get('/notifications');
            setNotifications(res.data || []);
        } catch (error) {
            console.error("Error fetching notifications:", error);
        }
    };

    useEffect(() => {
        fetchNotifications();
        const intervalId = setInterval(() => {
            fetchNotifications();
        }, 15000);

        return () => clearInterval(intervalId);
    }, [user]);

    useEffect(() => {
        const handleClickOutside = (event) => {
            if (notifMenuRef.current && !notifMenuRef.current.contains(event.target)) {
                setShowNotifMenu(false);
            }
        };
        document.addEventListener("mousedown", handleClickOutside);
        return () => document.removeEventListener("mousedown", handleClickOutside);
    }, []);

    const unreadCount = notifications.filter(n => !n.isRead).length;

    return (
        <header className="header">
            <div className="header-inner container">
                <Link to={getHomePath()} className="logo">
                    <div className="logo-icon">😋</div>
                    <span className="logo-text">HappyFood</span>
                </Link>

                {showSearch && (
                    <div className="search-bar">
                        <Search size={18} />
                        <input placeholder="Search for food..." />
                    </div>
                )}

                {showNav && (
                    <nav className="nav">
                        <NavLink to="/" className={({isActive}) => isActive ? "nav-btn active" : "nav-btn"}>
                            <House size={20}/> Home
                        </NavLink>
                        <NavLink to="/orders" className={({isActive}) => isActive ? "nav-btn active" : "nav-btn"}>
                            <NotepadText size={20}/> Orders
                        </NavLink>
                    </nav>
                )}

                <div className="user-section">
                    {showCart && (
                        <div className="cart-icon" onClick={() => navigate("/cart")} style={{ position: 'relative', cursor: 'pointer' }}>
                            <ShoppingCart size={22} />
                            {cartCount > 0 && <span className="cart-badge">{cartCount}</span>}
                        </div>
                    )}

                    {user ? (
                        <>
                            <div className="notification-wrapper" ref={notifMenuRef} style={{ position: 'relative' }}>
                                <div className="notification" onClick={() => setShowNotifMenu(!showNotifMenu)} style={{ cursor: 'pointer' }}>
                                    <Bell size={20} />
                                    {unreadCount > 0 && <span className="notification-badge">{unreadCount}</span>}
                                </div>

                                {showNotifMenu && (
                                    <div style={{
                                        position: 'absolute', top: '40px', right: '-10px', width: '300px',
                                        backgroundColor: '#fff', border: '1px solid #e5e7eb',
                                        borderRadius: '8px', boxShadow: '0 4px 6px -1px rgba(0, 0, 0, 0.1)',
                                        zIndex: 1000, overflow: 'hidden'
                                    }}>
                                        <div style={{ padding: '12px 16px', borderBottom: '1px solid #e5e7eb', fontWeight: 600 }}>
                                            Your notifications
                                        </div>
                                        <div style={{ maxHeight: '300px', overflowY: 'auto' }}>
                                            {notifications.length === 0 ? (
                                                <div style={{ padding: '20px', textAlign: 'center', color: '#6b7280', fontSize: '14px' }}>
                                                    No notification.
                                                </div>
                                            ) : (
                                                notifications.map(n => (
                                                    <div key={n.id} style={{
                                                        padding: '12px 16px', borderBottom: '1px solid #f3f4f6',
                                                        backgroundColor: n.isRead ? '#fff' : '#f0f9ff',
                                                        cursor: 'pointer'
                                                    }}>
                                                        <div style={{ fontSize: '14px', color: '#111827', marginBottom: '4px' }}>{n.message}</div>
                                                        <div style={{ fontSize: '12px', color: '#6b7280' }}>
                                                            {new Date(n.createdAt).toLocaleString('vi-VN')}
                                                        </div>
                                                    </div>
                                                ))
                                            )}
                                        </div>
                                    </div>
                                )}
                            </div>

                            <div className="user-profile-box" onClick={() => navigate("/profile")} style={{ display: 'flex', alignItems: 'center', gap: '8px', cursor: 'pointer' }}>
                                <div className="avatar-circle"><User size={16}/></div>
                                <div className="user-name-text">{user.fullname}</div>
                            </div>

                            <Button className="btn-danger btn-sm" onClick={handleLogout}>
                                Logout
                            </Button>
                        </>
                    ) : (
                        <Button className="btn-primary" onClick={() => navigate("/login")}>
                            Login
                        </Button>
                    )}
                </div>
            </div>
        </header>
    );
}