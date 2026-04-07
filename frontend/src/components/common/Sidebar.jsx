import { NavLink } from "react-router-dom";
import "./../styles/Sidebar.css";

export function Sidebar({ items = [] }) {
    return (
        <aside className="sidebar">
            {items.map((item, index) => (
                <NavLink
                    key={index}
                    to={item.path}
                    className="sidebar-item"
                >
                    <span className="icon">{item.icon}</span>
                    {item.label}
                </NavLink>
            ))}
        </aside>
    );
}