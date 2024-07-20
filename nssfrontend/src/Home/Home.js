import React from 'react';
import './Home.css'; 
import { useNavigate } from 'react-router-dom';

const Home = () => {
    const navigate = useNavigate();

    const handleLogin = () => {
        navigate('/login'); 
    };

    const handleSignUp = () => {
        navigate('/signup'); 
    };

    return (
        <div className="home-container">
            <div className="overlay">
                <h1>Schedule meetings effortlessly</h1>
                <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit dolor posuere vel venenatis eu sit massa volutpat massa.</p>
                <div className="buttons">
                    <button className="btn-get-started" onClick={handleSignUp}>Get started</button>
                    <button className="btn-log-in" onClick={handleLogin}>Log in</button>
                </div>
            </div>
        </div>
    );
}

export default Home;
