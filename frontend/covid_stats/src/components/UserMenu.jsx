import React, { useState, useContext } from 'react';
import { Link } from 'react-router-dom';
import '../UserMenu.css';
import { AuthContext } from '../context/AuthContext';

export default function UserMenu() {
    const [open, setOpen] = useState(false);
    const { user, logout } = useContext(AuthContext);

    const toggleMenu = () => {
        setOpen(prev => !prev);
    };

    const handleLogout = () => {
        // usuniecie usera z kontekstu i localStorage, czyszczenie naglowka axiosa
        logout();
        setOpen(false);
    };

    return (
        <div className="user-menu">
            <button
                className="user-menu-button"
                onClick={toggleMenu}
                aria-label="User menu"
            >
                {open ? '▲' : '▼'}
            </button>

            {open && (
                <div className="user-dropdown">
                    {user ? (
                        <>
                            <p>Jesteś zalogowany jako <strong>{user.username}</strong></p>
                            <button onClick={handleLogout}>Wyloguj</button>
                        </>
                    ) : (
                        <>
                            <Link to="/auth/login" onClick={() => setOpen(false)}>
                                Zaloguj
                            </Link>
                            <Link to="/register" onClick={() => setOpen(false)}>
                                Zarejestruj
                            </Link>
                        </>
                    )}
                </div>
            )}
        </div>
    );
}
