import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import Header from './Header';
import Footer from './Footer';
import Home from './Home';
import Tasks from './Tasks';
import About from './About';
import LoginPage from './LoginPage';
import SignUpPage from './SignUpPage';
import ProfilePage from './ProfilePage'; // Импорт компонента профиля
import ProposalsList from './ProposalsList';

import TaskForm from './TaskForm';
import TasksList from './TasksList';
import TaskDescription from './TaskDescription';
import EditProfilePage from './EditProfilePage';
import { AuthProvider, useAuth } from './contexts/AuthContext';

// Компонент для защищенного роута
const PrivateRoute = ({ children }) => {
    const { authToken } = useAuth();
    return authToken ? children : <Navigate to="/login" replace />;
};

const App = () => {
    return (
        <Router>
            <AuthProvider> {/* Перемещение AuthProvider на более высокий уровень */}
                <Header />
                <Routes>
                    <Route path="/" element={<Home />} />
                    <Route path="/tasks" element={<Tasks />} />
                    <Route path="/about" element={<About />} />
                    <Route path="/login" element={<LoginPage />} />
                    <Route path="/signup" element={<SignUpPage />} />
                    <Route path="/edit-profile" element={<EditProfilePage />} />
                    <Route path="/profile" element={<PrivateRoute><ProfilePage /></PrivateRoute>} />
                    <Route path="/task-form" element={<TaskForm />} />
                    <Route path="/TaskDescription/:id" element={<TaskDescription />} />
                    <Route path="/tasks-list" element={<TasksList />} />
                    <Route path="/proposals-list" element={<PrivateRoute><ProposalsList /></PrivateRoute>} />
                </Routes>
                <Footer />
            </AuthProvider>
        </Router>
    );
};

export default App;
