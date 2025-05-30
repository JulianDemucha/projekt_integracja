import { useState } from 'react'
import axios from 'axios'

export default function LoginForm() {
 const [credentials, setCredentials] = useState({
  username: '',
  password: ''
 })
 const [error, setError]   = useState(null)
 const [loading, setLoading] = useState(false)
 const [user, setUser]     = useState(null)

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
       { username: credentials.username, password: credentials.password }
   )

   setUser(res.data)
   console.log('Zalogowano:', res.data)
  } catch (err) {
   console.error(err)
   setError('Nieprawidłowy login lub hasło')
  } finally {
   setLoading(false)
  }
 }

 if (user) {
  return (
      <div>
       <p>Zalogowany jako <strong>{user.username}</strong></p>
       <p>Rola: {user.role}</p>
       <p>ID: {user.id}</p>
      </div>
  )
 }

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
