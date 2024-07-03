import React, { useState } from 'react';
import axios from 'axios';
import './Auth2.css';

const LoginPage = () => {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');

    const handleLogin = async (event) => {
        event.preventDefault();
        try {
            const response = await axios.post('http://localhost:8080/login', {
                username,
                password
            });
            console.log('Login successful:', response.data);
        } catch (error) {
            if (error.response) {
                console.error('Login failed:', error.response.data);
            } else {
                console.error('Login failed:', error.message);
            }
        }
    };


    return (
        <div className="container">
            <div className="form-container">
                <h2>Login</h2>
                <form onSubmit={handleLogin}>
                    <div className="input-group">
                        <input type="text" id="login" placeholder="Login" required
                               value={username} onChange={e => setUsername(e.target.value)} />
                    </div>
                    <div className="input-group">
                        <input type="password" id="password" placeholder="Password" required
                               value={password} onChange={e => setPassword(e.target.value)} />
                    </div>
                    <div className="buttons">
                        <a href="#" className="forgot-password">Forgot password</a>
                        <button type="submit" className="login-button">Login</button>
                    </div>
                </form>
            </div>
        </div>
    );
};

export default LoginPage;
