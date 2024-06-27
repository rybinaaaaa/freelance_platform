import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Header from './Header';
import Footer from './Footer';
import Home from './Home';
import Tasks from './Tasks';
import About from './About';
import LoginPage from './LoginPage';
import SignUpPage from './SignUpPage';
import ProfilePage from './ProfilePage'; // Импорт компонента профиля
import TaskForm from './TaskForm';


const App = () => {
    return (
        <Router>
            <Header />
            <Routes>
                <Route path="/" element={<Home />} />
                <Route path="/tasks" element={<Tasks />} />
                <Route path="/about" element={<About />} />
                <Route path="/login" element={<LoginPage />} />
                <Route path="/signup" element={<SignUpPage />} />
                <Route path="/profile" element={
                    <ProfilePage
                        username="Username"
                        rating="4.3"
                        role="Freelancer"
                        feedbacks={() => console.log('Show feedbacks')}
                        bio="I’m a freelance Product Designer, and I primarily work in the tech space, building mobile apps, websites, and web apps for early stage start-ups."
                        resumeLink="#"
                        email="some@gmail.com"
                        phone="705999556"
                        tasks="10"
                        orders="5"
                        proposals="2"
                    />
                } />
                <Route path="/task-form" element={<TaskForm />} />
            </Routes>
            <Footer />
        </Router>
    );
};

export default App;
