import { useState, useContext } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import axios from 'axios';
import { AuthContext } from '../context/AuthContext';
import '../RegisterForm.css';

export default function RegisterForm() {
    const { setUser, setAuthHeader } = useContext(AuthContext);
    const [form, setForm] = useState({
        username: '',
        password: '',
        confirmPassword: '',
    });
    const [error, setError] = useState(null);
    const [loading, setLoading] = useState(false);
    const navigate = useNavigate();

    const handleChange = (e) => {
        const { name, value } = e.target;
        setForm((prev) => ({ ...prev, [name]: value }));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError(null);

        if (!form.username || !form.password) {
            setError('Wypełnij wszystkie pola.');
            return;
        }
        if (form.password !== form.confirmPassword) {
            setError('Hasła nie są takie same.');
            return;
        }

        setLoading(true);
        try {
            // Rejestracja
            await axios.post('http://localhost:8080/users', {
                username: form.username,
                password: form.password,
            });

            // Automatyczne logowanie
            const basicHeader = 'Basic ' + btoa(`${form.username}:${form.password}`);
            const loginResponse = await axios.post(
                'http://localhost:8080/auth/login',
                { username: form.username, password: form.password },
                { headers: { Authorization: basicHeader } }
            );

            const userData = loginResponse.data;
            setUser(userData);
            setAuthHeader(basicHeader);
            localStorage.setItem('user', JSON.stringify(userData));
            localStorage.setItem('basicCreds', basicHeader);

            navigate('/');
        } catch (err) {
            console.error(err);
            setError(err.response?.data || 'Błąd rejestracji lub logowania');
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="register-container">
            <div className="register-panel">
                <h2>Rejestracja</h2>

                <form onSubmit={handleSubmit}>
                    <div className="field-group">
                        <label htmlFor="username">Login:</label>
                        <input
                            id="username"
                            type="text"
                            name="username"
                            value={form.username}
                            onChange={handleChange}
                            required
                            placeholder="Wpisz login"
                            className="register-input"
                        />
                    </div>

                    <div className="field-group">
                        <label htmlFor="password">Hasło:</label>
                        <input
                            id="password"
                            type="password"
                            name="password"
                            value={form.password}
                            onChange={handleChange}
                            required
                            minLength={4}
                            placeholder="Wpisz hasło"
                            className="register-input"
                        />
                    </div>

                    <div className="field-group">
                        <label htmlFor="confirmPassword">Powtórz hasło:</label>
                        <input
                            id="confirmPassword"
                            type="password"
                            name="confirmPassword"
                            value={form.confirmPassword}
                            onChange={handleChange}
                            required
                            minLength={4}
                            placeholder="Powtórz hasło"
                            className="register-input"
                        />
                    </div>

                    {error && <p className="error-text">{error}</p>}

                    <button
                        type="submit"
                        className="register-button"
                        disabled={loading}
                    >
                        {loading ? 'Przetwarzanie…' : 'Zarejestruj się'}
                    </button>
                </form>

                <div className="register-footer">
                    <p className="footer-text">
                        Masz już konto?{' '}
                        <Link to="/auth/login" className="login-link">
                            Zaloguj się
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
