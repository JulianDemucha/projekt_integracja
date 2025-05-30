import './App.css'
import {Link, Route, Routes} from "react-router-dom";
import LoginForm from "./components/LoginForm.jsx";
import RegisterForm from "./components/RegisterForm.jsx";
import { BrowserRouter as Router } from 'react-router-dom'
import HomePage from "./components/HomePage.jsx";
function App() {

    return (
        <>
            <Router>
                <nav >
                    <Link to="/">Home </Link>
                    <Link to="/register"> Rejestracja </Link>
                    <Link to="/auth/login"> Logowanie</Link>
                </nav>

                <Routes>
                    <Route path="/" element={<HomePage />} />
                    <Route path="/register" element={<RegisterForm />} />
                    <Route path="/auth/login" element={<LoginForm />} />
                </Routes>
            </Router>
        </>
    );
}
    export default App

