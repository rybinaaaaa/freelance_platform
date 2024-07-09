import React, { useState, useEffect } from 'react';
import './TaskDescription.css';
import { useParams } from 'react-router-dom';

const TaskDescription = () => {
    const { id } = useParams();
    const [task, setTask] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchTask = async () => {
            try {
                const response = await fetch(`http://localhost:8080/rest/tasks/${id}`);
                if (!response.ok) {
                    throw new Error('Failed to fetch the task');
                }
                const data = await response.json();
                setTask(data);
            } catch (error) {
                setError(error.message);
            } finally {
                setLoading(false);
            }
        };

        fetchTask();
    }, [id]);

    if (loading) return <div>Loading...</div>;
    if (error) return <div>Error: {error}</div>;

    return (
        <div className="task-details">
            <h1>{task?.title}</h1>
            <p>Type: {task?.type}</p>
            <p>Description: {task?.problem}</p>
        </div>
    );
};

export default TaskDescription;
