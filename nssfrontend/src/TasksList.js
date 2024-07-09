import React, { useState, useEffect } from 'react';
import axios from 'axios';
import Cookies from 'js-cookie';

const TasksList = () => {
    const [tasks, setTasks] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchTasks = async () => {
            try {
                const response = await axios.get('/rest/tasks', {
                    headers: { 'Authorization': Cookies.get('authToken') },
                    params: { createdBy: Cookies.get('username') }
                });
                if (response.status === 200) {
                    setTasks(response.data);
                } else {
                    throw new Error('Failed to fetch tasks');
                }
            } catch (error) {
                setError(error.message);
            } finally {
                setLoading(false);
            }
        };

        fetchTasks();
    }, []);

    if (loading) return <div>Loading tasks...</div>;
    if (error) return <div>Error: {error}</div>;

    return (
        <div>
            <h2>My Created Tasks</h2>
            {tasks.length > 0 ? (
                tasks.map((task, index) => (
                    <div key={index}>
                        <h3>{task.title}</h3>
                        <p>{task.description}</p>
                        <span>{task.status}</span>
                    </div>
                ))
            ) : (
                <p>No tasks found.</p>
            )}
        </div>
    );
};

export default TasksList;
