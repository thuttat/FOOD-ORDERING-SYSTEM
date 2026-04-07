import { BaseLayout } from "./BaseLayout.jsx";
import { Home, ShoppingBag, BarChart, Settings } from "lucide-react";

const sidebarItems = [
    { label: "Dashboard", path: "/restaurant/dashboard", icon: <Home size={18} /> },
    { label: "Orders", path: "/restaurant/orders", icon: <ShoppingBag size={18} /> },
    { label: "Analytics", path: "/restaurant/analytics", icon: <BarChart size={18} /> },
    { label: "Settings", path: "/restaurant/settings", icon: <Settings size={18} /> },
];

export function RestaurantLayout() {
    return (
        <BaseLayout role="RESTAURANT" sidebarItems={sidebarItems} />
    );
}