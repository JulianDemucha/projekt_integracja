import { useState, useEffect } from 'react'
import axios from 'axios'

export default function LoginForm() {
    const [credentials, setCredentials] = useState({
        username: '',
        password: ''
    })
    const [error, setError] = useState(null)
    const [loading, setLoading] = useState(false)
    const [user, setUser] = useState(null)

    // 1) Przy starcie komponentu sprawdzamy, czy w localStorage jest już zapisany użytkownik
    useEffect(() => {
        const stored = localStorage.getItem('user')
        if (stored) {
            const parsed = JSON.parse(stored)
            // Ustawiamy nagłówek dla axiosa, żeby każde kolejne żądanie zawierało token
            axios.defaults.headers.common['Authorization'] = `Bearer ${parsed.token}`
            setUser(parsed)
        }
    }, [])

    const handleChange = e => {
        const { name, value } = e.target
        setCredentials(prev => ({ ...prev, [name]: value }))
    }

    const handleSubmit = async e => {
        e.preventDefault()
        setError(null)
        setLoading(true)
        try {
            const res = await axios.post(
                'http://localhost:8080/auth/login',
                {
                    username: credentials.username,
                    password: credentials.password
                }
            )
            // Zakładamy, że backend zwraca JSON zawierający co najmniej pole "token",
            // oraz "username", "role" i "id". Przykład odpowiedzi:
            // { token: "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...", username: "jan", role: "ROLE_USER", id: 42 }

            const userData = res.data

            // 2) Zapisujemy usera + token do localStorage
            localStorage.setItem('user', JSON.stringify(userData))

            // 3) Ustawiamy nagłówek axiosa, aby token szedł w każdym kolejnym żądaniu
            axios.defaults.headers.common['Authorization'] = `Bearer ${userData.token}`

            // 4) Ustawiamy stan "user", żeby wyświetlić widok zalogowanego
            setUser(userData)
            console.log('Zalogowano:', userData)
        } catch (err) {
            console.error(err)
            setError('Nieprawidłowy login lub hasło')
        } finally {
            setLoading(false)
        }
    }

    const handleLogout = () => {
        // 5) Wylogowanie: czyścimy localStorage i nagłówek, ustawiamy user na null
        localStorage.removeItem('user')
        delete axios.defaults.headers.common['Authorization']
        setUser(null)
    }

    // Jeśli user jest zalogowany, wyświetlamy dane i przycisk "Wyloguj"
    if (user) {
        return (
            <div>
                <p>Zalogowany jako <strong>{user.username}</strong></p>
                <p>Rola: {user.role}</p>
                <p>ID: {user.id}</p>
                <button onClick={handleLogout}>Wyloguj się</button>
            </div>
        )
    }

    // W przeciwnym razie wyświetlamy formularz logowania
    return (
        <form onSubmit={handleSubmit}>
            <h2>Logowanie</h2>

            <div>
                <label>
                    Login:
                    <input
                        type="text"
                        name="username"
                        value={credentials.username}
                        onChange={handleChange}
                        required
                    />
                </label>
            </div>

            <div>
                <label>
                    Hasło:
                    <input
                        type="password"
                        name="password"
                        value={credentials.password}
                        onChange={handleChange}
                        required
                    />
                </label>
            </div>

            {error && <p style={{ color: 'red' }}>{error}</p>}

            <button type="submit" disabled={loading}>
                {loading ? 'Trwa logowanie…' : 'Zaloguj się'}
            </button>
        </form>
    )
}
