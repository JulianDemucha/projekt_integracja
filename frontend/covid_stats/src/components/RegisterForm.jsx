import { useState } from 'react'
import axios from 'axios'

export default function RegisterForm() {
    const [form, setForm] = useState({
        username: '',
        password: '',
        confirmPassword: ''
    })
    const [error, setError]     = useState(null)
    const [success, setSuccess] = useState(false)
    const [loading, setLoading] = useState(false)

    const handleChange = e => {
        const { name, value } = e.target
        setForm(prev => ({ ...prev, [name]: value }))
    }

    const handleSubmit = async e => {
        e.preventDefault()
        setError(null)

        if (!form.username || !form.password) {
            setError('Wypełnij wszystkie pola.')
            return
        }
        if (form.password !== form.confirmPassword) {
            setError('Hasła nie są takie same.')
            return
        }

        setLoading(true)
        try {
            const res = await axios.post(
                'http://localhost:8080/users',
                { username: form.username, password: form.password }
            )
            // res.data to: { id, username, password(hashed), role }
            console.log('Zarejestrowano:', res.data)
            setSuccess(true)
            setForm({ username: '', password: '', confirmPassword: '' })
        } catch (err) {
            console.error(err)
            setError(err.response?.data || 'Błąd rejestracji')
        } finally {
            setLoading(false)
        }
    }

    if (success) {
        return <p>Rejestracja zakończona. Możesz się teraz zalogować.</p>
    }

    return (
        <form onSubmit={handleSubmit}>
            <h2>Rejestracja</h2>

            <div>
                <label>
                    Login:
                    <input
                        type="text"
                        name="username"
                        value={form.username}
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
                        value={form.password}
                        onChange={handleChange}
                        required
                        minLength={4}
                    />
                </label>
            </div>

            <div>
                <label>
                    Powtórz hasło:
                    <input
                        type="password"
                        name="confirmPassword"
                        value={form.confirmPassword}
                        onChange={handleChange}
                        required
                        minLength={4}
                    />
                </label>
            </div>

            {error && <p style={{ color: 'red' }}>{error}</p>}

            <button type="submit" disabled={loading}>
                {loading ? 'Rejestruję…' : 'Zarejestruj się'}
            </button>
        </form>
    )
}
