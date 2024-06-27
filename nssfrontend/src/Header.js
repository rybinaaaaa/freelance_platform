import React from 'react';
import { NavLink, useNavigate } from 'react-router-dom';
import './Header.css'; // Импорт стилей
import logoImage from './img/LogoOneTask.png';
import profileIcon from './img/ProfileIcon.png';

const Header = () => {
    const navigate = useNavigate();

    const handleProfileClick = () => {
        navigate('/profile'); // Путь к странице профиля
    };
    return (
        <div className="navbar">
            <img src={logoImage} alt="Logo" className="logo" />
            <div className="menu">
                <NavLink to="/" className={({ isActive }) => isActive ? 'active' : undefined}>Home</NavLink>
                <NavLink to="/tasks" className={({ isActive }) => isActive ? 'active' : undefined}>Tasks</NavLink>
                <NavLink to="/about" className={({ isActive }) => isActive ? 'active' : undefined}>About Us</NavLink>
            </div>
            <img src={profileIcon} alt="Profile Icon" className="profile-icon" onClick={handleProfileClick}/>
        </div>
    );
};


export default Header;
