import { useState } from 'react'
import { Link } from 'react-router-dom'
import axios from 'axios'
import '../UserMenu.css'

export default function UserMenu({ user, setUser }) {
    const [open, setOpen] = useState(false)

    const toggleMenu = () => {
        setOpen(prev => !prev)
    }

    const handleLogout = () => {
        localStorage.removeItem('user')
        delete axios.defaults.headers.common['Authorization']
        setUser(null)
        setOpen(false)
    }

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
                            <p>Jesteś zalogowany!</p>
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
