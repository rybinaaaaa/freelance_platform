package freelanceplatform.data;

import freelanceplatform.model.Task;
import freelanceplatform.model.TaskStatus;
import freelanceplatform.model.TaskType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends CrudRepository<Task, Integer> {

    //TASK BOARD
    /**
     * Finds all tasks with the given status, ordered by the posted date from newest to oldest.
     *
     * @param status the status of the tasks to retrieve
     * @return a list of tasks with the given status, ordered by posted date from newest to oldest
     */
    @Query(value = "select t from Task t where t.status = :status order by t.postedDate desc")
    List<Task> findAllByStatusFromNewest(TaskStatus status);

    /**
     * Finds all tasks with the given status, ordered by the posted date from oldest to newest.
     *
     * @param status the status of the tasks to retrieve
     * @return a list of tasks with the given status, ordered by posted date from oldest to newest
     */
    @Query(value = "select t from Task t where t.status = :status order by t.postedDate")
    List<Task> findAllByStatusFromOldest(TaskStatus status);

    /**
     * Finds all tasks with the given type and status, ordered by the posted date from newest to oldest.
     *
     * @param type   the type of the tasks to retrieve
     * @param status the status of the tasks to retrieve
     * @return a list of tasks with the given type and status, ordered by posted date from newest to oldest
     */
    @Query(value = "select t from Task t where t.type = :type and t.status = :status order by t.postedDate desc")
    List<Task> findAllByTypeAndStatusFromNewest(TaskType type, TaskStatus status);

    /**
     * Finds all tasks with the given type and status, ordered by the posted date from oldest to newest.
     *
     * @param type   the type of the tasks to retrieve
     * @param status the status of the tasks to retrieve
     * @return a list of tasks with the given type and status, ordered by posted date from oldest to newest
     */
    @Query(value = "select t from Task t where t.type = :type and t.status = :status order by t.postedDate")
    List<Task> findAllByTypeAndStatusFromOldest(TaskType type, TaskStatus status);

    //TAKEN TASKS
    /**
     * Finds all tasks assigned to a freelancer that have not yet expired.
     *
     * @param freelancerId the ID of the freelancer
     * @return a list of tasks assigned to the freelancer with deadlines that have not expired
     */
    @Query(value = "select t from Task t where t.freelancer.id = :freelancerId and t.deadline > CURRENT_TIMESTAMP")
    List<Task> findAllTakenByFreelancerIdDeadlineNotExpired(Integer freelancerId);

    /**
     * Finds all tasks assigned to a freelancer that have expired.
     *
     * @param freelancerId the ID of the freelancer
     * @return a list of tasks assigned to the freelancer with deadlines that have expired
     */
    @Query(value = "select t from Task t where t.freelancer.id = :freelancerId and t.deadline < CURRENT_TIMESTAMP")
    List<Task> findAllTakenByFreelancerIdDeadlineExpired(Integer freelancerId);

    /**
     * Finds all tasks assigned to a freelancer with the given status that have not yet expired.
     *
     * @param freelancerId the ID of the freelancer
     * @param taskStatus   the status of the tasks to retrieve
     * @return a list of tasks assigned to the freelancer with the given status and deadlines that have not expired
     */
    @Query(value = "select t from Task t where t.freelancer.id = :freelancerId and t.status = :taskStatus and t.deadline > CURRENT_TIMESTAMP")
    List<Task> findAllTakenByFreelancerIdAndStatusDeadlineNotExpired(Integer freelancerId, TaskStatus taskStatus);

    /**
     * Finds all tasks assigned to a freelancer with the given status that have expired.
     *
     * @param freelancerId the ID of the freelancer
     * @param taskStatus   the status of the tasks to retrieve
     * @return a list of tasks assigned to the freelancer with the given status and deadlines that have expired
     */
    @Query(value = "select t from Task t where t.freelancer.id = :freelancerId and t.status = :taskStatus and t.deadline > CURRENT_TIMESTAMP")
    List<Task> findAllTakenByFreelancerIdAndStatusDeadlineExpired(Integer freelancerId, TaskStatus taskStatus);

    //POSTED TASKS
    /**
     * Finds all tasks posted by a customer that have not yet expired.
     *
     * @param customerId the ID of the customer
     * @return a list of tasks posted by the customer with deadlines that have not expired
     */
    @Query(value = "select t from Task t where t.customer.id = :customerId and t.deadline > CURRENT_TIMESTAMP")
    List<Task> findAllPostedByCustomerIdDeadlineNotExpired(Integer customerId);

    /**
     * Finds all tasks posted by a customer that have expired.
     *
     * @param customerId the ID of the customer
     * @return a list of tasks posted by the customer with deadlines that have expired
     */
    @Query(value = "select t from Task t where t.customer.id = :customerId and t.deadline < CURRENT_TIMESTAMP")
    List<Task> findAllPostedByCustomerIdDeadlineExpired(Integer customerId);

    /**
     * Finds all tasks posted by a customer with the given status that have not yet expired.
     *
     * @param customerId the ID of the customer
     * @param taskStatus the status of the tasks to retrieve
     * @return a list of tasks posted by the customer with the given status and deadlines that have not expired
     */
    @Query(value = "select t from Task t where t.customer.id = :customerId and t.status = :taskStatus and t.deadline > CURRENT_TIMESTAMP")
    List<Task> findAllPostedByCustomerIdAndStatusDeadlineNotExpired(Integer customerId, TaskStatus taskStatus);

    /**
     * Finds all tasks posted by a customer with the given status that have expired.
     *
     * @param customerId the ID of the customer
     * @param taskStatus the status of the tasks to retrieve
     * @return a list of tasks posted by the customer with the given status and deadlines that have expired
     */
    @Query(value = "select t from Task t where t.customer.id = :customerId and t.status = :taskStatus and t.deadline > CURRENT_TIMESTAMP")
    List<Task> findAllPostedByCustomerIdAndStatusDeadlineExpired(Integer customerId, TaskStatus taskStatus);

    /**
     * Finds all tasks.
     *
     * @return a list of all {@link Task} entities
     */
    List<Task> findAll();
}
