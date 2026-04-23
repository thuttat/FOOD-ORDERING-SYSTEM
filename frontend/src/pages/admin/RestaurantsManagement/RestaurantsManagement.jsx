import React, { useEffect, useState } from 'react';
import { Search } from 'lucide-react';
import { Input } from '../../../components/common/Input';
import { Button } from '../../../components/common/Button';
import { RestaurantService } from "../../../apis/RestaurantService.js";
import './RestaurantsManagement.css';

import { ActiveRestaurants } from './components/ActiveRestaurants.jsx';
import { PendingTable } from './components/PendingTable.jsx';
import { SuspendedTable } from './components/SuspendedTable.jsx';
import { DetailPanel } from './components/DetailPanel.jsx';
import { RejectModal } from './components/RejectModal.jsx';
import { ApprovalChart } from './components/ApprovalChart.jsx';
import {StatsSection} from "./components/StatsSection.jsx";
import {Pagination} from "../../../components/common/Pagination.jsx";

export function RestaurantsManagement() {
    const [activeTab, setActiveTab] = useState('active');
    const [searchQuery, setSearchQuery] = useState('');
    const [selectedRestaurant, setSelectedRestaurant] = useState(null);
    const [selectedForApproval, setSelectedForApproval] = useState(new Set());
    const [rejectModalOpen, setRejectModalOpen] = useState(false);
    const [restaurants, setRestaurants] = useState([]);
    const [isLoading, setIsLoading] = useState(true);
    const [rejectingId, setRejectingId] = useState(null);
    const [currentPage, setCurrentPage] = useState(0);
    const ITEMS_PER_PAGE = 10;

    useEffect(() => {
        setCurrentPage(0);
    }, [activeTab, searchQuery]);

    const fetchRestaurants = async () => {
        try {
            setIsLoading(true);
            const response = await RestaurantService.getRestaurants();
            if (response.status === 200) {
                setRestaurants(response.data);
            }
        } catch (error) {
            console.error("Failed to fetch restaurants", error);
        } finally {
            setIsLoading(false);
        }
    };

    useEffect(() => {
        fetchRestaurants();
    }, []);

    const filteredRestaurants = restaurants.filter(restaurant => {
        if (!searchQuery.trim()) return true;
        const query = searchQuery.toLowerCase();
        return (
            (restaurant.name && restaurant.name.toLowerCase().includes(query)) ||
            (restaurant.ownerName && restaurant.ownerName.toLowerCase().includes(query)) ||
            (restaurant.phoneNumber && restaurant.phoneNumber.toLowerCase().includes(query)) ||
            (restaurant.address && restaurant.address.toLowerCase().includes(query)) ||
            (restaurant.description && restaurant.description.toLowerCase().includes(query))
        );
    });

    const activeRestaurants = filteredRestaurants.filter(r => r.status === "ACTIVE");
    const pendingRestaurants = filteredRestaurants.filter(r => r.status === "PENDING");
    const lockedRestaurants = filteredRestaurants.filter(r => r.status === "LOCKED");

    let currentTabList = [];
    if (activeTab === 'active') currentTabList = activeRestaurants;
    else if (activeTab === 'pending') currentTabList = pendingRestaurants;
    else if (activeTab === 'suspended') currentTabList = lockedRestaurants;

    const totalPages = Math.ceil(currentTabList.length / ITEMS_PER_PAGE);
    const paginatedData = currentTabList.slice(
        currentPage * ITEMS_PER_PAGE,
        (currentPage + 1) * ITEMS_PER_PAGE
    );

    const totalProcessed = activeRestaurants.length + lockedRestaurants.length;
    const approvalRate = totalProcessed === 0 ? 0 : Math.round((activeRestaurants.length / totalProcessed) * 100);

    const getChartData = () => {
        const monthNames = ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"];
        const dataMap = {};
        const today = new Date();

        for (let i = 5; i >= 0; i--) {
            const d = new Date(today.getFullYear(), today.getMonth() - i, 1);
            const monthStr = monthNames[d.getMonth()];
            dataMap[monthStr] = { month: monthStr, approved: 0, rejected: 0 };
        }

        restaurants.forEach(r => {
            if (!r.createdAt) return;
            const d = new Date(r.createdAt);
            const monthStr = monthNames[d.getMonth()];

            if (dataMap[monthStr]) {
                if (r.status === "ACTIVE") dataMap[monthStr].approved += 1;
                if (r.status === "LOCKED") dataMap[monthStr].rejected += 1;
            }
        });

        return Object.values(dataMap);
    };

    const handleApprove = async (id) => {
        try {
            await RestaurantService.approveRestaurant(id);
            alert("Approved restaurant successfully!");
            setSelectedRestaurant(null);
            fetchRestaurants();
        } catch (error) {
            console.error("Failed to approve restaurant", error);
            alert("Cannot approve restaurant. Please try again!");
        }
    };

    const handleLock = async (id) => {
        if (!window.confirm("Are you sure you want to suspend this restaurant?")) return;
        try {
            await RestaurantService.lockRestaurant(id);
            alert("Suspended restaurant successfully!");
            setSelectedRestaurant(null);
            fetchRestaurants();
        } catch (error) {
            console.error("Failed to suspend restaurant", error);
            alert("Cannot suspend restaurant. Please try again!");
        }
    };

    const toggleSelection = (id) => {
        const newSelection = new Set(selectedForApproval);
        if (newSelection.has(id)) {
            newSelection.delete(id);
        } else {
            newSelection.add(id);
        }
        setSelectedForApproval(newSelection);
    };

    const handleSelectAll = (e) => {
        if (e.target.checked) {
            const allPendingIds = paginatedData.map(r => r.id);
            setSelectedForApproval(new Set(allPendingIds));
        } else {
            setSelectedForApproval(new Set());
        }
    };

    const isAllSelected = pendingRestaurants.length > 0 && selectedForApproval.size === pendingRestaurants.length;

    const handleBulkApprove = async () => {
        try {
            const promises = Array.from(selectedForApproval).map(id => RestaurantService.approveRestaurant(id));
            await Promise.all(promises);
            alert(`Approved ${selectedForApproval.size} restaurants successfully!`);
            setSelectedForApproval(new Set());
            fetchRestaurants();
        } catch (error) {
            console.error("Failed to bulk approve:", error);
            alert("Error in bulk approve process. Please try again!");
        }
    };

    const handleReject = async () => {
        if (!rejectingId) return;
        try {
            await RestaurantService.lockRestaurant(rejectingId);
            alert("Rejected restaurant successfully!");
            setRejectModalOpen(false);
            setRejectingId(null);
            fetchRestaurants();
        } catch (error) {
            console.error("Failed to reject restaurant", error);
            alert("Cannot reject restaurant. Please try again!");
        }
    };

    const handleReinstate = async (id) => {
        if (!window.confirm("Are you sure you want to reinstate this restaurant?")) return;
        try {
            await RestaurantService.reinstateRestaurant(id);
            alert("Reinstated restaurant successfully!");
            setSelectedRestaurant(null);
            fetchRestaurants();
        } catch (error) {
            console.error("Failed to suspend restaurant", error);
            alert("Cannot suspend restaurant. Please try again!");
        }
    };

    const formatDate = (date) => {
        if (!date) return "N/A";
        return new Date(date).toLocaleDateString('vi-VN');
    };

    const openRejectModal = (id) => {
        setRejectingId(id);
        setRejectModalOpen(true);
        setSelectedRestaurant(null);
    };

    return (
        <div className="container">
            <div className="admin-restaurants-container">
                <div className="header-section">
                    <div>
                        <h1>Restaurant Management</h1>
                        <p className="header-subtitle">Monitor and manage all restaurants on the platform</p>
                    </div>
                    {activeTab === 'pending' && selectedForApproval.size > 0 && (
                        <Button onClick={handleBulkApprove}>
                            Approve Selected ({selectedForApproval.size})
                        </Button>
                    )}
                </div>

                <StatsSection
                    active={activeRestaurants.length}
                    pending={pendingRestaurants.length}
                    locked={lockedRestaurants.length}
                    rate={approvalRate}
                />

                <div className="tabs-wrapper">
                    <div className="tabs-list">
                        {[
                            { id: 'active', label: 'Active Restaurants', count: activeRestaurants.length },
                            { id: 'pending', label: 'Pending Approval', count: pendingRestaurants.length },
                            { id: 'suspended', label: 'Suspended', count: lockedRestaurants.length },
                        ].map((tab) => (
                            <button
                                key={tab.id}
                                onClick={() => setActiveTab(tab.id)}
                                className={`tab-btn ${activeTab === tab.id ? 'active' : ''}`}
                            >
                                {tab.label} <span className="tab-count">({tab.count})</span>
                            </button>
                        ))}
                    </div>
                </div>

                <div className="search-container">
                    <Input
                        placeholder="Search restaurants..."
                        icon={<Search className="w-5 h-5" />}
                        value={searchQuery}
                        onChange={(e) => setSearchQuery(e.target.value)}
                    />
                </div>

                {isLoading ? (
                    <div className="text-center py-10 text-muted">Loading...</div>
                ) : (
                    <>
                        {activeTab === 'active' && (
                            <>
                                <ActiveRestaurants
                                    restaurants={paginatedData}
                                    onSelect={setSelectedRestaurant}
                                    onSuspend={handleLock}
                                />
                                <div style={{ marginTop: '20px', display: 'flex', justifyContent: 'center' }}>
                                    <Pagination currentPage={currentPage} totalPages={totalPages} setCurrentPage={setCurrentPage} />
                                </div>
                            </>
                        )}

                        {activeTab === 'pending' && (
                            <>
                                <PendingTable
                                    restaurants={paginatedData}
                                    selectedForApproval={selectedForApproval}
                                    isAllSelected={isAllSelected}
                                    onToggleSelection={toggleSelection}
                                    onSelectAll={handleSelectAll}
                                    onSelect={setSelectedRestaurant}
                                    onApprove={handleApprove}
                                    onRejectClick={openRejectModal}
                                    formatDate={formatDate}
                                />
                                <div style={{ marginTop: '20px', display: 'flex', justifyContent: 'center' }}>
                                    <Pagination currentPage={currentPage} totalPages={totalPages} setCurrentPage={setCurrentPage} />
                                </div>
                            </>
                        )}

                        {activeTab === 'suspended' && (
                            <>
                                <SuspendedTable
                                    restaurants={paginatedData}
                                    onSelect={setSelectedRestaurant}
                                    onReinstate={handleReinstate}
                                    formatDate={formatDate}
                                />
                                <div style={{ marginTop: '20px', display: 'flex', justifyContent: 'center' }}>
                                    <Pagination currentPage={currentPage} totalPages={totalPages} setCurrentPage={setCurrentPage} />
                                </div>
                            </>
                        )}
                    </>
                )}

                {selectedRestaurant && (
                    <DetailPanel
                        restaurant={selectedRestaurant}
                        activeTab={activeTab}
                        onClose={() => setSelectedRestaurant(null)}
                        onApprove={handleApprove}
                        onRejectClick={openRejectModal}
                        onSuspend={handleLock}
                        onReinstate={handleReinstate}
                    />
                )}

                {rejectModalOpen && (
                    <RejectModal
                        onClose={() => {
                            setRejectModalOpen(false);
                            setRejectingId(null);
                        }}
                        onConfirm={handleReject}
                    />
                )}

                {activeTab === 'pending' && (
                    <ApprovalChart data={getChartData()} />
                )}
            </div>
        </div>
    );
}