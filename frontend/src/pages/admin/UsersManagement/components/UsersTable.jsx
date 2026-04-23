import React from "react";
import { Edit2, AlertCircle } from "lucide-react";
import { Badge } from "../../../../components/common/Badge";
import { Card, CardContent } from "../../../../components/common/Card";
import { Pagination } from "../../../../components/common/Pagination.jsx";

export default function UsersTable({
                                       users,
                                       isLoading,
                                       pageInfo,
                                       fetchUsers,
                                       setEditingUser,
                                       handleToggleStatus,
                                       handleChangeRole
                                   }) {
    return (
        <Card>
            <CardContent className="table-wrapper">
                <table className="user-table">
                    <thead>
                    <tr>
                        <th>User</th>
                        <th>Email</th>
                        <th>Role</th>
                        <th>Joined</th>
                        <th>Status</th>
                        <th>Actions</th>
                    </tr>
                    </thead>

                    <tbody>
                    {isLoading ? (
                        <tr>
                            <td colSpan="7" style={{ textAlign: 'center' }}>
                                Loading...
                            </td>
                        </tr>
                    ) : users.map((user) => {
                        const displayStatus =
                            user.status === 'INACTIVE' ? 'UNACTIVE' : user.status;

                        return (
                            <tr key={user.id}>
                                <td>{user.fullname}</td>
                                <td>{user.email}</td>

                                <td>
                                    <select
                                        value={user.role}
                                        className="role-select"
                                        onChange={(e) =>
                                            handleChangeRole(user.id, e.target.value)
                                        }
                                    >
                                        <option value="USER">Customer</option>
                                        <option value="RESTAURANT">Restaurant</option>
                                        <option value="ADMIN">Admin</option>
                                    </select>
                                </td>

                                <td>
                                    {new Date(user.createdAt).toLocaleDateString('en-US')}
                                </td>

                                <td>
                                    <Badge status={displayStatus === "ACTIVE" ? "active" : 'unactive'}>
                                        {displayStatus}
                                    </Badge>
                                </td>

                                <td>
                                    <div className="actions">
                                        <button onClick={() => setEditingUser(user)}>
                                            <Edit2 size={16} />
                                        </button>

                                        <button onClick={() => handleToggleStatus(user)}>
                                            <AlertCircle size={16} />
                                        </button>
                                    </div>
                                </td>
                            </tr>
                        );
                    })}
                    </tbody>
                </table>

                <Pagination
                    currentPage={pageInfo.current}
                    totalPages={pageInfo.total}
                    setCurrentPage={(action) => {
                        const newPage =
                            typeof action === 'function'
                                ? action(pageInfo.current)
                                : action;
                        fetchUsers(newPage);
                    }}
                />
            </CardContent>
        </Card>
    );
}