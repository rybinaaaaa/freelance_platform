// src/contexts/AuthContext.js
import React, { createContext, useContext, useState } from 'react';
import Cookies from 'js-cookie';

const AuthContext = createContext(null);

export const useAuth = () => useContext(AuthContext);

export const AuthProvider = ({ children }) => {
    const [authToken, setAuthToken] = useState(Cookies.get('authToken'));

    const login = (token) => {
        Cookies.set('authToken', token, { expires: 7 });
        setAuthToken(token);
    };

    const logout = () => {
        Cookies.remove('authToken');
        setAuthToken(null);
    };

    return (
        <AuthContext.Provider value={{ authToken, login, logout }}>
            {children}
        </AuthContext.Provider>
    );
};
