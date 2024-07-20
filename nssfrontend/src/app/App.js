import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import Header from '../Header/Header';
import Footer from '../Footer/Footer';
import Home from '../Home/Home';
import Tasks from '../Tasks/Tasks';
import About from '../About/About';
import LoginPage from '../LoginPage/LoginPage';
import SignUpPage from '../SignUpPage/SignUpPage';
import ProfilePage from '../Profile/ProfilePage'; 
import ProposalsList from '../ProposalsList/ProposalsList';
import SentProposals from '../SentProposals/SentProposals'; 
import ReceivedProposals from '../ReceivedProposals/ReceivedProposals';
import TaskForm from '../TaskForm/TaskForm';
import TasksList from '../TaskList/TasksList';
import TaskDescription from '../TaskDescription/TaskDescription';
import EditProfilePage from '../EditProfilePage/EditProfilePage';
import { AuthProvider, useAuth } from '../contexts/AuthContext';


const PrivateRoute = ({ children }) => {
    const { authToken } = useAuth();
    return authToken ? children : <Navigate to="/login" replace />;
};

const App = () => {
    return (
        <Router>
            <AuthProvider> {}
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
