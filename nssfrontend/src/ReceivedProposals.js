import React, { useEffect, useState } from 'react';
import axios from 'axios';
import Cookies from 'js-cookie';


const ReceivedProposals = () => {
    const [proposals, setProposals] = useState([]);
    const [loading, setLoading] = useState(false);

    useEffect(() => {
        const fetchReceivedProposals = async () => {
            setLoading(true);
            const authToken = Cookies.get('authToken');
            try {
                // Убедитесь, что передаёте параметр 'expired' в запросе
                const tasksResponse = await axios.get('http://localhost:8080/rest/tasks/posted', {
                    headers: { 'Authorization': authToken },
                    params: { expired: false }  // Указываем, что ищем неистекшие задачи
                });
                const taskIds = tasksResponse.data.map(task => task.id);
                const proposalsResponse = await axios.get('http://localhost:8080/rest/proposals', {
                    headers: { 'Authorization': authToken }
                });
                const receivedProposals = proposalsResponse.data.filter(p => taskIds.includes(p.taskId));
                setProposals(receivedProposals);
            } catch (error) {
                console.error('Error fetching received proposals:', error);
            } finally {
                setLoading(false);
            }
        };

        fetchReceivedProposals();
    }, []);

    return (
        <div>
            {loading ? <p>Loading...</p> : (
                <ul>
                    {proposals.map(proposal => (
                        <li key={proposal.id}>
                            Task Name: {proposal.taskTitle} (Task ID: {proposal.taskId})
                        </li>
                    ))}
                </ul>
            )}
        </div>
    );
};

export default ReceivedProposals;
