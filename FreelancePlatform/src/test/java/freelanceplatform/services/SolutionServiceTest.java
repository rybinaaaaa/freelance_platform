package freelanceplatform.services;


import freelanceplatform.data.SolutionRepository;
import freelanceplatform.data.TaskRepository;
import freelanceplatform.data.UserRepository;
import freelanceplatform.environment.Generator;
import freelanceplatform.exceptions.NotFoundException;
import freelanceplatform.model.Role;
import freelanceplatform.model.Solution;
import freelanceplatform.model.Task;
import freelanceplatform.utils.IntegrationTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ActiveProfiles("services")
public class SolutionServiceTest extends IntegrationTestBase {

    @Autowired
    private SolutionService solutionService;

    @Autowired
    private SolutionRepository solutionRepo;

    @Autowired
    private UserRepository userRepo;

    private Solution solution;

    private Task task;

    @BeforeEach
    public void setUp() {
        solution = Generator.generateSolution();

        task = Generator.generateTask();
        task.setCustomer(Generator.generateUser());
        task.getCustomer().setRole(Role.USER);

        solution.setTask(task);
        task.setSolution(solution);
        solutionRepo.save(solution);

        userRepo.save(task.getCustomer());
    }

    @Test
    public void getThrowsNotFoundExceptionIfIdIsWrong() {
        assertThrows(NotFoundException.class, () -> solutionService.findById(-1));
    }

    @Test
    public void updateThrowsNotFoundExceptionIfIdIsWrong() {
        solution = Generator.generateSolution();
        solution.setId(-1);
        assertThrows(NotFoundException.class, () -> solutionService.update(solution));
    }

    @Test
    public void deleteRemovesSolutionFromTask() {
        solutionService.deleteById(solution.getId());
        assertNull(task.getSolution());
    }
}
