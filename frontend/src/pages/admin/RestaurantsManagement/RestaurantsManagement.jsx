import React, { useEffect, useState } from 'react';
import { Search } from 'lucide-react';
import { Input } from '../../../components/common/Input';
import { Button } from '../../../components/common/Button';
import { RestaurantService } from "../../../apis/RestaurantService.js";
import './RestaurantsManagement.css';

import { ActiveRestaurants } from './components/ActiveRestaurants.jsx';
import { SuspendedTable } from './components/SuspendedTable.jsx';
import { DetailPanel } from './components/DetailPanel.jsx';
import {StatsSection} from "./components/StatsSection.jsx";
import {Pagination} from "../../../components/common/Pagination.jsx";
import UserService from "../../../apis/UserService.js";
import CreateRestaurantModal from "./components/CreateRestaurantModal.jsx";

export function RestaurantsManagement() {
    const [activeTab, setActiveTab] = useState('active');
    const [searchQuery, setSearchQuery] = useState('');
    const [selectedRestaurant, setSelectedRestaurant] = useState(null);
    const [restaurants, setRestaurants] = useState([]);
    const [isLoading, setIsLoading] = useState(true);
    const [currentPage, setCurrentPage] = useState(0);
    const ITEMS_PER_PAGE = 10;
    const [potentialOwners, setPotentialOwners] = useState([]);
    const [isCreateModalOpen, setIsCreateModalOpen] = useState(false);

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

    const fetchPotentialOwners = async () => {
        try {
            const response = await UserService.getAllUsers({ role: 'RESTAURANT', size: 1000 });
            const data = response.data || response;
            const ownerOptions = (data.content || []).map(u => ({
                value: u.id,
                label: `${u.fullname} (@${u.username})`
            }));
            setPotentialOwners(ownerOptions);
        } catch (error) {
            console.error("Failed to fetch potential owners", error);
        }
    };

    useEffect(() => {
        fetchRestaurants();
        fetchPotentialOwners();
    }, []);

    const handleCreateRestaurant = async (formData) => {
        try {
            await RestaurantService.createRestaurant({ ...formData, isOpen: true });
            alert("Restaurant created successfully! Automatically activated.");
            setIsCreateModalOpen(false);
            fetchRestaurants();
        } catch (error) {
            console.error("Error creating restaurant", error);
            alert("Error: Please double-check that the Owner is correct.");
        }
    };

    const filteredRestaurants = restaurants.filter(restaurant => {
        if (!searchQuery.trim()) return true;
        const query = searchQuery.toLowerCase();
        return (
            (restaurant.name && restaurant.name.toLowerCase().includes(query)) ||
            (restaurant.ownerName && restaurant.ownerName.toLowerCase().includes(query)) ||
            (restaurant.phoneNumber && restaurant.phoneNumber.toLowerCase().includes(query))
        );
    });

    const activeRestaurants = filteredRestaurants.filter(r => r.status === "ACTIVE");
    const lockedRestaurants = filteredRestaurants.filter(r => r.status === "LOCKED");

    let currentTabList = activeTab === 'active' ? activeRestaurants : lockedRestaurants;

    const totalPages = Math.ceil(currentTabList.length / ITEMS_PER_PAGE);
    const paginatedData = currentTabList.slice(
        currentPage * ITEMS_PER_PAGE,
        (currentPage + 1) * ITEMS_PER_PAGE
    );

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

    return (
        <div className="container">
            <div className="admin-restaurants-container">
                <div className="header-section" style={{ display: 'flex', justifyContent: 'space-between' }}>
                    <div>
                        <h1>Restaurant Management</h1>
                        <p className="header-subtitle">Monitor and manage all restaurants on the platform</p>
                    </div>
                    <Button className="btn-primary" onClick={() => setIsCreateModalOpen(true)}>
                        + Create new restaurant
                    </Button>
                </div>

                <StatsSection
                    active={activeRestaurants.length}
                    locked={lockedRestaurants.length}
                    pending={0} rate={100}
                />

                <div className="tabs-wrapper">
                    <div className="tabs-list">
                        <button onClick={() => setActiveTab('active')} className={`tab-btn ${activeTab === 'active' ? 'active' : ''}`}>
                            Active <span className="tab-count">({activeRestaurants.length})</span>
                        </button>
                        <button onClick={() => setActiveTab('suspended')} className={`tab-btn ${activeTab === 'suspended' ? 'active' : ''}`}>
                            Suspended <span className="tab-count">({lockedRestaurants.length})</span>
                        </button>
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
                        onSuspend={handleLock}
                        onReinstate={handleReinstate}
                    />
                )}

                <CreateRestaurantModal
                    isOpen={isCreateModalOpen}
                    onClose={() => setIsCreateModalOpen(false)}
                    handleCreate={handleCreateRestaurant}
                    owners={potentialOwners}
                />
            </div>
        </div>
    );
}