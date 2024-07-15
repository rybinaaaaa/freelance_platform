import React, { useEffect, useState } from 'react';
import axios from 'axios';
import Cookies from 'js-cookie';

const ReceivedProposals = () => {
    const [proposals, setProposals] = useState([]);
    const [selectedFreelancer, setSelectedFreelancer] = useState({});
    const [loading, setLoading] = useState(false);

    useEffect(() => {
        const fetchReceivedProposals = async () => {
            setLoading(true);
            const authToken = Cookies.get('authToken');
            try {
                const tasksResponse = await axios.get('http://localhost:8080/rest/tasks/posted', {
                    headers: { 'Authorization': authToken },
                    params: { expired: false }
                });
                const taskIds = tasksResponse.data.map(task => task.id);

                const proposalsResponse = await axios.get('http://localhost:8080/rest/proposals', {
                    headers: { 'Authorization': authToken }
                });
                const receivedProposals = proposalsResponse.data.filter(p => taskIds.includes(p.taskId));

                // Fetch task titles and freelancer usernames
                const proposalsWithDetails = await Promise.all(receivedProposals.map(async (proposal) => {
                    const [taskData, freelancerData] = await Promise.all([
                        axios.get(`http://localhost:8080/rest/tasks/${proposal.taskId}`, {
                            headers: { 'Authorization': authToken }
                        }),
                        axios.get(`http://localhost:8080/rest/users/${proposal.freelancerId}`, {
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

    const handleSelectFreelancer = (taskId, freelancerId) => {
        console.log(`Selecting freelancer ${freelancerId} for task ${taskId}`);
        setSelectedFreelancer(prev => ({
            ...prev,
            [taskId]: freelancerId  // Update selection state
        }));
    };


    const handleConfirmSelection = async (taskId) => {
        const authToken = Cookies.get('authToken');
        const freelancerId = selectedFreelancer[taskId];

        // Логирование для отладки перед отправкой запроса
        console.log(`Attempting to assign freelancer ${freelancerId} to task ${taskId}`);

        if (!freelancerId) {
            alert("Please select a freelancer before confirming.");
            return;
        }

        try {
            const response = await axios.post(`http://localhost:8080/rest/tasks/posted/${taskId}/proposals/${freelancerId}`, {}, {
                headers: {
                    'Authorization': authToken
                }
            });

            if (response.status === 204) {
                alert(`Freelancer ID ${freelancerId} successfully assigned to task ID ${taskId}.`);
                // Можно здесь добавить дополнительное логирование для подтверждения успешного запроса
                console.log(`Freelancer ID ${freelancerId} was successfully assigned to task ID ${taskId}`);
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
                                    checked={selectedFreelancer[taskId] === proposal.freelancerId}
                                    onChange={() => handleSelectFreelancer(taskId, proposal.freelancerId)}
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
