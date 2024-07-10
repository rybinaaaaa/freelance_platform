import React, { useState, useEffect } from 'react';
import axios from 'axios';
import Cookies from 'js-cookie';
import ReactPaginate from 'react-paginate';
import './TasksList.css';

const TasksList = () => {
    const [tasks, setTasks] = useState([]);
    const [currentPage, setCurrentPage] = useState(0);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const tasksPerPage = 4;

    useEffect(() => {
        const fetchTasks = async () => {
            try {
                const authToken = Cookies.get('authToken');

                if (!authToken) {
                    throw new Error('Unauthorized: Auth token is missing');
                }

                const response = await axios.get('http://localhost:8080/rest/tasks/posted', {
                    headers: {
                        'Authorization': authToken
                    },
                    params: {
                        expired: false
                    }
                });

                if (response.status === 200) {
                    const detailedTasks = response.data.map(task => ({
                        ...task,
                        postedDate: task.deadline // Адаптируйте в соответствии с вашим API
                    }));
                    setTasks(detailedTasks);
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

    const indexOfLastTask = (currentPage + 1) * tasksPerPage;
    const indexOfFirstTask = indexOfLastTask - tasksPerPage;
    const currentTasks = tasks.slice(indexOfFirstTask, indexOfLastTask);

    const handlePageClick = (event) => {
        setCurrentPage(Number(event.selected));
    };

    if (loading) return <div>Loading tasks...</div>;
    if (error) return <div>Error: {error}</div>;

    return (
        <div className="tasks-container">
            <h2>My Created Tasks</h2>
            <div className="tasks-grid">
                {currentTasks.map((task, index) => (
                    <div key={index} className="task-card">
                        <h3>{task.title}</h3>
                        <p>Posted {new Date(task.postedDate).toLocaleDateString()}</p>
                        <p>Budget: from {task.payment}</p>
                        <p>{task.problem}</p>
                        <div className="tags">
                            {task.types && task.types.map((type, idx) => (
                                <span key={idx} className="tag">{type}</span>
                            ))}
                        </div>
                        <span className="task-status">{task.status}</span>
                    </div>
                ))}
            </div>
            <div className="pagination-wrapper">
                <ReactPaginate
                    previousLabel={'<'}
                    nextLabel={'>'}
                    pageCount={Math.ceil(tasks.length / tasksPerPage)}
                    onPageChange={handlePageClick}
                    containerClassName={'pagination'}
                    activeClassName={'active'}
                    pageClassName={'page-item'}
                    pageLinkClassName={'page-link'}
                />
            </div>
        </div>
    );
};

export default TasksList;
