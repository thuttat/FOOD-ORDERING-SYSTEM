import React from 'react';
import { Search } from 'lucide-react';
import { Input } from '../../../../components/common/Input';
import { Select } from '../../../../components/common/Select';

export function OrderFilterBar({ searchQuery, setSearchQuery, statusFilter, setStatusFilter }) {
    return (
        <div className="filter-bar">
            <div className="filter-search-input">
                <Input
                    placeholder="Search by Order ID or Customer..."
                    icon={<Search className="action-icon" />}
                    value={searchQuery}
                    onChange={(e) => setSearchQuery(e.target.value)}
                />
            </div>
            <Select
                options={[
                    { value: 'all', label: 'All Status' },
                    { value: 'PENDING', label: 'Pending' },
                    { value: 'CONFIRMED', label: 'Confirmed' },
                    { value: 'PREPARING', label: 'Preparing' },
                    { value: 'OUT_FOR_DELIVERY', label: 'Delivering' },
                    { value: 'DELIVERED', label: 'Delivered' },
                    { value: 'COMPLETED', label: 'Completed' },
                    { value: 'CANCELLED', label: 'Cancelled' },
                ]}
                value={statusFilter}
                onChange={(e) => setStatusFilter(e.target.value)}
            />
        </div>
    );
}