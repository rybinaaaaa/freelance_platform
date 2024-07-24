package freelanceplatform.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import freelanceplatform.dto.Mapper;
import freelanceplatform.dto.creation.SolutionCreation;
import freelanceplatform.dto.readUpdate.SolutionReadUpdate;
import freelanceplatform.environment.Generator;
import freelanceplatform.model.Role;
import freelanceplatform.model.Solution;
import freelanceplatform.model.Task;
import freelanceplatform.model.User;
import freelanceplatform.model.security.UserDetails;
import freelanceplatform.services.SolutionService;
import freelanceplatform.services.TaskService;
import freelanceplatform.services.UserService;
import freelanceplatform.utils.IntegrationTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Objects;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class SolutionControllerTests extends IntegrationTestBase {
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final SolutionService solutionService;
    private final UserService userService;
    private final TaskService taskService;
    private final Mapper mapper;
    private User userAdmin;
    private User emptyUser;
    private Task task;

    @Autowired
    public SolutionControllerTests(MockMvc mockMvc, ObjectMapper objectMapper, SolutionService solutionService, UserService userService, TaskService taskService, Mapper mapper) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.solutionService = solutionService;
        this.userService = userService;
        this.taskService = taskService;
        this.mapper = mapper;
    }

    @BeforeEach
    public void init() {
        userAdmin = Generator.generateUser();
        userAdmin.setRole(Role.ADMIN);
        userService.save(userAdmin);

        emptyUser = Generator.generateUser();
        emptyUser.setRole(Role.USER);
        userService.save(emptyUser);

        task = Generator.generateTask();
        task.setCustomer(emptyUser);
        taskService.save(task);
    }

    @Test
    public void saveByUserReturnsStatusCreated() throws Exception {
        Solution solution = Generator.generateSolution();
        Task task = taskService.findById(1).orElse(null);
        Objects.requireNonNull(task);
        solution.setTask(task);

        SolutionCreation solutionCreation = mapper.toSolutionCreation(solution);
        String solutionJson = objectMapper.writeValueAsString(solutionCreation);

        mockMvc.perform(post("/rest/solutions")
                        .with(user(new UserDetails(emptyUser)))
                        .contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8")
                        .content(solutionJson).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    public void saveByAdminReturnsStatusForbidden() throws Exception {
        Solution solution = Generator.generateSolution();
        SolutionCreation solutionCreation = mapper.toSolutionCreation(solution);
        String solutionJson = objectMapper.writeValueAsString(solutionCreation);

        mockMvc.perform(post("/rest/solutions")
                        .with(user(new UserDetails(userAdmin)))
                        .contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8")
                        .content(solutionJson).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    public void saveByGuestReturnsStatusForbidden() throws Exception {
        emptyUser.setRole(Role.GUEST);
        Solution solution = Generator.generateSolution();
        SolutionCreation solutionCreation = mapper.toSolutionCreation(solution);
        String solutionJson = objectMapper.writeValueAsString(solutionCreation);

        mockMvc.perform(post("/rest/solutions")
                        .with(user(new UserDetails(emptyUser)))
                        .contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8")
                        .content(solutionJson).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    public void getByIdReturnsStatusOk() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/rest/solutions/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(1)));
    }

    @Test
    public void getByIdReturnsNotFoundForUnknownId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/rest/solutions/-1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateReturnsNotFoundForWrongId() throws Exception {
        Solution solution = solutionService.findById(1).orElse(null);
        Objects.requireNonNull(solution);
        solution.setDescription("new description");

        SolutionReadUpdate solutionReadUpdate = mapper.toSolutionReadUpdate(solution);
        String solutionJson = objectMapper.writeValueAsString(solutionReadUpdate);

        mockMvc.perform(put("/rest/solutions/-1")
                        .with(user(new UserDetails(emptyUser)))
                        .contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8")
                        .content(solutionJson).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateByUserReturnsStatusNoContent() throws Exception {
        Solution solution = solutionService.findById(1).orElse(null);
        Objects.requireNonNull(solution);
        solution.setTask(task);
        task.setSolution(solution);
        task.setFreelancer(emptyUser);
        taskService.save(task);
        solutionService.save(solution);
        solution.setDescription("new description");

        SolutionReadUpdate solutionReadUpdate = mapper.toSolutionReadUpdate(solution);
        String solutionJson = objectMapper.writeValueAsString(solutionReadUpdate);

        mockMvc.perform(put("/rest/solutions/1")
                        .with(user(new UserDetails(emptyUser)))
                        .contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8")
                        .content(solutionJson).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    public void updateByAdminReturnsStatusForbidden() throws Exception {
        Solution solution = solutionService.findById(1).orElse(null);
        Objects.requireNonNull(solution);
        solution.setDescription("new description");

        SolutionReadUpdate solutionReadUpdate = mapper.toSolutionReadUpdate(solution);
        String solutionJson = objectMapper.writeValueAsString(solutionReadUpdate);

        mockMvc.perform(put("/rest/solutions/1")
                        .with(user(new UserDetails(userAdmin)))
                        .contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8")
                        .content(solutionJson).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    public void updateByGuestReturnsStatusForbidden() throws Exception {
        emptyUser.setRole(Role.GUEST);
        Solution solution = solutionService.findById(1).orElse(null);
        Objects.requireNonNull(solution);
        solution.setDescription("new description");

        SolutionReadUpdate solutionReadUpdate = mapper.toSolutionReadUpdate(solution);
        String solutionJson = objectMapper.writeValueAsString(solutionReadUpdate);

        mockMvc.perform(put("/rest/solutions/1")
                        .with(user(new UserDetails(emptyUser)))
                        .contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8")
                        .content(solutionJson).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    public void deleteReturnsNotFoundForWrongId() throws Exception {
        mockMvc.perform(delete("/rest/solutions/-1")
                        .with(user(new UserDetails(emptyUser))))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteByAdminReturnsStatusForbidden() throws Exception {
        mockMvc.perform(delete("/rest/solutions/1")
                        .with(user(new UserDetails(userAdmin))))
                .andExpect(status().isForbidden());
    }

    @Test
    public void deleteByGuestReturnsStatusForbidden() throws Exception {
        emptyUser.setRole(Role.GUEST);
        mockMvc.perform(delete("/rest/solutions/1")
                        .with(user(new UserDetails(emptyUser))))
                .andExpect(status().isForbidden());
    }

    @Test
    public void deleteByUserReturnsStatusNoContent() throws Exception {
        Solution solution = solutionService.findById(1).orElse(null);
        Objects.requireNonNull(solution);
        task.setFreelancer(emptyUser);
        task.setCustomer(userAdmin);
        task.setSolution(solution);
        taskService.save(task);
        solution.setTask(task);
        solutionService.save(solution);
        mockMvc.perform(delete("/rest/solutions/1")
                        .with(user(new UserDetails(emptyUser))))
                .andExpect(status().isNoContent());
    }
}
