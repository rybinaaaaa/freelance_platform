import React, { useState } from 'react';
import './Profile.css';
import { useNavigate } from 'react-router-dom';
import TasksList from '../TaskList/TasksList';
import ProposalsList from '../ProposalsList/ProposalsList';
import PickedOrders from '../PickedOrders/PickedOrders'; 
import Cookies from 'js-cookie';

const ProfilePage = ({ username, rating, role, feedbacks, bio, resumeLink, email, phone, tasks, orders, proposals }) => {
    const navigate = useNavigate();
    const [showTasks, setShowTasks] = useState(false);
    const [showProposals, setShowProposals] = useState(false);
    const [showPickedOrders, setShowPickedOrders] = useState(false);

    const handleToggleTasks = () => {
        setShowTasks(!showTasks);
    };

    const handleToggleProposals = () => {
        setShowProposals(!showProposals);
    };

    const handleTogglePickedOrders = () => {
        setShowPickedOrders(!showPickedOrders);
    };

    const handleAddTask = () => {
        navigate('/task-form');
    };

    const handleEditProfile = () => {
        navigate('/edit-profile');
    };

    const handleLogout = () => {
        Cookies.remove('username');
        Cookies.remove('email');
        Cookies.remove('id');
        Cookies.remove('authToken');
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
                    <h2>Personal data</h2>
                    <p>{username}</p>
                    <h2>Contacts</h2>
                    <p>{email}</p>
                    <p>{phone}</p>
                </div>
                <div className="profile-about">
                    <h2>About me</h2>
                    <p>{bio}</p>
                </div>
                <div className="profile-tasks">
                    <button onClick={handleToggleTasks}>Created Tasks</button>
                    <button onClick={handleTogglePickedOrders}>Picked Orders</button>
                    <button onClick={handleToggleProposals}>Proposals</button>
                    <button onClick={handleAddTask}>Add task</button>
                </div>
                {showTasks && <TasksList />}
                {showPickedOrders && <PickedOrders />}
                {showProposals && <ProposalsList />}
            </div>
        </div>
    );
};

export default ProfilePage;
