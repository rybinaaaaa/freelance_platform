package freelanceplatform.data;

import freelanceplatform.model.Solution;
import freelanceplatform.model.Task;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SolutionRepository extends CrudRepository<Solution, Integer> {

    /**
     * Finds a {@link Solution} by its associated {@link Task}.
     *
     * @param task the task associated with the solution
     * @return an {@link Optional} containing the solution if found, or an empty {@link Optional} if no solution is found for the given task
     */
    Optional<Solution> findByTask(Task task);

    /**
     * Retrieves all {@link Solution} entities.
     *
     * @return a list of all {@link Solution} entities
     */
    List<Solution> findAll();

}
