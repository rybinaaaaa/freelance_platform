package freelanceplatform.services;

import java.util.List;
import java.util.Optional;

public interface IService<E, K> {

    List<E> findAll();

    Optional<E> findById(K id);

    E update(E entityToUpdate);

    boolean deleteById(K id);
}
