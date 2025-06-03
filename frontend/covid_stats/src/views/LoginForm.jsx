// src/views/LoginForm.jsx
import { useState, useContext } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import axios from 'axios';
import { AuthContext } from '../context/AuthContext';
import '../LoginForm.css'; // jeśli masz własne style

export default function LoginForm() {
    const { setUser, setAuthHeader } = useContext(AuthContext);
    const [credentials, setCredentials] = useState({
        username: '',
        password: '',
    });
    const [error, setError] = useState(null);
    const [loading, setLoading] = useState(false);
    const navigate = useNavigate();

    const handleChange = (e) => {
        const { name, value } = e.target;
        setCredentials((prev) => ({ ...prev, [name]: value }));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError(null);
        setLoading(true);
        try {
            // Tworzymy nagłówek Basic
            const basicHeader = 'Basic ' + btoa(`${credentials.username}:${credentials.password}`);
            const response = await axios.post(
                'http://localhost:8080/auth/login',
                {
                    username: credentials.username,
                    password: credentials.password,
                },
                {
                    headers: {
                        Authorization: basicHeader
                    }
                }
            );

            const userData = response.data; // backend zwraca { id, username, role }
            // Zapisujemy usera i nagłówek w konteście i localStorage
            setUser(userData);
            setAuthHeader(basicHeader);

            localStorage.setItem('user', JSON.stringify(userData));
            localStorage.setItem('basicCreds', basicHeader);

            navigate('/');
        } catch (err) {
            console.error(err);
            setError('Nieprawidłowy login lub hasło');
        } finally {
            setLoading(false);
        }
    };

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

                    <button
                        type="submit"
                        className="login-button"
                        disabled={loading}
                    >
                        {loading ? 'Trwa logowanie…' : 'Zaloguj się'}
                    </button>
                </form>

                <div className="login-footer">
                    <p>
                        Nie masz konta?{' '}
                        <Link to="/register" className="register-link">
                            Zarejestruj się
                        </Link>
                    </p>
                    <button
                        onClick={() => navigate('/')}
                        className="back-button"
                    >
                        Powrót do strony głównej
                    </button>
                </div>
            </div>
        </div>
    );
}
