import { useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import axios from 'axios'
import '../LoginForm.css'   // Import stylów dla panelu

export default function LoginForm({ setUser }) {
    const [credentials, setCredentials] = useState({
        username: '',
        password: ''
    })
    const [error, setError] = useState(null)
    const [loading, setLoading] = useState(false)
    const navigate = useNavigate()

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

            // backend musi zwracać JSON: { token, username, role, id }
            const userData = res.data

            localStorage.setItem('user', JSON.stringify(userData))
            axios.defaults.headers.common['Authorization'] = `Bearer ${userData.token}`
            setUser(userData)

            console.log('Zalogowano:', userData)
            // Po zalogowaniu możemy przekierować na stronę główną
            navigate('/')
        } catch (err) {
            console.error(err)
            setError('Nieprawidłowy login lub hasło')
        } finally {
            setLoading(false)
        }
    }

    return (
        <div className="login-container">
            <div className="login-panel">
                <h2>Logowanie</h2>

                <form onSubmit={handleSubmit}>
                    <div className="field-group">
                        <label htmlFor="username">Login:</label>
                        <input
                            id="username"
                            type="text"
                            name="username"
                            value={credentials.username}
                            onChange={handleChange}
                            required
                            placeholder="Wpisz login"
                            className="login-input"
                        />
                    </div>

                    <div className="field-group">
                        <label htmlFor="password">Hasło:</label>
                        <input
                            id="password"
                            type="password"
                            name="password"
                            value={credentials.password}
                            onChange={handleChange}
                            required
                            placeholder="Wpisz hasło"
                            className="login-input"
                        />
                    </div>

                    {error && <p className="error-text">{error}</p>}

                    <button type="submit" className="login-button" disabled={loading}>
                        {loading ? 'Trwa logowanie…' : 'Zaloguj się'}
                    </button>
                </form>

                <div className="login-footer">
                    <p>Nie masz konta? <Link to="/register" className="register-link">Zarejestruj się</Link></p>
                    <button onClick={() => navigate('/')} className="back-button">
                        Powrót do strony głównej
                    </button>
                </div>
            </div>
        </div>
    )
}
