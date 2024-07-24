package freelanceplatform.data;

import freelanceplatform.model.Resume;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ResumeRepository extends CrudRepository<Resume, Integer> {

    /**
     * Finds a {@link Resume} by the user ID.
     *
     * @param id the ID of the user associated with the resume
     * @return an {@link Optional} containing the resume if found, or an empty {@link Optional} if no resume is found for the given user ID
     */
    Optional<Resume> findByUserId(Integer id);
}
