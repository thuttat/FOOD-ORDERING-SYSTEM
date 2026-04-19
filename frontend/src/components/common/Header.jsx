import {Link, NavLink, useNavigate} from "react-router-dom";
import { Button } from "./Button.jsx";
import {ShoppingCart, Search, Bell, House, ChefHat, NotepadText} from "lucide-react";
import "./../styles/Header.css";
import {useAuth} from "../../services/AuthContext.jsx";

export function Header({
                           showSearch = false,
                           showNav = false,
                           showCart = false,
                       }) {
    const navigate = useNavigate();
    const {user, role, logout} = useAuth();

    const handleLogout = () => {
        logout();
        navigate("/login");
    };

    const getHomePath = () => {
        if (role === "ADMIN") return "/admin";
        if (role === "RESTAURANT") return "/restaurant";
        return "/";
    };

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
                        <input placeholder="Search for food, restaurants..." />
                    </div>
                )}

                {showNav && (
                    <nav className="nav">
                        <NavLink to="/" className={({ isActive }) =>
                            isActive ? "nav-btn active" : "nav-btn"
                        }><House size={20} strokeWidth={1.75} /> Home</NavLink>
                        <NavLink to="/restaurants" className={({ isActive }) =>
                            isActive ? "nav-btn active" : "nav-btn"
                        }><ChefHat size={20} strokeWidth={1.75} /> Restaurants</NavLink>
                        <NavLink to="/orders" className={({ isActive }) =>
                            isActive ? "nav-btn active" : "nav-btn"
                        }><NotepadText size={20} strokeWidth={1.75} /> Orders</NavLink>
                    </nav>
                )}

                <div className="user-section">
                    {showCart && (
                        <div className="cart" onClick={() => navigate("/cart")}>
                            <ShoppingCart size={20} />
                            <span className="cart-badge">2</span>
                        </div>
                    )}

                    {user ? (
                        <>
                            <div className="notification">
                                <Bell size={20} />
                                <span className="notification-badge">3</span>
                            </div>

                            <div className="user-info">
                                <div className="user-name">{user ? user.fullname || user.username : ""}</div>
                                <div className="text-muted small">{user ? user.email : ""}</div>
                            </div>

                            <Button className="btn-danger" onClick={handleLogout}>
                                Log out
                            </Button>
                        </>
                    ) : (
                        <Button className="btn-primary" onClick={() => navigate("/login")}>
                            Log in
                        </Button>
                    )}
                </div>
            </div>
        </header>
    );
}