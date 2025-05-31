// src/components/UserMenu.jsx

import { useState } from 'react'
import { Link } from 'react-router-dom'
import axios from 'axios'

export default function UserMenu({ user, setUser }) {
    const [open, setOpen] = useState(false)

    const toggleMenu = () => {
        setOpen(prev => !prev)
    }

    const handleLogout = () => {
        // 1) Usuń dane z localStorage
        localStorage.removeItem('user')
        // 2) Usuń domyślny nagłówek axiosa
        delete axios.defaults.headers.common['Authorization']
        // 3) Wyczyść stan w App (ustaw user na null)
        setUser(null)
        // 4) Zamknij dropdown
        setOpen(false)
    }

    return (
        <div className="user-menu">
            {/* Przycisk ze strzałką */}
            <button
                className="user-menu-button"
                onClick={toggleMenu}
                aria-label="User menu"
            >
                {open ? '▲' : '▼'}
            </button>

            {/* Jeśli open === true, pokazujemy mały panel */}
            {open && (
                <div className="user-dropdown">
                    {user ? (
                        <>
                            <p>Jesteś zalogowany!</p>
                            {/* Możesz tu wypisać np. user.username,
                  ale wystarczy prosty tekst */}
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
    )
}
