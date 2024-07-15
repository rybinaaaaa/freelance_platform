import React, { useState, useEffect } from 'react';
import axios from 'axios';
import Cookies from 'js-cookie';
import './ProposalsList.css';

const ProposalsList = () => {
    const [proposals, setProposals] = useState([]);
    const [loading, setLoading] = useState(false);

    useEffect(() => {
        const fetchProposalsAndTasks = async () => {
            setLoading(true);
            try {
                const authHeader = Cookies.get('authToken'); // Получение токена авторизации из cookies
                const response = await axios.get('http://localhost:8080/rest/proposals', {
                    headers: {
                        'Authorization': authHeader
                    }
                });

                const userId = Cookies.get('userId'); // Получаем userId из cookies
                const filteredProposals = response.data.filter(p => p.freelancerId.toString() === userId);

                // Для каждого предложения загрузим данные задачи
                const proposalsWithTaskTitles = await Promise.all(filteredProposals.map(async (proposal) => {
                    const taskResponse = await fetch(`http://localhost:8080/rest/tasks/${proposal.taskId}`);
                    const taskData = await taskResponse.json();
                    return { ...proposal, taskTitle: taskData.title };
                }));

                setProposals(proposalsWithTaskTitles);
            } catch (error) {
                console.error('Error fetching proposals and tasks:', error);
            } finally {
                setLoading(false);
            }
        };

        fetchProposalsAndTasks();
    }, []);

    return (
        <div className="proposals-container">
            <div className="proposal-filter-buttons">
                <button className="active">Sent Proposals</button>
            </div>
            <h2>Sent Proposals</h2>
            {loading ? <p>Loading...</p> : (
                <ul>
                    {proposals.map(proposal => (
                        <li key={proposal.id}>
                            Task Name: {proposal.taskTitle}
                        </li>
                    ))}
                </ul>
            )}
        </div>
    );
};

export default ProposalsList;
