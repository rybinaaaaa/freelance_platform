import React, { useEffect, useState } from 'react';
import ReactPaginate from 'react-paginate';
import './Tasks.css';
import { useNavigate } from 'react-router-dom';

const Tasks = () => {
    const navigate = useNavigate();
    const [tasks, setTasks] = useState([]); // All tasks
    const [currentPage, setCurrentPage] = useState(0);
    const [loading, setLoading] = useState(true); // Loading state
    const [error, setError] = useState(null); // Error state
    const tasksPerPage = 4;

    useEffect(() => {
        const fetchTasks = async () => {
            try {
                setLoading(true);
                const response = await fetch('https://freelance-platform-3-0-2.onrender.com/rest/tasks/taskBoard?fromNewest=false'); // Fetching tasks from backend
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                const data = await response.json();
                // Transform data to match the desired format
                const transformedData = data.map(task => ({
                    ...task,
                    types: [task.type],
                    postedDate: task.deadline // Assuming 'deadline' is the posted date for this example
                }));
                setTasks(transformedData);
                setLoading(false);
            } catch (error) {
                setError('Failed to load tasks');
                setLoading(false);
            }
        };

        fetchTasks();
    }, []);

    // Calculate current tasks to display
    const indexOfLastTask = (currentPage + 1) * tasksPerPage;
    const indexOfFirstTask = indexOfLastTask - tasksPerPage;
    const currentTasks = tasks.slice(indexOfFirstTask, indexOfLastTask);

    // Handle page change
    const handlePageClick = (event) => {
        setCurrentPage(Number(event.selected));
    };

    const handleSeeMoreClick = (taskId) => {
        navigate(`/TaskDescription/${taskId}`);
    };


    if (loading) {
        return <div>Loading...</div>;
    }

    if (error) {
        return <div>{error}</div>;
    }

    return (
        <div className="tasks-container">
            <h2>All Tasks</h2>
            <div className="tasks-grid">
                {currentTasks.map((task, index) => (  // Use currentTasks here
                    <div key={index} className="task-card">
                        <h3>{task.title}</h3>
                        <p>Posted {new Date(task.postedDate).toLocaleDateString()}</p>
                        <p>Budget: from {task.payment}</p>
                        <p>
                            {task.problem.substring(0, 30)}...
                            <button className="see-more" onClick={() => handleSeeMoreClick(task.id)}>see more</button>
                        </p>
                        <div className="tags">
                            {task.types.map((type, idx) => (
                                <span key={idx} className="tag">{type}</span>
                            ))}
                        </div>
                    </div>
                ))}
            </div>
            <div className="pagination-wrapper"> {/* New container for pagination */}
                <ReactPaginate
                    previousLabel={'<'}
                    nextLabel={'>'}
                    pageCount={Math.ceil(tasks.length / tasksPerPage)}
                    onPageChange={handlePageClick}
                    containerClassName={'pagination'}
                    activeClassName={'active'}
                    pageClassName={'page-item'} // new class for page items
                    pageLinkClassName={'page-link'} // new class for links
                />
            </div>
        </div>
    );
}

export default Tasks;
