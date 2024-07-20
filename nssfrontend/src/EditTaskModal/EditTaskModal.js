import React, { useState } from 'react';
import axios from 'axios';
import Cookies from 'js-cookie';
import './EditTaskModal.css'; 
const EditTaskModal = ({ taskId, onClose, onTaskUpdated }) => {
    const [title, setTitle] = useState('');
    const [problem, setProblem] = useState('');
    const [deadline, setDeadline] = useState('');
    const [payment, setPayment] = useState('');
    const [selectedType, setSelectedType] = useState('');

    const handleSubmit = async (event) => {
        event.preventDefault();
        const updatedTask = {
            title,
            problem,
            deadline,
            payment,
            type: selectedType
        };

        console.log('Updated Task:', updatedTask); 

        const authToken = Cookies.get('authToken');

        try {
            const response = await axios.put(`https://freelance-platform-3-0-2.onrender.com/rest/tasks/posted/${taskId}`, updatedTask, {
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': authToken
                }
            });

            if (response.status === 204) {
                alert('Task updated successfully!');
                onTaskUpdated();
                onClose();
            } else {
                alert('Failed to update task');
            }
        } catch (error) {
            console.error('Error updating task:', error);
            alert('Error updating task. See console for details.');
        }
    };

    return (
        <div className="modal">
            <div className="modal-content">
                <span className="close" onClick={onClose}>&times;</span>
                <form onSubmit={handleSubmit}>
                    <label>
                        Title:
                        <input type="text" value={title} onChange={e => setTitle(e.target.value)} />
                    </label>
                    <label>
                        Problem Description:
                        <textarea value={problem} onChange={e => setProblem(e.target.value)} />
                    </label>
                    <label>
                        Deadline:
                        <input type="datetime-local" value={deadline} onChange={e => setDeadline(e.target.value)} />
                    </label>
                    <label>
                        Payment:
                        <input type="number" value={payment} onChange={e => setPayment(e.target.value)} />
                    </label>
                    <label>
                        Type:
                        <div className="radio-group">
                            {[
                                'TranslationAndLanguageServices',
                                'DataEntryAndVirtualAssistance',
                                'ConsultingAndBusinessServices',
                                'CreativeAndArtisticServices',
                                'GraphicDesignAndMultimedia',
                                'EngineeringAndArchitecture',
                                'WritingAndContentCreation',
                                'ProgrammingAndDevelopment',
                                'GamingAndVrArDevelopment',
                                'TutoringAndEducation',
                                'SalesAndMarketing',
                                'DigitalMarketing'
                            ].map((type, index) => (
                                <label key={index} className="radio-label">
                                    <input
                                        type="radio"
                                        value={type}
                                        checked={selectedType === type}
                                        onChange={() => setSelectedType(type)}
                                    />
                                    {type}
                                </label>
                            ))}
                        </div>
                    </label>
                    <button type="submit">Save Task</button>
                </form>
            </div>
        </div>
    );
};

export default EditTaskModal;
