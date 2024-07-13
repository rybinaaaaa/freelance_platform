import React, { useState, useEffect } from 'react';
import axios from 'axios';
import Cookies from 'js-cookie';

const EditProfilePage = () => {
  const [firstName, setFirstName] = useState('');
  const [lastName, setLastName] = useState('');
  const [email, setEmail] = useState('');
  const [userId, setUserId] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const savedUsername = Cookies.get('username');
    fetchUserIdByUsername(savedUsername);
  }, []);

  const fetchUserIdByUsername = async (username) => {
    try {
      const response = await axios.get(`http://localhost:8080/rest/users/username/${username}`);
      setUserId(response.data.id);
      setLoading(false);
      fetchUserData(response.data.id);
    } catch (error) {
      console.error('Error fetching user id:', error);
      setLoading(false);
    }
  };

  const fetchUserData = async (userId) => {
    try {
      const response = await axios.get(`http://localhost:8080/rest/users/${userId}`);
      const { firstName, lastName, email } = response.data;
      setFirstName(firstName);
      setLastName(lastName);
      setEmail(email);
    } catch (error) {
      console.error('Error fetching user data:', error);
    }
  };

  const handleSubmit = async (event) => {
    event.preventDefault();

    const formData = {
      id: userId,
      firstName,
      lastName,
      email
    };

    const authToken = Cookies.get('authToken');

    try {
      const response = await axios.put(`http://localhost:8080/rest/users/${userId}`, formData, {
        headers: {
          'Authorization': authToken
        }
      });
      console.log('User updated successfully:', response.data);
      // Добавьте здесь логику для обновления пользовательского интерфейса, например, отображение сообщения об успешном обновлении
    } catch (error) {
      console.error('Error updating user:', error);
      // Обработка ошибок, например, вывод ошибки на экран или перенаправление пользователя
    }
  };

  if (loading) {
    return <div>Loading...</div>;
  }

  return (
    <div className="container">
      <h2>Edit Profile</h2>
      <form onSubmit={handleSubmit}>
        <div className="input-group">
          <label htmlFor="firstName">First Name:</label>
          <input
            type="text"
            id="firstName"
            value={firstName}
            onChange={(e) => setFirstName(e.target.value)}
            required
          />
        </div>
        <div className="input-group">
          <label htmlFor="lastName">Last Name:</label>
          <input
            type="text"
            id="lastName"
            value={lastName}
            onChange={(e) => setLastName(e.target.value)}
            required
          />
        </div>
        <div className="input-group">
          <label htmlFor="email">Email:</label>
          <input
            type="email"
            id="email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            required
          />
        </div>
        <button type="submit">Save Changes</button>
      </form>
    </div>
  );
};

export default EditProfilePage;
