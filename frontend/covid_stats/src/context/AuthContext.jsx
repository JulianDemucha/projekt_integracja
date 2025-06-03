// src/context/AuthContext.jsx
import React, { createContext, useEffect, useState } from 'react';
import axios from 'axios';

export const AuthContext = createContext({
    user: null,
    setUser: () => {},
    setAuthHeader: () => {},
    logout: () => {},
});

export const AuthProvider = ({ children }) => {
    const [user, setUser] = useState(null);

    // Przy mountowaniu – sprawdzamy localStorage
    useEffect(() => {
        const storedUser = localStorage.getItem('user');
        const storedCreds = localStorage.getItem('basicCreds');
        if (storedUser && storedCreds) {
            setUser(JSON.parse(storedUser));
            axios.defaults.headers.common['Authorization'] = storedCreds;
        }
    }, []);

    const setAuthHeader = (basicHeader) => {
        axios.defaults.headers.common['Authorization'] = basicHeader;
    };

    const logout = () => {
        setUser(null);
        delete axios.defaults.headers.common['Authorization'];
        localStorage.removeItem('user');
        localStorage.removeItem('basicCreds');
    };

    return (
        <AuthContext.Provider value={{ user, setUser, setAuthHeader, logout }}>
            {children}
        </AuthContext.Provider>
    );
};
