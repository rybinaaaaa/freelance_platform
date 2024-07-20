import React, { useEffect, useState } from 'react';
import axios from 'axios';
import Cookies from 'js-cookie';

const ReceivedProposals = () => {
    const [proposals, setProposals] = useState([]);
    const [selectedProposal, setSelectedProposal] = useState({});
    const [loading, setLoading] = useState(false);

    useEffect(() => {
        const fetchReceivedProposals = async () => {
            setLoading(true);
            const authToken = Cookies.get('authToken');
            try {
                const tasksResponse = await axios.get('https://freelance-platform-3-0-2.onrender.com/rest/tasks/posted', {
                    headers: { 'Authorization': authToken },
                    params: { expired: false }
                });
                const taskIds = tasksResponse.data.map(task => task.id);

                const proposalsResponse = await axios.get('https://freelance-platform-3-0-2.onrender.com/rest/proposals', {
                    headers: { 'Authorization': authToken }
                });
                const receivedProposals = proposalsResponse.data.filter(p => taskIds.includes(p.taskId));

                // Fetch task titles and freelancer usernames
                const proposalsWithDetails = await Promise.all(receivedProposals.map(async (proposal) => {
                    const [taskData, freelancerData] = await Promise.all([
                        axios.get(`https://freelance-platform-3-0-2.onrender.com/rest/tasks/${proposal.taskId}`, {
                            headers: { 'Authorization': authToken }
                        }),
                        axios.get(`https://freelance-platform-3-0-2.onrender.com/rest/users/${proposal.freelancerId}`, {
                            headers: { 'Authorization': authToken }
                        })
                    ]);

                    return {
                        ...proposal,
                        taskTitle: taskData.data.title,
                        freelancerUsername: freelancerData.data.username
                    };
                }));

                // Group proposals by task
                const taskProposals = taskIds.reduce((acc, taskId) => {
                    acc[taskId] = proposalsWithDetails.filter(p => p.taskId === taskId);
                    return acc;
                }, {});

                setProposals(taskProposals);
            } catch (error) {
                console.error('Error fetching received proposals:', error);
            } finally {
                setLoading(false);
            }
        };

        fetchReceivedProposals();
    }, []);

    const handleSelectFreelancer = (taskId, proposalId) => {
        setSelectedProposal(prev => ({
            ...prev,
            [taskId]: proposalId
        }));
    };

    const handleConfirmSelection = async (taskId) => {
        const authToken = Cookies.get('authToken');
        const proposalId = selectedProposal[taskId];

        if (!proposalId) {
            alert("Please select a proposal before confirming.");
            return;
        }

        try {
            const response = await axios.post(`https://freelance-platform-3-0-2.onrender.com/rest/tasks/posted/${taskId}/proposals/${proposalId}`, {}, {
                headers: {
                    'Authorization': authToken
                }
            });

            if (response.status === 204) {
                alert(`Proposal ID ${proposalId} successfully assigned to task ID ${taskId}.`);
            } else {
                throw new Error('Failed to assign freelancer');
            }
        } catch (error) {
            console.error('Error during freelancer assignment:', error);
            alert(`Error assigning freelancer: ${error.message}`);
        }
    };

    return (
        <div>
            {loading ? <p>Loading...</p> : Object.keys(proposals).map(taskId => (
                <div key={taskId}>
                    <h2>{proposals[taskId][0]?.taskTitle} (Task ID: {taskId})</h2>
                    {proposals[taskId].map(proposal => (
                        <div key={proposal.id}>
                            <label>
                                <input
                                    type="checkbox"
                                    checked={selectedProposal[taskId] === proposal.id}
                                    onChange={() => handleSelectFreelancer(taskId, proposal.id)}
                                />
                                Freelancer: {proposal.freelancerUsername}
                            </label>
                        </div>
                    ))}
                    <button onClick={() => handleConfirmSelection(taskId)}>Confirm Selection</button>
                </div>
            ))}
        </div>
    );
};

export default ReceivedProposals;
