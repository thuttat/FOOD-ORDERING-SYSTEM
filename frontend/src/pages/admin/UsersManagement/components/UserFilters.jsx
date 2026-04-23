import React from "react";
import { Search } from "lucide-react";
import { Input } from "../../../../components/common/Input";
import { Select } from "../../../../components/common/Select";

export default function UserFilters({
                                        searchQuery,
                                        setSearchQuery,
                                        roleFilter,
                                        setRoleFilter,
                                        statusFilter,
                                        setStatusFilter
                                    }) {
    return (
        <div className="filters">
            <Input
                placeholder="Search users..."
                icon={<Search />}
                value={searchQuery}
                onChange={(e) => setSearchQuery(e.target.value)}
            />

            <Select
                options={[
                    { value: 'all', label: 'All Roles' },
                    { value: 'user', label: 'Customers' },
                    { value: 'restaurant', label: 'Restaurants' },
                    { value: 'admin', label: 'Admin' },
                ]}
                value={roleFilter}
                onChange={(e) => setRoleFilter(e.target.value)}
            />

            <Select
                options={[
                    { value: 'all', label: 'All Status' },
                    { value: 'active', label: 'Active' },
                    { value: 'unactive', label: 'Unactive' },
                ]}
                value={statusFilter}
                onChange={(e) => setStatusFilter(e.target.value)}
            />
        </div>
    );
}