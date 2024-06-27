import React from 'react';
import './Profile.css';  // Путь к CSS файлу стилей
import { useNavigate } from 'react-router-dom';


const ProfilePage = ({ username, rating, role, feedbacks, bio, resumeLink, email, phone, tasks, orders, proposals }) => {
    const navigate = useNavigate();

    const handleAddTask = () => {
        navigate('/task-form'); // Путь к странице создания задач
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
                <button>Edit profile</button>
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
                    <button>Created Tasks</button>
                    <button>Picked Orders</button>
                    <button>Proposals</button>
                    <button onClick={handleAddTask}>Add task</button>
                </div>

            </div>
        </div>
    );
};

export default ProfilePage;
