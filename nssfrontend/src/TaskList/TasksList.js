import React, { useState, useEffect } from 'react';
import axios from 'axios';
import ReactPaginate from 'react-paginate';
import { useNavigate } from 'react-router-dom';
import EditTaskModal from '../EditTaskModal/EditTaskModal';
import './TasksList.css';
import { useAuth } from '../contexts/AuthContext';
import Cookies from 'js-cookie'; 

const TasksList = () => {
    const [tasks, setTasks] = useState([]);
    const [currentPage, setCurrentPage] = useState(0);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [isEditing, setIsEditing] = useState(false);
    const [taskIdToEdit, setTaskIdToEdit] = useState(null);

    const navigate = useNavigate();
    const authToken = Cookies.get('authToken'); 
    const tasksPerPage = 4;

    useEffect(() => {
        if (!authToken) {
            console.log("Redirecting to login due to missing auth token");
            setError("You are not authorized. Please login.");
            navigate('/login', {replace: true});
            return;
        }

        fetchTasks();
    }, [authToken, navigate]);

    const fetchTasks = async () => {
        setLoading(true);
        try {
            const response = await axios.get('https://freelance-platform-3-0-2.onrender.com/rest/tasks/posted', {
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
                    postedDate: task.deadline
                }));
                setTasks(detailedTasks);
            } else {
                throw new Error('Failed to fetch tasks');
            }
        } catch (error) {
            setError(error.message);
            console.error('Error while fetching tasks:', error);
        } finally {
            setLoading(false);
        }
    };

    const handlePageClick = (event) => {
        setCurrentPage(Number(event.selected));
    };

    const handleEditClick = (taskId) => {
        setTaskIdToEdit(taskId);
        setIsEditing(true);
    };

    const closeEditModal = () => {
        setIsEditing(false);
        setTaskIdToEdit(null);
    };

    const handleTaskUpdated = async () => {
        setLoading(true);
        try {
            const response = await axios.get('https://freelance-platform-3-0-2.onrender.com/rest/tasks/posted', {
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
                    postedDate: task.deadline
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

    const handleDeleteClick = async (taskId) => {

        try {
            const response = await axios.delete(`https://freelance-platform-3-0-2.onrender.com/rest/tasks/posted/${taskId}`, {
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': authToken
                }
            });

            if (response.status === 204) {
                alert('Task deleted successfully!');
                fetchTasks(); 
            } else {
                alert('Failed to delete task');
            }
        } catch (error) {
            console.error('Error deleting task:', error);
            alert('Error deleting task. See console for details.');
        }
    };

    if (loading) return <div>Loading tasks...</div>;
    if (error) return <div>Error: {error}</div>;

    const indexOfLastTask = (currentPage + 1) * tasksPerPage;
    const indexOfFirstTask = indexOfLastTask - tasksPerPage;
    const currentTasks = tasks.slice(indexOfFirstTask, indexOfLastTask);

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
                            {task.types ? task.types.map((type, idx) => (
                                <span key={idx} className="tag">{type}</span>
                            )) : null}
                        </div>
                        <span className="task-status">{task.status}</span>
                        <button onClick={() => handleEditClick(task.id)}>Edit</button>
                        <button onClick={() => handleDeleteClick(task.id)}>Delete</button>
                    </div>
                ))}
            </div>
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
            {isEditing && (
                <EditTaskModal
                    taskId={taskIdToEdit}
                    onClose={closeEditModal}
                    onTaskUpdated={handleTaskUpdated}
                />
            )}
        </div>
    );
};

export default TasksList;
