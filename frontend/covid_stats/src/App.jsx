import {useEffect, useState} from 'react'
import './App.css'
import axios from "axios";

function App() {
    const [users, setUsers] = useState([]);
    useEffect(() => {
        axios.get('http://localhost:8080/users')
            .then(res => setUsers(res.data))
            .catch(err => console.error('Błąd:', err));

    }, []);
    console.log(users);
    return (
        <ul>
            {users.map(user => (
                <li key={user.id}>
                    <strong>{user.username}</strong> — {user.role}
                </li>
            ))}
        </ul>
    );
}
    export default App

