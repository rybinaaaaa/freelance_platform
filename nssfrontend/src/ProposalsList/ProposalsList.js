import React, { useState } from 'react';
import SentProposals from '../SentProposals/SentProposals';
import ReceivedProposals from '../ReceivedProposals/ReceivedProposals';
import './ProposalsList.css';

const ProposalsList = () => {
    const [viewType, setViewType] = useState('sent'); 

    return (
        <div className="proposals-container">
            <div className="proposal-filter-buttons">
                <button onClick={() => setViewType('sent')}
                        className={viewType === 'sent' ? 'active' : ''}>
                    Sent Proposals
                </button>
                <button onClick={() => setViewType('received')}
                        className={viewType === 'received' ? 'active' : ''}>
                    Received Proposals
                </button>
            </div>
            {viewType === 'sent' ? <SentProposals /> : <ReceivedProposals />}
        </div>
    );
};

export default ProposalsList;
