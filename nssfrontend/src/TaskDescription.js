import React, { useState, useEffect } from 'react';
import './TaskDescription.css';
import { useParams } from 'react-router-dom';
import Cookies from 'js-cookie';

const TaskDescription = () => {
    const { id } = useParams();
    const [task, setTask] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [isOwner, setIsOwner] = useState(false);

    const currentUserUsername = Cookies.get('username'); // Предполагаем, что имя пользователя хранится в cookies

    useEffect(() => {
        const fetchTask = async () => {
            try {
                const response = await fetch(`http://localhost:8080/rest/tasks/${id}`);
                if (!response.ok) {
                    throw new Error('Failed to fetch the task');
                }
                const data = await response.json();
                setTask(data);
                setIsOwner(data.customerUsername === currentUserUsername);
            } catch (error) {
                setError(error.message);
            } finally {
                setLoading(false);
            }
        };

        fetchTask();
    }, [id, currentUserUsername]);

    const sendProposal = async () => {
        const authToken = Cookies.get('authToken');
        if (!authToken) {
            alert('No authentication token found. Please login.');
            return;
        }

        const proposalData = {
            taskId: id  // Убедитесь, что id задачи корректно передается в эту функцию
        };

        try {
            const response = await fetch('http://localhost:8080/rest/proposals', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': authToken  // Токен уже должен содержать префикс 'Basic '
                },
                body: JSON.stringify(proposalData)
            });

            if (!response.ok) {
                const errorText = await response.text();
                throw new Error('Failed to send proposal: ' + errorText);
            }

            const location = response.headers.get('Location');
            console.log('Proposal created at: ', location);
            alert('Proposal sent successfully!');
        } catch (error) {
            console.error('Error sending proposal:', error);
            alert('Failed to send proposal. See console for details.');
        }
    };






    if (loading) return <div>Loading...</div>;
    if (error) return <div>Error: {error}</div>;

    return (
        <div className="task-details">
            <h1>{task?.title}</h1>
            <p>Type: {task?.type}</p>
            <p>Description: {task?.problem}</p>
            {isOwner ? (
                <p>This is your task.</p>
            ) : (
                <button onClick={sendProposal}>Send Proposal</button>
            )}
        </div>
    );
};

export default TaskDescription;
