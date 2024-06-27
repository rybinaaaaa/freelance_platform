import React, { useEffect, useState } from 'react';
import './Tasks.css'; // Убедитесь, что стили подключены

const Tasks = () => {
    const [tasks, setTasks] = useState([]);

    useEffect(() => {
        const loadedTasks = JSON.parse(localStorage.getItem('tasks')) || [];
        setTasks(loadedTasks);
    }, []);

    return (
        <div className="tasks-container">
            {tasks.map((task, index) => (
                <div key={index} className="task-card">
                    <h3>{task.title}</h3>
                    <p>Posted {new Date(task.postedDate).toLocaleDateString()}</p>
                    <p>Budget: from {task.payment}</p>
                    <p>{task.problem.split(' ').slice(0, 15).join(' ')}...</p>
                    <div className="tags">
                        {task.types.map((type, idx) => (
                            <span key={idx} className="tag">{type}</span>
                        ))}
                    </div>
                </div>
            ))}
        </div>
    );
}

export default Tasks;
