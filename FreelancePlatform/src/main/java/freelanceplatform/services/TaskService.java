package freelanceplatform.services;


import freelanceplatform.data.TaskRepository;
import freelanceplatform.data.UserRepository;
import freelanceplatform.exceptions.NotFoundException;
import freelanceplatform.exceptions.ValidationException;
import freelanceplatform.model.Task;
import freelanceplatform.model.TaskStatus;
import freelanceplatform.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

@Service
public class TaskService {

    private final TaskRepository taskRepo;
    private final UserRepository userRepo;

    @Autowired
    public TaskService(TaskRepository taskRepo, UserRepository userRepo) {
        this.taskRepo = taskRepo;
        this.userRepo = userRepo;
    }

    @Transactional
    public void save(Task task){
        Objects.requireNonNull(task);
        task.setStatus(TaskStatus.UNASSIGNED);
        //todo добавить логику нотификации
        taskRepo.save(task);
    }

    @Transactional(readOnly = true)
    public Task get(Integer id){
        Objects.requireNonNull(id);
        Optional<Task> task = taskRepo.findById(id);
        if (task.isEmpty()) throw new NotFoundException("Task identified by " + id + " not found.");
        return task.get();
    }

    @Transactional(readOnly = true)
    public Iterable<Task> getAllUnassigned(){
        return taskRepo.findAllByStatus(TaskStatus.UNASSIGNED);
    }

    @Transactional(readOnly = true)
    public Iterable<Task> getAllAssigned(){
        return taskRepo.findAllByStatus(TaskStatus.ASSIGNED);
    }

    @Transactional(readOnly = true)
    public Iterable<Task> getAllSubmitted(){
        return taskRepo.findAllByStatus(TaskStatus.SUBMITTED);
    }

    @Transactional(readOnly = true)
    public Iterable<Task> getAllAccepted(){
        return taskRepo.findAllByStatus(TaskStatus.ACCEPTED);
    }

    public boolean exists(Integer id){
        Objects.requireNonNull(id);
        return taskRepo.existsById(id);
    }

    @Transactional
    public void update(Task task){
        Objects.requireNonNull(task);
        if (exists(task.getId())) {
            if (!task.getStatus().equals(TaskStatus.UNASSIGNED)) throw new ValidationException("Task can be updated only if it is unassigned");
            taskRepo.save(task);
        } else {
            throw new NotFoundException("Task to update identified by " + task.getId() + " not found.");
        }
    }

    @Transactional
    public void delete(Task task){
        Objects.requireNonNull(task);
        if (exists(task.getId())) {
            task.getCustomer().removePostedTask(task);
            task.getFreelancer().removeTakenTask(task);
            task.setStatus(TaskStatus.DELETED);
            userRepo.save(task.getCustomer());
            userRepo.save(task.getFreelancer());
            taskRepo.save(task);
        } else {
            throw new NotFoundException("Task to delete identified by " + task.getId() + " not found.");
        }
    }

    @Transactional
    public void assign(Task task, User freelancer){
        Objects.requireNonNull(task);
        Objects.requireNonNull(freelancer);
        task.setStatus(TaskStatus.ASSIGNED);
        task.setFreelancer(freelancer);
        task.setAssignedDate(LocalDate.now());
        freelancer.addTaskToTaken(task);
        //todo добавить логику нотификации
        taskRepo.save(task);
        userRepo.save(freelancer);
    }

    @Transactional
    public void submit(Task task){
        Objects.requireNonNull(task);
        task.setStatus(TaskStatus.SUBMITTED);
        task.setSubmittedDate(LocalDate.now());
        //todo добавить логику нотификации
        taskRepo.save(task);
    }

    @Transactional
    public void refuse(Task task){
        Objects.requireNonNull(task);
        if (task.getAssignedDate().equals(LocalDate.now())){
            task.setStatus(TaskStatus.UNASSIGNED);
            task.getFreelancer().removeTakenTask(task);
            task.setFreelancer(null);
            task.setAssignedDate(null);
            //todo добавить логику нотификации
            taskRepo.save(task);
            userRepo.save(task.getFreelancer());
        } else {
            throw new ValidationException("Freelancer can not refuse from task after 24h from assignment");
        }
    }

    @Transactional
    public void accept(Task task){
        Objects.requireNonNull(task);
        task.setStatus(TaskStatus.ACCEPTED);
        //todo добавить логику нотификации
        taskRepo.save(task);
    }

    @Transactional
    public void returnWithRevisions(Task task, String revisions){
        Objects.requireNonNull(task);
        Objects.requireNonNull(revisions);
        task.setStatus(TaskStatus.ASSIGNED);
        task.setRevisions(revisions);
        //todo добавить логику нотификации
        taskRepo.save(task);
    }
}
