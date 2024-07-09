import React, { useState, useEffect } from 'react';
import axios from 'axios';
import Cookies from 'js-cookie';

const UserProfile = () => {
    const [user, setUser] = useState(null);
    const [formData, setFormData] = useState({
        id: '',
        firstName: '',
        lastName: '',
        email: ''
    });
    const authToken = Cookies.get('authToken');

    useEffect(() => {
        const fetchUser = async () => {
            try {
                const response = await axios.get('http://localhost:8080/rest/users/current', {
                    headers: {
                        'Authorization': authToken
                    }
                });
                setUser(response.data);
                setFormData({
                    id: response.data.id,
                    firstName: response.data.firstName,
                    lastName: response.data.lastName,
                    email: response.data.email
                });
            } catch (error) {
                console.error('Error fetching user:', error);
            }
        };

        fetchUser();
    }, [authToken]);

    useEffect(() => {
        if (user) {
            setFormData({
                id: user.id,
                firstName: user.firstName,
                lastName: user.lastName,
                email: user.email
            });
        }
    }, [user]);

    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setFormData({
            ...formData,
            [name]: value
        });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const response = await axios.put(`http://localhost:8080/rest/users/${formData.id}`, formData, {
                headers: {
                    'Authorization': authToken,
                    'Content-Type': 'application/json'
                }
            });
            if (response.status === 200) {
                // Обновляем данные пользователя после успешного обновления
                setUser(response.data);
                // Обновляем форму после успешного обновления профиля
                setFormData({
                    id: response.data.id,
                    firstName: response.data.firstName,
                    lastName: response.data.lastName,
                    email: response.data.email
                });
                console.log('Profile updated successfully');
            } else {
                console.error('Failed to update user:', response.statusText);
            }
        } catch (error) {
            console.error('Error updating user:', error);
        }
    };

    return (
        <div>
            {user ? (
                <div>
                    <h2>User Profile</h2>
                    <p>Username: {user.username}</p>
                    <form onSubmit={handleSubmit}>
                        <label>
                            First Name:
                            <input type="text" name="firstName" value={formData.firstName} onChange={handleInputChange} />
                        </label>
                        <br />
                        <label>
                            Last Name:
                            <input type="text" name="lastName" value={formData.lastName} onChange={handleInputChange} />
                        </label>
                        <br />
                        <label>
                            Email:
                            <input type="email" name="email" value={formData.email} onChange={handleInputChange} />
                        </label>
                        <br />
                        <button type="submit">Save</button>
                    </form>
                </div>
            ) : (
                <p>Loading user profile...</p>
            )}
        </div>
    );
};

export default UserProfile;
