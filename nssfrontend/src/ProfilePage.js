import React, { useState, useEffect } from 'react';
import './Profile.css';
import { useNavigate } from 'react-router-dom';
import TasksList from './TasksList';
import Cookies from 'js-cookie';
import axios from 'axios';
import ReactPaginate from 'react-paginate';

const ProfilePage = ({ username, rating, role, feedbacks, bio, resumeLink, email, phone, tasks, orders, proposals }) => {
    const navigate = useNavigate();

    const [showTasks, setShowTasks] = useState(false);


    const handleToggleTasks = () => {
        setShowTasks(!showTasks);
    };
    const handleAddTask = () => {
        navigate('/task-form');
    };

    const handleEditProfile = () => {
        navigate('/edit-profile');
    };
    const handleLogout = () => {
        // Очистка кук
        Cookies.remove('username');
        Cookies.remove('email');
        Cookies.remove('id');
        Cookies.remove('authToken');

        // Переадресация на страницу входа
        navigate('/login', { replace: true });
    };

    return (
        <div className="profile-container">
            <div className="profile-header">
                <h1>{username}</h1>
                <div className="profile-subheader">
                    <span className="rating">Rating {rating}</span>
                    <span className="role">{role}</span>
                </div>
                <button onClick={feedbacks}>See feedbacks</button>
                <button onClick={handleEditProfile}>Edit profile</button>
                <button onClick={handleLogout}>Logout</button>
            </div>
            <div className="profile-body">
            <div className="profile-details">
                    <div className="left-section">
                        <h2>Personal data</h2>
                        <p>{username}</p>
                        <h2>Contacts</h2>
                        <p>{email}</p>
                        <p>{phone}</p>
                    </div>
                    <div className="right-section">
                        <h2>Resume</h2>
                        <a href={resumeLink}>Download</a>
                    </div>
                </div>
                <div className="profile-about">
                    <h2>About me</h2>
                    <p>{bio}</p>
                </div>
                <div className="profile-tasks">
                    <button onClick={handleToggleTasks}>Created Tasks</button>
                    <button>Picked Orders</button>
                    <button>Proposals</button>
                    <button onClick={handleAddTask}>Add task</button>
                </div>
            </div>
            {showTasks && <TasksList />}
        </div>
    );
};

export default ProfilePage;
