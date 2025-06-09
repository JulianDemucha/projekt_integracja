import React from 'react'
import { useNavigate } from 'react-router-dom'
import axios from 'axios'
import '../DownloadButtons.css'

export function DownloadButton({ url, baseUrl, params, filename, label }) {
    const loggedIn = !!localStorage.getItem('user')
    const navigate = useNavigate()

    const handleClick = () => {
        if (!loggedIn) {
            navigate('/auth/login')
            return
        }
        const config = { responseType: 'blob' }
        if (params) config.params = params

        axios
            .get(baseUrl || url, config)
            .then((res) => {
                const blob = new Blob([res.data], { type: 'text/csv;charset=utf-8;' })
                const link = document.createElement('a')
                link.href = window.URL.createObjectURL(blob)
                link.setAttribute('download', filename)
                document.body.appendChild(link)
                link.click()
                document.body.removeChild(link)
            })
            .catch((err) => console.error('Błąd pobierania CSV:', err))
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
