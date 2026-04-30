import React, {useEffect, useState} from 'react';
import './UsersManagement.css';
import UserService from "../../../apis/UserService.js";
import UsersStats from "./components/UsersStats.jsx";
import UserFilters from "./components/UserFilters.jsx";
import UsersTable from "./components/UsersTable.jsx";
import UsersChart from "./components/UsersChart.jsx";
import EditUserModal from "./components/EditUserModal.jsx";
import {Button} from "../../../components/common/Button.jsx";
import CreateUserModal from "./components/CreateUserModal.jsx";

export function UsersManagement() {
    const [users, setUsers] = useState([]);
    const [searchQuery, setSearchQuery] = useState('');
    const [isLoading, setIsLoading] = useState(true);
    const [isCreateModalOpen, setIsCreateModalOpen] = useState(false);
    const [roleFilter, setRoleFilter] = useState('all');
    const [statusFilter, setStatusFilter] = useState('all');
    const [editingUser, setEditingUser] = useState(null);
    const [pageInfo, setPageInfo] = useState({ current: 0, total: 0, totalElements: 0 });
    const [systemStats, setSystemStats] = useState({ total: 0, activeCustomers: 0, activeRestaurants: 0, growthData: [] });

    const fetchStats = async () => {
        try {
            const res = await UserService.getStats();
            const data = res.data ? res.data : res;
            setSystemStats({
                total: data.total || 0,
                activeCustomers: data.activeCustomers || 0,
                activeRestaurants: data.activeRestaurants || 0,
                growthData: data.growthData || []
            });
        } catch (error) {
            console.error("Failed to fetch system stats", error);
        }
    };

    const fetchUsers = async (page = 0) => {
        try {
            setIsLoading(true);
            const params = {
                page: page,
                size: 20,
                search: searchQuery || null,
                role: roleFilter === 'all' ? null : roleFilter.toUpperCase(),
                status: statusFilter === 'all' ? null : (statusFilter === 'unactive' ? 'INACTIVE' : statusFilter.toUpperCase())
            };

            const response = await UserService.getAllUsers(params);
            const data = response.data ? response.data : response;
            setUsers(data.content || []);
            setPageInfo({
                current: data.number || 0,
                total: data.totalPages || 0,
                totalElements: data.totalElements || 0
            });
        } catch (error) {
            console.error("Failed to fetch users: ", error);
            setUsers([]);
        } finally {
            setIsLoading(false);
        }
    };

    useEffect(() => {
        fetchStats();
    }, []);

    useEffect(() => {
        fetchUsers(0);
    }, [searchQuery, roleFilter, statusFilter]);

    const handleCreateUser = async (formData) => {
        try {
            await UserService.createUserByAdmin(formData);
            alert("User created successfully");
            setIsCreateModalOpen(false);
            fetchUsers(0);
            fetchStats();
        } catch (error) {
            console.error("Failed to create user", error);
            alert("Failed to create user");
        }
    };

    const handleToggleStatus = async (user) => {
        const newStatus = user.status === 'ACTIVE' ? 'INACTIVE' : 'ACTIVE';
        const displayStatus = newStatus === 'INACTIVE' ? 'UNACTIVE' : 'ACTIVE';
        if (!window.confirm(`Are you sure you want to change this user to ${displayStatus.toLowerCase()}?`)) return;
        try {
            await UserService.updateStatus(user.id, newStatus);
            fetchUsers();
        } catch (error) {
            console.error("Failed to update user: ", error);
            alert(`Failed to update user ${user.fullname}`);
        }
    };

    const handleChangeRole = async (userId, newRole) => {
        try {
            await UserService.updateRole(userId, newRole);
            fetchUsers();
        } catch (error) {
            console.error("Failed to update user: ", error);
            alert("Failed to update user role");
        }
    };

    const handleSaveUser = async () => {
        try {
            await UserService.updateUser(editingUser.id, {
                fullname: editingUser.fullname,
                email: editingUser.email,
                phone: editingUser.phone
            });
            alert("Update user information successfully!");
            setEditingUser(null);
            fetchUsers();
        } catch (error) {
            console.error("Failed to save user", error);
            alert("Failed to update user. Please try again!");
        }
    };

    return (
        <div className="container" >
            <div className="admin-users">
                <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start' }}>
                    <div>
                        <h1>User Management</h1>
                        <p className="header-subtitle">Manage customer and restaurant accounts</p>
                    </div>
                    <Button className="btn-primary" onClick={() => setIsCreateModalOpen(true)}>
                        + Create new user
                    </Button>
                </div>

                <UsersStats systemStats={systemStats} />

                <UserFilters
                    searchQuery={searchQuery}
                    setSearchQuery={setSearchQuery}
                    roleFilter={roleFilter}
                    setRoleFilter={setRoleFilter}
                    statusFilter={statusFilter}
                    setStatusFilter={setStatusFilter}
                />

                <UsersTable
                    users={users}
                    isLoading={isLoading}
                    pageInfo={pageInfo}
                    fetchUsers={fetchUsers}
                    setEditingUser={setEditingUser}
                    handleToggleStatus={handleToggleStatus}
                    handleChangeRole={handleChangeRole}
                />

                <UsersChart growthData={systemStats.growthData} />

                <EditUserModal
                    editingUser={editingUser}
                    setEditingUser={setEditingUser}
                    handleSaveUser={handleSaveUser}
                />

                <CreateUserModal
                    isOpen={isCreateModalOpen}
                    onClose={() => setIsCreateModalOpen(false)}
                    handleCreate={handleCreateUser}
                />
            </div>
        </div>
    );
}