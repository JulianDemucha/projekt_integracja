// src/App.jsx

import './App.css'
import {
    BrowserRouter as Router,
    Routes,
    Route,
    useLocation
} from 'react-router-dom'
import { useEffect, useState } from 'react'
import axios from 'axios'

import HomePage from './components/HomePage.jsx'
import LoginForm from './components/LoginForm.jsx'
import RegisterForm from './components/RegisterForm.jsx'
import UserMenu from './components/UserMenu.jsx'

function AppContent() {
    const [user, setUser] = useState(null)
    const location = useLocation() // Tutaj jest już w kontekście <Router>

    useEffect(() => {
        const stored = localStorage.getItem('user')
        if (stored) {
            const parsed = JSON.parse(stored)
            axios.defaults.headers.common['Authorization'] = `Bearer ${parsed.token}`
            setUser(parsed)
        }
    }, [])

    return (
        <>
            {/*
      */}
            {(location.pathname !== '/auth/login' && location.pathname !== '/register') &&  (
                <UserMenu user={user} setUser={setUser} />
            )}

            <Routes>
                <Route path="/" element={<HomePage />} />
                <Route
                    path="/auth/login"
                    element={<LoginForm setUser={setUser} />}
                />
                <Route path="/register" element={<RegisterForm />} />
            </Routes>
        </>
    )
}

export default function App() {
    return (
        // tutaj zeby AppContent mial dostep do useLocation()
        <Router>
            <AppContent />
        </Router>
    )
}
