package freelanceplatform.data;


import freelanceplatform.model.Proposal;
import freelanceplatform.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface UserRepository extends CrudRepository<User, Integer> {

    /**
     * Checks if a user exists with the given username.
     *
     * @param username the username to check
     * @return {@code true} if a user with the given username exists; {@code false} otherwise
     */
    boolean existsByUsername(String username);

    /**
     * Checks if a user exists with the given email.
     *
     * @param email the email to check
     * @return {@code true} if a user with the given email exists; {@code false} otherwise
     */
    boolean existsByEmail(String email);

    /**
     * Retrieves a user by their username.
     *
     * @param username the username of the user to retrieve
     * @return an {@link Optional} containing the user if found, or an empty {@link Optional} if not found
     */
    Optional<User> getByUsername(String username);

    /**
     * Retrieves all users.
     *
     * @return a list of all {@link User} entities
     */
    List<User> findAll();
}
