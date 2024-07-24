package freelanceplatform.data;

import freelanceplatform.model.Feedback;
import freelanceplatform.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedbackRepository extends CrudRepository<Feedback, Integer> {

    /**
     * Retrieves all {@link Feedback} entities.
     *
     * @return a list of all {@link Feedback} entities
     */
    List<Feedback> findAll();

    /**
     * Finds all {@link Feedback} entities received by a specific user.
     *
     * @param receiver the {@link User} who received the feedback
     * @return a list of {@link Feedback} entities received by the given user
     */
    List<Feedback> findByReceiver(User receiver);

    /**
     * Finds all {@link Feedback} entities sent by a specific user.
     *
     * @param sender the {@link User} who sent the feedback
     * @return a list of {@link Feedback} entities sent by the given user
     */
    List<Feedback> findBySender(User sender);
}
