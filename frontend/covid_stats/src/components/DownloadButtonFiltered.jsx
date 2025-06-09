import React from 'react'
import { useNavigate } from 'react-router-dom'
import axios from 'axios'
import '../DownloadButtons.css'

export function DownloadButtonFiltered({ baseUrl, params, filename, label }) {
    const loggedIn = !!localStorage.getItem('user')
    const navigate = useNavigate()

    const handleClick = async () => {
        if (!loggedIn) {
            navigate('/auth/login')
            return
        }
        try {
            // Manually construct URL with URLSearchParams (no brackets)
            const url = new URL(baseUrl)
            Object.entries(params || {}).forEach(([key, value]) => {
                if (Array.isArray(value)) {
                    value.forEach(v => url.searchParams.append(key, v))
                } else {
                    url.searchParams.set(key, value)
                }
            })
            const res = await axios.get(url.toString(), { responseType: 'blob' })
            const blob = new Blob([res.data], { type: 'text/csv;charset=utf-8;' })
            const link = document.createElement('a')
            link.href = window.URL.createObjectURL(blob)
            link.setAttribute('download', filename)
            document.body.appendChild(link)
            link.click()
            document.body.removeChild(link)
        } catch (err) {
            console.error('Błąd pobierania CSV:', err)
            alert('Nie udało się pobrać danych. Spróbuj ponownie.')
        }
    }

    return (
        <button
            style={{ width: 200 }}
            onClick={handleClick}
            className={loggedIn ? 'download-button' : 'login-button'}
        >
            {loggedIn ? label : 'Zaloguj się, aby eksportować dane'}
        </button>
    )
}
