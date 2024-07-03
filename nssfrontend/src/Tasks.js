import React, { useEffect, useState } from 'react';
import ReactPaginate from 'react-paginate';
import './Tasks.css';
import { useNavigate } from 'react-router-dom';

const Tasks = () => {
    const navigate = useNavigate();
    const [tasks, setTasks] = useState([]); // Все задачи
    const [currentPage, setCurrentPage] = useState(0);
    const tasksPerPage = 4;

    useEffect(() => {
        // Загрузите задачи из localStorage или другого источника
        const loadedTasks = JSON.parse(localStorage.getItem('tasks')) || [];
        setTasks(loadedTasks);
    }, []);

    // Вычислите текущие задачи для отображения
    const indexOfLastTask = (currentPage + 1) * tasksPerPage;
    const indexOfFirstTask = indexOfLastTask - tasksPerPage;
    const currentTasks = tasks.slice(indexOfFirstTask, indexOfLastTask);

    // Обработка изменения страницы
    const handlePageClick = (event) => {
        setCurrentPage(Number(event.selected));
    };
    const handleSeeMoreClick = () => {
        navigate('/TaskDescription'); // Простой переход на страницу TaskDescription
    };

    return (
        <div className="tasks-container">
            {currentTasks.map((task, index) => (  // Используйте currentTasks здесь
                <div key={index} className="task-card">
                    <h3>{task.title}</h3>
                    <p>Posted {new Date(task.postedDate).toLocaleDateString()}</p>
                    <p>Budget: from {task.payment}</p>
                    <p>
                        {task.problem.substring(0, 30)}...
                        <button className="see-more" onClick={handleSeeMoreClick}>see more</button>
                    </p>
                    <div className="tags">
                        {task.types.map((type, idx) => (
                            <span key={idx} className="tag">{type}</span>
                        ))}
                    </div>
                </div>
            ))}
            <div className="pagination-wrapper"> {/* Новый контейнер для пагинации */}
                <ReactPaginate
                    previousLabel={'<'}
                    nextLabel={'>'}
                    pageCount={Math.ceil(tasks.length / tasksPerPage)}
                    onPageChange={handlePageClick}
                    containerClassName={'pagination'}
                    activeClassName={'active'}
                    pageClassName={'page-item'} // новый класс для элементов страниц
                    pageLinkClassName={'page-link'} // новый класс для ссылок
                />
            </div>

        </div>
    );
}

export default Tasks;

