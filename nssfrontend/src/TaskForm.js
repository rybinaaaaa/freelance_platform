import React, { useState } from 'react';
import './TaskForm.css'; // Убедитесь, что стили подключены

function TaskForm() {
    const [title, setTitle] = useState('');
    const [problem, setProblem] = useState('');
    const [deadline, setDeadline] = useState('');
    const [payment, setPayment] = useState('');
    const [selectedTypes, setSelectedTypes] = useState([]);
    //const [showDropdown, setShowDropdown] = useState(false);


    const [showDropdown, setShowDropdown] = useState(false);

    const toggleDropdown = () => setShowDropdown(!showDropdown);

    const taskTypes = [
        "TranslationAndLanguageServices",
        "DataEntryAndVirtualAssistance",
        "ConsultingAndBusinessServices",
        "CreativeAndArtisticServices",
        "GraphicDesignAndMultimedia",
        "EngineeringAndArchitecture",
        "WritingAndContentCreation",
        "ProgrammingAndDevelopment",
        "GamingAndVrArDevelopment",
        "TutoringAndEducation",
        "SalesAndMarketing",
        "DigitalMarketing"
    ];

    const handleSubmit = (event) => {
        event.preventDefault();
        const now = new Date().toISOString();
        const taskData = {
            title,
            problem,
            deadline: new Date(deadline).toISOString(),
            payment: parseFloat(payment),
            types: selectedTypes,
            postedDate: now, // Дата создания задачи
            status: 'UNASSIGNED',
            freelancer: 'user123', // Пока что пустое, будет заполняться из бэкенда
            assignedDate: null,
            submittedDate: null
        };

        // Получение текущего массива задач или инициализация пустого массива
        const currentTasks = JSON.parse(localStorage.getItem('tasks')) || [];
        // Добавление новой задачи в массив
        currentTasks.push(taskData);
        // Сохранение обновленного массива задач в localStorage
        localStorage.setItem('tasks', JSON.stringify(currentTasks));

        alert('Task saved!');
        setTitle('');
        setProblem('');
        setDeadline('');
        setPayment('');
        setSelectedTypes([]);
    };


    const handleTypeChange = (type) => {
        setSelectedTypes(prevTypes => {
            if (prevTypes.includes(type)) {
                return prevTypes.filter(t => t !== type);
            } else {
                return [...prevTypes, type];
            }
        });
    };
    return (
        <div className="form-container">
            <form onSubmit={handleSubmit}>
                <label>
                    Title:
                    <input type="text" value={title} onChange={e => setTitle(e.target.value)}/>
                </label>
                <label>
                    Problem Description:
                    <textarea value={problem} onChange={e => setProblem(e.target.value)}/>
                </label>
                <label>
                    Deadline:
                    <input type="datetime-local" value={deadline} onChange={e => setDeadline(e.target.value)}/>
                </label>
                <label>
                    Payment:
                    <input type="number" value={payment} onChange={e => setPayment(e.target.value)}/>
                </label>
                <div className="dropdown-container">
                    <button type="button" onClick={toggleDropdown}>Type</button>
                    <div className={`checkbox-group ${showDropdown ? 'show' : ''}`}>
                        {taskTypes.map((type, index) => (
                            <label key={index} className="checkbox-label">
                                <input
                                    type="checkbox"
                                    checked={selectedTypes.includes(type)}
                                    onChange={() => handleTypeChange(type)}
                                />
                                {type}
                            </label>
                        ))}
                    </div>
                </div>

                <button type="submit">Save Task</button>
            </form>
        </div>
    );
}

export default TaskForm;
