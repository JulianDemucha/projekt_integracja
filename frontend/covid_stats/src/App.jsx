import './App.css';
import {
    BrowserRouter as Router,
    Routes,
    Route,
} from 'react-router-dom';
import {AuthProvider} from './context/AuthContext';
import HomePage from './views/HomePage.jsx';
import LoginForm from './views/LoginForm.jsx';
import RegisterForm from './views/RegisterForm.jsx';
import UserMenu from './components/UserMenu.jsx';
import Footer from './components/Footer.jsx';

function AppContent() {
    return (
        <>
            <UserMenu/>

            <Routes>
                <Route path="/" element={<HomePage/>}/>
                <Route path="/auth/login" element={<LoginForm/>}/>
                <Route path="/register" element={<RegisterForm/>}/>
            </Routes>

            <Footer/>
        </>
    );
}

export default function App() {
    return (
        <AuthProvider>
            <Router>
                <AppContent/>
            </Router>
        </AuthProvider>
    );
}
