package freelanceplatform.data;

import freelanceplatform.model.Proposal;
import freelanceplatform.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProposalRepository extends CrudRepository<Proposal, Integer> {

    /**
     * Retrieves all {@link Proposal} entities.
     *
     * @return a list of all {@link Proposal} entities
     */
    List<Proposal> findAll();

    /**
     * Finds all {@link Proposal} entities associated with a specific freelancer.
     *
     * @param freelancer the {@link User} representing the freelancer whose proposals are to be retrieved
     * @return a list of {@link Proposal} entities associated with the given freelancer
     */
    List<Proposal> findByFreelancer(User freelancer);
}
