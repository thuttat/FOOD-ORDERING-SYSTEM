import React from 'react';
import { Navigate } from 'react-router-dom';

const PrivateRoute = ({ children, allowedRoles }) => {
    const token = localStorage.getItem("accessToken") || localStorage.getItem("token");
    const currentRole = localStorage.getItem("role");

    if (!token || !currentRole) {
        localStorage.removeItem("accessToken");
        localStorage.removeItem("token");
        localStorage.removeItem("role");
        return <Navigate to="/login" replace />;
    }
    const hasAccess = allowedRoles.some(role => currentRole.includes(role));

    if (!hasAccess) {
        console.warn("Unauthorized access attempt. Redirecting to home...");
        return <Navigate to="/" replace />;
    }

    return children;
};

export default PrivateRoute;