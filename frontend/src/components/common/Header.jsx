import { Link, NavLink, useNavigate } from "react-router-dom";
import { Button } from "./Button.jsx";
import { ShoppingCart, Search, Bell, House, NotepadText, User } from "lucide-react";
import "./../styles/Header.css";
import { useAuth } from "../../services/AuthContext.jsx";

export function Header({
                           showSearch = false,
                           showNav = false,
                           showCart = false,
                           cartCount = 0,
                           notificationCount = 0
                       }) {
    const navigate = useNavigate();
    const { user, role, logout } = useAuth();

    return (
        <header className="header">
            <div className="header-inner container">
                {/* Điều hướng Logo theo vai trò */}
                <Link to={role === "ADMIN" ? "/admin" : role === "RESTAURANT" ? "/restaurant" : "/"} className="logo">
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
                        {/* Link đến lịch sử đơn hàng */}
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
                            {/* Icon chuông thông báo - Đã sửa lỗi nhấn vào không hiện gì bằng cách thêm Route ở App.jsx */}
                            <div className="notif-icon" onClick={() => navigate("/notifications")} style={{ position: 'relative', cursor: 'pointer' }}>
                                <Bell size={22} />
                                {notificationCount > 0 && <span className="notif-badge">{notificationCount}</span>}
                            </div>

                            <div className="user-profile-box" onClick={() => navigate("/profile")} style={{ display: 'flex', alignItems: 'center', gap: '8px', cursor: 'pointer' }}>
                                <div className="avatar-circle"><User size={16}/></div>
                                <div className="user-name-text">{user.fullname}</div>
                            </div>

                            <Button className="btn-danger btn-sm" onClick={() => { logout(); navigate("/login"); }}>
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