import React, { createContext, useContext, useState, useEffect } from 'react';

const AuthContext = createContext();

function AuthProvider({ children }) {
    const [user, setUser] = useState(null);

    useEffect(() => {
        const storedUser = JSON.parse(localStorage.getItem('user'))
        setUser(storedUser)
    }, []);

    const getUser = () => {
        const storedUser = JSON.parse(localStorage.getItem('user'));
        console.log("Stored User:", storedUser); // Debugging log
        return storedUser;
        //return JSON.parse(localStorage.getItem('user'))
    }

    const userIsAuthenticated = () => {
        return localStorage.getItem('user') !== null
    }

    const userLogin = (userData) => {
        localStorage.setItem('user', JSON.stringify(userData));
        setUser(userData); // Use userData instead of user
    };

    const userLogout = () => {
        localStorage.removeItem('user');
        setUser(null);
    };

    const contextValue = {
        user,
        getUser,
        userIsAuthenticated,
        userLogin,
        userLogout,
    };

    return (
        <AuthContext.Provider value={contextValue}>
            {children}
        </AuthContext.Provider>
    );
}

export default AuthContext;

export function useAuth() {
    return useContext(AuthContext);
}

export { AuthProvider };
