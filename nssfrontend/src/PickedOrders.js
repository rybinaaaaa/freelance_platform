import React, { useEffect, useState } from 'react';
import axios from 'axios';
import Cookies from 'js-cookie';

const PickedOrders = () => {
    const [tasks, setTasks] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchPickedTasks = async () => {
            setLoading(true);
            const authToken = Cookies.get('authToken');
            const userId = Cookies.get('userId'); // Получаем ID пользователя из куки

            try {
                const response = await axios.get(`http://localhost:8080/rest/tasks/taken`, {
                    headers: { 'Authorization': authToken },
                    params: {
                        taskStatus: 'ASSIGNED', // Указываем статус задач
                        expired: false, // Исключаем истекшие задачи
                        freelancerId: userId  // Параметр не поддерживается бэкендом, указан для примера
                    }
                });

                if (response.status === 200) {
                    setTasks(response.data);
                } else {
                    throw new Error('Failed to fetch picked tasks');
                }
            } catch (error) {
                setError(error.message);
                console.error('Error fetching picked tasks:', error);
            } finally {
                setLoading(false);
            }
        };

        fetchPickedTasks();
    }, []);

    if (loading) return <div>Loading...</div>;
    if (error) return <div>Error: {error}</div>;

    return (
        <div>
            <h2>Picked Orders</h2>
            {tasks.length > 0 ? (
                <ul>
                    {tasks.map(task => (
                        <li key={task.id}>
                            <h3>{task.title}</h3>
                            <p>Description: {task.problem}</p>
                            <p>Deadline: {new Date(task.deadline).toLocaleDateString()}</p>
                        </li>
                    ))}
                </ul>
            ) : (
                <p>No picked tasks found.</p>
            )}
        </div>
    );
};

export default PickedOrders;
