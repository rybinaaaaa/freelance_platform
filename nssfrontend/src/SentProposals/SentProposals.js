import React, { useEffect, useState } from 'react';
import axios from 'axios';
import Cookies from 'js-cookie';

const SentProposals = () => {
    const [proposals, setProposals] = useState([]);
    const [loading, setLoading] = useState(false);

    useEffect(() => {
        const fetchSentProposals = async () => {
            setLoading(true);
            const authToken = Cookies.get('authToken');
            try {
                const response = await axios.get('https://freelance-platform-3-0-2.onrender.com/rest/proposals', {
                    headers: { 'Authorization': authToken }
                });
                const userId = Cookies.get('userId');
                const filteredProposals = response.data.filter(p => p.freelancerId.toString() === userId);

            
                const proposalsWithTaskTitles = await Promise.all(filteredProposals.map(async (proposal) => {
                    const taskResponse = await axios.get(`https://freelance-platform-3-0-2.onrender.com/rest/tasks/${proposal.taskId}`, {
                        headers: { 'Authorization': authToken }
                    });
                    const taskData = await taskResponse.data;
                    return { ...proposal, taskTitle: taskData.title }; 
                }));

                setProposals(proposalsWithTaskTitles);
            } catch (error) {
                console.error('Error fetching sent proposals:', error);
            } finally {
                setLoading(false);
            }
        };

        fetchSentProposals();
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

export default SentProposals;
