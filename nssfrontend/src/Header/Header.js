import React, { useEffect, useState } from 'react';
import { NavLink, useNavigate } from 'react-router-dom';
import Cookies from 'js-cookie';
import './Header.css';
import logoImage from '../img/LogoOneTask.png';
import profileIcon from '../img/ProfileIcon.png';

const Header = () => {
    const navigate = useNavigate();
    const [username, setUsername] = useState('');

    useEffect(() => {
        
       const savedUsername = Cookies.get('username');
        if (savedUsername) {
            setUsername(savedUsername);
        } else {
            console.log('No username found in cookies');
            //navigate('/login', { replace: true });
        }
    }, );

    const handleProfileClick = () => {
        navigate('/profile'); 
    };

    return (
        <div className="navbar">
            <img src={logoImage} alt="Logo" className="logo" />
            <div className="menu">
                <NavLink to="/" className={({ isActive }) => isActive ? 'active' : undefined}>Home</NavLink>
                <NavLink to="/tasks" className={({ isActive }) => isActive ? 'active' : undefined}>Tasks</NavLink>
                <NavLink to="/about" className={({ isActive }) => isActive ? 'active' : undefined}>About Us</NavLink>
            </div>
            {username ? (
                <div className="user-info">
                    <span className="username">{username}</span>
                    <img src={profileIcon} alt="Profile Icon" className="profile-icon" onClick={handleProfileClick} />
                </div>
            ) : (
                <div className="user-info">
                    <NavLink to="/login" className="login-link">Login</NavLink>
                </div>
            )}
        </div>
    );
};

export default Header;
