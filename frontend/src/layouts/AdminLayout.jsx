import { BaseLayout } from "./BaseLayout.jsx";
import { Home, Store, Users, ShoppingBag, Settings } from "lucide-react";

const sidebarItems = [
    { label: "Dashboard", path: "/admin/dashboard", icon: <Home size={18} /> },
    { label: "Restaurants", path: "/admin/restaurants", icon: <Store size={18} /> },
    { label: "Users", path: "/admin/users", icon: <Users size={18} /> },
    { label: "Orders", path: "/admin/orders", icon: <ShoppingBag size={18} /> },
    { label: "Settings", path: "/admin/settings", icon: <Settings size={18} /> },
];

export function AdminLayout() {
    return (
        <BaseLayout role="ADMIN" sidebarItems={sidebarItems} />
    );
}