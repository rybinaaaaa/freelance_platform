import React, { useState } from 'react';
import axios from 'axios';
import Cookies from 'js-cookie';
import './Auth.css';
import sideImage from './img/loginimage.jpeg';
import { Link } from 'react-router-dom';

const SignUpPage = () => {
    const [formData, setFormData] = useState({
        username: '',
        firstName: '',
        lastName: '',
        email: '',
        password: '',
        confirmPassword: ''
    });

    const handleChange = (e) => {
        const { id, value } = e.target;
        setFormData({
            ...formData,
            [id]: value
        });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        if (formData.password !== formData.confirmPassword) {
            alert("Passwords do not match");
            return;
        }

        const userData = {
            username: formData.username,
            firstName: formData.firstName,
            lastName: formData.lastName,
            email: formData.email,
            password: formData.password,
            rating: "0",
            role: "USER"
        };

        console.log('Submitting user data:', userData); // Log the data before sending

        try {
            const response = await fetch('http://localhost:8080/rest/users', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(userData)
            });

            if (response.ok) {
                console.log('Registration successful');
                alert('Registration successful');
                // Automatically log in the user after successful registration
                const token = btoa(`${formData.username}:${formData.password}`);
                const authHeader = `Basic ${token}`;

                const loginResponse = await axios.post('http://localhost:8080/login?', {}, {
                    headers: {
                        'Authorization': authHeader
                    },
                    params: {
                        username: formData.username,
                        password: formData.password,
                    },
                });

                // Save user data and auth token in cookies
                const { username, email, id } = loginResponse.data;
                Cookies.set('username', username, { expires: 7 });
                Cookies.set('email', email, { expires: 7 });
                Cookies.set('id', id, { expires: 7 });
                Cookies.set('authToken', authHeader, { expires: 7 });

                console.log('Login successful:', loginResponse.data);
                console.log('Authorization header:', loginResponse.config.headers.Authorization);
                window.location.reload();
            } else {
                const errorData = await response.json();
                console.error('Registration failed:', errorData);
                alert(`Registration failed: ${errorData.message}`);
            }
        } catch (error) {
            console.error('Error:', error);
            alert('An error occurred during registration.');
        }
    };

    return (
        <div className="main-container">
            <div className="content-container">
                <div className="form-container">
                    <h1>Sign Up</h1>
                    <form onSubmit={handleSubmit}>
                        <div className="input-group">
                            <label htmlFor="username" className="label">Username</label>
                            <input type="text" id="username" placeholder="Enter your username" value={formData.username} onChange={handleChange} required />
                        </div>
                        <div className="input-group">
                            <label htmlFor="firstName" className="label">First Name</label>
                            <input type="text" id="firstName" placeholder="Enter your first name" value={formData.firstName} onChange={handleChange} required />
                        </div>
                        <div className="input-group">
                            <label htmlFor="lastName" className="label">Last Name</label>
                            <input type="text" id="lastName" placeholder="Enter your last name" value={formData.lastName} onChange={handleChange} required />
                        </div>
                        <div className="input-group">
                            <label htmlFor="email" className="label">E-mail</label>
                            <input type="email" id="email" placeholder="Enter your E-mail address" value={formData.email} onChange={handleChange} required />
                        </div>
                        <div className="input-group">
                            <label htmlFor="password" className="label">Enter your password</label>
                            <input type="password" id="password" placeholder="Password" value={formData.password} onChange={handleChange} required />
                        </div>
                        <div className="input-group">
                            <label htmlFor="confirmPassword" className="label">Confirm your password</label>
                            <input type="password" id="confirmPassword" placeholder="Confirm Password" value={formData.confirmPassword} onChange={handleChange} required />
                            <p className="login-prompt">
                                If you have an account you can <Link to="/login">login here</Link>
                            </p>
                        </div>
                        <button type="submit" className="signup-button">Sign Up</button>
                    </form>
                </div>
            </div>
            <div className="image-container">
                <img src={sideImage} alt="Side Image" className="side-image" />
            </div>
        </div>
    );
};

export default SignUpPage;
