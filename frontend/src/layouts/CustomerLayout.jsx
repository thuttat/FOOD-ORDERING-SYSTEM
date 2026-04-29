import React, { useState, useEffect } from "react";
import { Header } from "../components/common/Header.jsx";
import { Outlet } from "react-router-dom";
import { Footer } from "../components/common/Footer.jsx";
import { CartService } from "../apis/CartService";
import api from "../apis/AxiosClient";

export function CustomerLayout() {
    const [cartCount, setCartCount] = useState(0);
    const [notifyCount, setNotifyCount] = useState(0);

    const refreshData = async () => {
        try {
            const cartRes = await CartService.getCart();
            const count = cartRes.data.items?.reduce((sum, item) => sum + item.quantity, 0) || 0;
            setCartCount(count);

            const notifyRes = await api.get("/notifications/me").catch(() => ({ data: [] }));
            const unread = notifyRes.data.filter(n => !n.isRead).length;
            setNotifyCount(unread);
        } catch (error) {
            console.error("Sync error:", error);
        }
    };

    useEffect(() => {
        const timer = setTimeout(() => {
            refreshData();
        }, 0);

        window.addEventListener('cartUpdated', refreshData);
        window.addEventListener('notifyUpdated', refreshData);
        return () => {
            clearTimeout(timer);
            window.removeEventListener('cartUpdated', refreshData);
            window.removeEventListener('notifyUpdated', refreshData);
        };
    }, []);

    return (
        <div>
            <Header role="USER" showCart cartCount={cartCount} notificationCount={notifyCount} />
            <main className="container" style={{ minHeight: '80vh', padding: '20px 0' }}><Outlet /></main>
            <Footer />
        </div>
    );
}