package freelanceplatform.services;

import freelanceplatform.data.SolutionRepository;
import freelanceplatform.data.TaskRepository;
import freelanceplatform.exceptions.NotFoundException;
import freelanceplatform.model.Solution;
import freelanceplatform.model.Task;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@CacheConfig(cacheNames = {"solutions"})
@Slf4j
public class SolutionService implements IService<Solution, Integer>{

    private final SolutionRepository solutionRepo;
    private final TaskRepository taskRepo;

    @Autowired
    public SolutionService(SolutionRepository solutionRepo, TaskRepository taskRepo) {
        this.solutionRepo = solutionRepo;
        this.taskRepo = taskRepo;
    }

    /**
     * Saves a new solution.
     *
     * @param solution Solution object to be saved.
     */
    @CachePut(key = "#solution.id")
    @Transactional
    public Solution save(Solution solution) {
        Objects.requireNonNull(solution);

        Task task = solution.getTask();
        Objects.requireNonNull(task);

        solution.setTask(taskRepo.findById(task.getId()).orElse(null));

        solutionRepo.save(solution);
        return solution;
    }

    @Transactional(readOnly = true)
    public List<Solution> findAll() {
        log.info("Finding all solutions");
        return solutionRepo.findAll();
    }

    /**
     * Retrieves a solution by its ID.
     *
     * @param id ID of the solution to retrieve.
     * @return Solution object if found.
     * @throws NotFoundException if the solution with the specified ID is not found.
     */
    @Transactional(readOnly = true)
    @Cacheable
    public Optional<Solution> findById(Integer id) {
        log.info("Get task by id {}.", id);
        Objects.requireNonNull(id);
        Optional<Solution> solution = solutionRepo.findById(id);
        if (solution.isEmpty()) throw new NotFoundException("Solution identified by " + id + " not found.");
        return solution;
    }

    /**
     * Retrieves a solution associated with a specific task.
     *
     * @param task Task object to retrieve the associated solution.
     * @return Solution object associated with the task.
     * @throws NotFoundException if no solution is found for the specified task.
     */
    @Transactional(readOnly = true)
    @Cacheable
    public Solution getByTask(Task task) {
        Objects.requireNonNull(task);
        Optional<Solution> solution = solutionRepo.findByTask(task);
        if (solution.isEmpty())
            throw new NotFoundException("Solution identified by task" + task.getId() + " not found.");
        return solution.get();
    }

    /**
     * Checks if a solution with the given ID exists.
     *
     * @param id ID of the solution to check.
     * @return true if a solution with the specified ID exists; false otherwise.
     */
    public boolean exists(Integer id) {
        Objects.requireNonNull(id);
        return solutionRepo.existsById(id);
    }

    /**
     * Updates details of an existing solution.
     *
     * @param solution Updated Solution object.
     * @throws NotFoundException if the solution to update is not found.
     */
    @Transactional
    @CachePut(key = "#solution.id")
    public Solution update(Solution solution) {
        Objects.requireNonNull(solution);
        if (exists(solution.getId())) {
            solutionRepo.save(solution);
            return solution;
        } else {
            throw new NotFoundException("Solution to update identified by " + solution.getId() + " not found.");
        }
    }

    /**
     * Deletes a solution by its ID. This method is transactional and will evict any related caches upon successful execution.
     * It ensures that the task associated with the solution is updated appropriately to remove the reference to the deleted solution.
     *
     * @param id the ID of the solution to be deleted. Must not be null.
     * @return {@code true} if the solution was found and deleted successfully, {@code false} otherwise.
     * @throws NullPointerException if the provided ID is null.
     */
    @Transactional
    @CacheEvict
    public boolean deleteById(Integer id){
        Objects.requireNonNull(id);
        log.info("Deleting task with id {}", id);
        return solutionRepo.findById(id)
                .map(solution -> {
                    Optional.ofNullable(solution.getTask())
                            .ifPresent(task -> task.setSolution(null));
                    solutionRepo.delete(solution);
                    return true;
                }).orElse(false);
    }
}
