package freelanceplatform.services;

import freelanceplatform.data.TaskRepository;
import freelanceplatform.data.UserRepository;
import freelanceplatform.environment.Generator;
import freelanceplatform.exceptions.NotFoundException;
import freelanceplatform.exceptions.ValidationException;
import freelanceplatform.model.*;
import freelanceplatform.utils.CacheableTestBase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("services")
public class TaskServiceTest extends CacheableTestBase {

    @Autowired
    private TaskService taskService;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private TaskRepository taskRepo;

    @Autowired
    CacheManager cacheManager;

    private Task task;

    private User freelancer;

    @BeforeEach
    public void setUp(){
        task = Generator.generateTask();
        freelancer = Generator.generateUser();
        freelancer.setRole(Role.USER);
        task.setFreelancer(freelancer);
        userRepo.save(task.getCustomer());
        userRepo.save(freelancer);
        taskRepo.save(task);
    }


//    @Test
//    public void testCaching() {
//        // First call - should fetch from method
//        Integer idFirstCall = taskService.getById(task.getId()).getId();
//
//        // Second call - should fetch from cache
//        Integer idSecondCall = taskService.getById(task.getId()).getId();
//
//        // Verify method invocation on mock object
//        verify(taskService, times(1)).getById(task.getId()); // Ensure method was called only once
//    }

    @Test
    public void testCaching() {
        taskService.findById(task.getId());

        Assertions.assertTrue(Optional.ofNullable(cacheManager.getCache("tasks").get(task.getId())).isPresent());
        Assertions.assertFalse(Optional.ofNullable(cacheManager.getCache("tasks").get(-1)).isPresent());
    }

    @Test
    public void getThrowsNotFoundExceptionIfIdIsWrong(){
        assertThrows(NotFoundException.class, () -> taskService.findById(-1));
    }

    @Test
    public void findByIdReturnsTaskWithCorrectId(){
        Integer id = taskService.findAll().stream().findAny().map(Task::getId).orElse(null);
        Task task = taskService.findById(id).orElse(null);
        Objects.requireNonNull(task);
        assertEquals(id, task.getId());
    }

    @Test
    public void updateThrowsValidationExceptionIfTaskStatusIsNotUnassigned(){
        task.setStatus(TaskStatus.SUBMITTED);
        taskRepo.save(task);
        assertThrows(ValidationException.class, () -> taskService.update(task));
    }

    @Test
    public void updateThrowsNotFoundExceptionIfTaskDoesNotExist(){
        task = Generator.generateTask();
        task.setId(-1);
        assertThrows(NotFoundException.class, () -> taskService.update(task));
    }

    @Test
    public void deleteRemovesTaskFromFreelancersTakenTasks(){
        task.setStatus(TaskStatus.ASSIGNED);
        task.setFreelancer(freelancer);
        freelancer.addTaskToTaken(task);
        userRepo.save(freelancer);
        taskRepo.save(task);
        assertTrue(freelancer.getTakenTasks().contains(task));
        taskService.deleteById(task.getId());
        assertFalse(freelancer.getTakenTasks().contains(task));
    }

    @Test
    public void deleteRemovesTaskFromCustomersPostedTasks(){
        task.setStatus(TaskStatus.ASSIGNED);
        assertTrue(task.getCustomer().getPostedTasks().contains(task));
        taskService.deleteById(task.getId());
        assertFalse(task.getCustomer().getPostedTasks().contains(task));
    }

    @Test
    public void assignAddsTaskToFreelancersTakenTasks(){
        taskService.assignFreelancer(task, freelancer);
        assertTrue(freelancer.getTakenTasks().contains(task));
    }

    @Test
    public void removeFreelancerRemovesTaskFromFreelancersTakenTasks(){
        task.setFreelancer(freelancer);
        task.setAssignedDate(LocalDateTime.now());
        freelancer.addTaskToTaken(task);
        taskRepo.save(task);
        userRepo.save(freelancer);
        taskService.removeFreelancer(task);
        assertFalse(freelancer.getTakenTasks().contains(task));
    }

    @Override
    protected String getCacheName() {
        return "tasks";
    }
}
