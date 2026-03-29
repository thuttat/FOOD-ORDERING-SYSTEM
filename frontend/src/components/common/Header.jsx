import { Link, useNavigate } from "react-router-dom";
import { Button } from "./Button.jsx";
import { ShoppingCart, Search } from "lucide-react";
import "./../styles/Header.css";

export function Header() {
    const navigate = useNavigate();

    const handleLogout = () => {
        localStorage.removeItem("user");
        navigate("/login");
    };

    return (
        <header className="header">
            <div className="header-inner container">
                <Link to="/" className="logo">
                    <div className="logo-icon">😋</div>
                    <span className="logo-text">HappyFood</span>
                </Link>

                <div className="search-bar">
                    <Search size={18} />
                    <input
                        type="text"
                        placeholder="Search for food, restaurants..."
                    />
                </div>

                <nav className="nav">
                    <Link to="/" className="nav-btn">Home</Link>
                    <Link to="/restaurants" className="nav-btn">Restaurants</Link>
                    <Link to="/orders" className="nav-btn">Orders</Link>
                </nav>

                <div className="user-section">
                    <div className="cart" onClick={() => navigate("/cart")}>
                        <ShoppingCart size={20} />
                        <span className="cart-badge">2</span>
                    </div>

                    <div className="user-info">
                        <div className="user-name">John Doe</div>
                        <div className="text-muted small">john@example.com</div>
                    </div>

                    <Button className="btn-danger" onClick={handleLogout}>
                        Log out
                    </Button>
                </div>
            </div>
        </header>
    );
}