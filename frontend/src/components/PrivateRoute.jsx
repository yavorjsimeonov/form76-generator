import React from 'react';
import { Navigate } from 'react-router-dom';
import { useAuth } from './AuthContext';

function PrivateRoute({ children, roles }) {
    const { userIsAuthenticated, getUser } = useAuth();
    const user = getUser();

    // Redirect to login if not authenticated
    if (!userIsAuthenticated()) {
        return <Navigate to="/login" replace />;
    }

    // Handle roles for authorization
    if (roles && !roles.includes(user.role)) {
        return <div>You do not have the required permissions to access this page.</div>;
    }

    return children;
}

export default PrivateRoute;
