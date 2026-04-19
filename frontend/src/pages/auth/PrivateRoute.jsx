import React from 'react';
import { Navigate } from 'react-router-dom';

const PrivateRoute = ({ children, allowedRoles }) => {
    const token = localStorage.getItem("accessToken");

    const currentRole = localStorage.getItem("role");

    if (!token || !currentRole) {
        localStorage.removeItem("accessToken");
        localStorage.removeItem("role");
        return <Navigate to="/" replace />;
    }

    const hasAccess = allowedRoles.some(role => currentRole.includes(role));

    if (!hasAccess) {
        console.warn("Unauthorized...");
        localStorage.removeItem("accessToken");
        localStorage.removeItem("role");
        return <Navigate to="/" replace />;
    }

    return children;
};

export default PrivateRoute;