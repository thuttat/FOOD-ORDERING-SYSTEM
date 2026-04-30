import React, { useEffect, useState } from 'react';
import { OrderService } from "../../../apis/OrderService.js";
import './AdminOrders.css';
import { OrderStats } from './components/OrderStats';
import { OrderFilterBar } from './components/OrderFilterBar';
import { OrderTable } from './components/OrderTable';
import { OrderChart } from './components/OrderChart';
import { OrderActionModal } from './components/OrderActionModal';

export function AdminOrders() {
    const [orders, setOrders] = useState([]);
    const [stats, setStats] = useState({ total: 0, pending: 0, inProgress: 0, delivered: 0, chartData: [] });
    const [isLoading, setIsLoading] = useState(true);
    const [searchQuery, setSearchQuery] = useState('');
    const [statusFilter, setStatusFilter] = useState('all');
    const [restaurantFilter, setRestaurantFilter] = useState('all');
    const [actionModal, setActionModal] = useState(null);
    const [currentPage, setCurrentPage] = useState(0);
    const [totalPages, setTotalPages] = useState(0);

    const fetchOrdersAndStats = async () => {
        setIsLoading(true);
        try {
            const [orderResponse, statsResponse] = await Promise.all([
                OrderService.getAdminOrders(searchQuery, statusFilter, restaurantFilter, currentPage, 10),
                OrderService.getAdminStats()
            ]);
            setOrders(orderResponse.data.content || []);
            setTotalPages(orderResponse.data.totalPages || 0);
            setStats(statsResponse.data);
        } catch (error) {
            console.error("Error fetching orders: ", error);
        } finally {
            setIsLoading(false);
        }
    };

    useEffect(() => {
        const timeoutId = setTimeout(() => {
            fetchOrdersAndStats();
        }, 500);
        return () => clearTimeout(timeoutId);
    }, [searchQuery, statusFilter, restaurantFilter, currentPage]);

    useEffect(() => {
        setCurrentPage(0);
    }, [searchQuery, statusFilter, restaurantFilter]);

    const handleActionConfirm = async () => {
        if (!actionModal || !actionModal.order) return;
        try {
            if (actionModal.type === 'cancel') {
                await OrderService.updateOrderStatus(actionModal.order.id, 'CANCELLED');
                alert("Order cancelled successfully!");
                fetchOrdersAndStats();
            } else if (actionModal.type === 'refund') {
                alert("Refunds have been issued to customers!");
            }
        } catch (error) {
            console.error("Operation error: ", error);
            alert("An error occurred, please try again.");
        } finally {
            setActionModal(null);
        }
    }

    return (
        <div className="container">
            <div className="admin-orders">
                <div className="header-section">
                    <div>
                        <h1>Order Management</h1>
                        <p className="header-subtitle">Monitor and manage all platform orders</p>
                    </div>
                </div>

                <OrderStats stats={stats} />

                <OrderFilterBar
                    searchQuery={searchQuery}
                    setSearchQuery={setSearchQuery}
                    statusFilter={statusFilter}
                    setStatusFilter={setStatusFilter}
                />

                <OrderTable
                    orders={orders}
                    isLoading={isLoading}
                    setActionModal={setActionModal}
                    currentPage={currentPage}
                    totalPages={totalPages}
                    setCurrentPage={setCurrentPage}
                />

                <OrderChart chartData={stats.chartData} />

                <OrderActionModal
                    actionModal={actionModal}
                    setActionModal={setActionModal}
                    handleActionConfirm={handleActionConfirm}
                />
            </div>
        </div>
    );
}