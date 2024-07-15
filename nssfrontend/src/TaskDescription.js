import React, { useState, useEffect } from 'react';
import './TaskDescription.css';
import { useParams } from 'react-router-dom';
import Cookies from 'js-cookie';

const TaskDescription = () => {
    const { id } = useParams();
    const [task, setTask] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [isOwner, setIsOwner] = useState(false);
    const [proposals, setProposals] = useState([]);

    const currentUserUsername = Cookies.get('username');
    const authToken = Cookies.get('authToken');
    const userId = Cookies.get('userId');

    useEffect(() => {
        const fetchTaskAndProposals = async () => {
            try {
                const taskResponse = await fetch(`http://localhost:8080/rest/tasks/${id}`);
                if (!taskResponse.ok) {
                    throw new Error('Failed to fetch the task');
                }
                const taskData = await taskResponse.json();
                setTask(taskData);
                setIsOwner(taskData.customerUsername === currentUserUsername);

                const proposalsResponse = await fetch(`http://localhost:8080/rest/proposals`, {
                    headers: {
                        'Authorization': authToken
                    }
                });
                if (!proposalsResponse.ok) {
                    throw new Error('Failed to fetch proposals');
                }
                const proposalsData = await proposalsResponse.json();
                setProposals(proposalsData.filter(p => p.freelancerId.toString() === userId));
            } catch (error) {
                setError(error.message);
            } finally {
                setLoading(false);
            }
        };

        fetchTaskAndProposals();
    }, [id, currentUserUsername, authToken, userId]);

    const sendProposal = async () => {
        if (proposals.some(p => p.taskId.toString() === id)) {
            alert('You have already sent a proposal for this task.');
            return;
        }

        const proposalData = {
            taskId: id,
            freelancerId: userId
        };

        try {
            const response = await fetch(`http://localhost:8080/rest/proposals`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': authToken
                },
                body: JSON.stringify(proposalData)
            });
            if (!response.ok) {
                throw new Error('Failed to send proposal');
            }

            // Попытка чтения JSON только если в ответе есть данные
            let responseData = null;
            if (response.headers.get("content-length") !== "0") {
                responseData = await response.json(); // Только если есть что читать
            }

            alert('Proposal sent successfully!');
            if (responseData && responseData.id) {
                setProposals([...proposals, { ...proposalData, id: responseData.id }]);
            } else {
                // Добавляем без ID, если ответ сервера не содержал данных
                setProposals([...proposals, { ...proposalData }]);
            }
        } catch (error) {
            // Возможно, стоит уточнить текст ошибки, чтобы он был информативнее
            alert('Error sending proposal: ' + (error.message || "No response from server"));
        }


    };

    if (loading) return <div>Loading...</div>;
    if (error) return <div>Error: {error}</div>;

    return (
        <div className="task-details">
            <h1>{task?.title}</h1>
            <p>Type: {task?.type}</p>
            <p>Description: {task?.problem}</p>
            {isOwner ? (
                <p>This is your task.</p>
            ) : (
                <button onClick={sendProposal}>Send Proposal</button>
            )}
        </div>
    );
};

export default TaskDescription;
