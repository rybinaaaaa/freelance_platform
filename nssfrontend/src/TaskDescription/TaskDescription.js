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
                const taskResponse = await fetch(`https://freelance-platform-3-0-2.onrender.com/rest/tasks/${id}`);
                if (!taskResponse.ok) {
                    throw new Error('Failed to fetch the task');
                }
                const taskData = await taskResponse.json();
                setTask(taskData);
                setIsOwner(taskData.customerUsername === currentUserUsername);

                const proposalsResponse = await fetch(`https://freelance-platform-3-0-2.onrender.com/rest/proposals`, {
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
            const response = await fetch(`https://freelance-platform-3-0-2.onrender.com/rest/proposals`, {
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


            let responseData = null;
            if (response.headers.get("content-length") !== "0") {
                responseData = await response.json(); 
            }

            alert('Proposal sent successfully!');
            if (responseData && responseData.id) {
                setProposals([...proposals, { ...proposalData, id: responseData.id }]);
            } else {
               
                setProposals([...proposals, { ...proposalData }]);
            }
        } catch (error) {
           
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
