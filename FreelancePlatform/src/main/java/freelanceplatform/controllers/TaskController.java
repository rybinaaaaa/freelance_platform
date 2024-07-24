package freelanceplatform.controllers;


import freelanceplatform.dto.Mapper;
import freelanceplatform.dto.creation.TaskCreation;
import freelanceplatform.dto.readUpdate.TaskReadUpdate;
import freelanceplatform.model.*;
import freelanceplatform.model.security.UserDetails;
import freelanceplatform.services.TaskService;
import freelanceplatform.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


@Slf4j
@RestController
@RequestMapping("/rest/tasks")
@PreAuthorize("permitAll()")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;
    private final UserService userService;
    private final Mapper mapper;

    /**
     * Saves a new task based on the provided TaskCreationDTO.
     *
     * @param taskDTO the TaskCreationDTO object containing the task details
     * @param auth    the Authentication object for the current user
     * @return ResponseEntity with the URI of the newly created task
     */
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> save(@RequestBody TaskCreation taskDTO, Authentication auth) {
        final User user = ((UserDetails) auth.getPrincipal()).getUser();
        final Task task = mapper.toTask(taskDTO);
        task.setCustomer(user);
        taskService.save(task);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(task.getId()).toUri();
        return ResponseEntity.created(location).build();
    }

    /**
     * Retrieves a task by its ID.
     *
     * @param id the ID of the task
     * @return ResponseEntity with the task data or 404 if not found
     */
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TaskReadUpdate> getById(@PathVariable Integer id) {
        return taskService.findById(id)
                .map(task -> ResponseEntity.ok(mapper.toTaskReadUpdate(task))).orElse(ResponseEntity.notFound().build());
    }

    /**
     * Retrieves all tasks based on specified filters.
     *
     * @param fromNewest Whether to sort tasks from newest to oldest.
     * @param type       Optional parameter to filter tasks by type.
     * @return ResponseEntity containing a list of TaskDTOs.
     */
    @GetMapping(value = "/taskBoard", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Iterable<TaskReadUpdate>> getAllTaskBoard(@RequestParam boolean fromNewest,
                                                                    @RequestParam(required = false) TaskType type) {
        List<Task> tasks = Optional.ofNullable(type)
                .map(t -> taskService.findAllTaskBoardByTypeAndPostedDate(t, fromNewest))
                .orElseGet(() -> taskService.findAllTaskBoardByPostedDate(fromNewest));
        List<TaskReadUpdate> taskReadUpdates = tasks.stream().map(mapper::toTaskReadUpdate).toList();
        return ResponseEntity.ok(taskReadUpdates);
    }

    /**
     * Retrieves all tasks taken by the authenticated user based on status and expiration.
     *
     * @param taskStatus Optional parameter to filter tasks by status.
     * @param expired    Whether to include expired tasks.
     * @param auth       Authentication object containing user details.
     * @return ResponseEntity containing a list of TaskDTOs.
     */
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping(value = "/taken", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Iterable<TaskReadUpdate>> getAllTakenByTaskStatusAndExpiredStatus(@RequestParam(required = false) TaskStatus taskStatus,
                                                                                            @RequestParam boolean expired, Authentication auth) {
        User user = ((UserDetails) auth.getPrincipal()).getUser();
        List<Task> tasks = Optional.ofNullable(taskStatus)
                .map(t -> taskService.findAllTakenByUserIdAndStatusAndDeadlineStatus(user.getId(), t, expired))
                .orElseGet(() -> taskService.findAllTakenByUserIdAndDeadlineStatus(user.getId(), expired));

        List<TaskReadUpdate> taskReadUpdates = tasks.stream().map(mapper::toTaskReadUpdate).toList();
        return ResponseEntity.ok(taskReadUpdates);
    }

    /**
     * Retrieves all tasks posted by the authenticated user based on status and expiration.
     *
     * @param taskStatus Optional parameter to filter tasks by status.
     * @param expired    Whether to include expired tasks.
     * @param auth       Authentication object containing user details.
     * @return ResponseEntity containing a list of TaskDTOs.
     */
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping(value = "/posted", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Iterable<TaskReadUpdate>> getAllPostedByTaskStatusAndExpiredStatus(@RequestParam(required = false) TaskStatus taskStatus, @RequestParam boolean expired, Authentication auth) {
        User user = ((UserDetails) auth.getPrincipal()).getUser();

        List<Task> tasks = Optional.ofNullable(taskStatus)
                .map(t -> taskService.findAllPostedByUserIdAndStatusAndExpiredStatus(user.getId(), t, expired))
                .orElseGet(() -> taskService.findAllPostedByUserIdAndStatusAndExpiredStatus(user.getId(), taskStatus, expired));
        List<TaskReadUpdate> taskReadUpdates = tasks.stream().map(mapper::toTaskReadUpdate).toList();
        return ResponseEntity.ok(taskReadUpdates);
    }

    /**
     * Updates details of a posted task.
     *
     * @param id             ID of the task to update.
     * @param updatedTaskReadUpdate Updated details of the task.
     * @return ResponseEntity indicating success or failure of the update operation.
     */
    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping(value = "/posted/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> update(@PathVariable Integer id, @RequestBody TaskReadUpdate updatedTaskReadUpdate, Authentication auth) {
        final Task task = taskService.findById(id).orElse(null);
        if (!hasAccess(Objects.requireNonNull(task), auth)) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        task.setTitle(updatedTaskReadUpdate.getTitle());
        task.setProblem(updatedTaskReadUpdate.getProblem());
        task.setDeadline(updatedTaskReadUpdate.getDeadline());
        task.setType(updatedTaskReadUpdate.getType());
        taskService.update(task);
        return ResponseEntity.noContent().build();
    }

    /**
     * Deletes a posted task.
     *
     * @param id ID of the task to delete.
     * @return ResponseEntity indicating success or failure of the delete operation.
     */
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @DeleteMapping(value = "/posted/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id, Authentication auth) {
        final Task task = taskService.findById(id).orElse(null);
        if (!hasAccess(Objects.requireNonNull(task), auth)) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        return taskService.deleteById(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    /**
     * Assigns a freelancer to a posted task.
     *
     * @param id         ID of the task to assign a freelancer to.
     * @param proposalId ID of the proposal from the freelancer.
     * @return ResponseEntity indicating success or failure of the assignment operation.
     */
    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping(value = "/posted/{id}/proposals/{proposalId}")
    public ResponseEntity<Void> assignFreelancer(@PathVariable Integer id, @PathVariable Integer proposalId, Authentication auth) {
        final Task task = taskService.findById(id).orElse(null);
        if (!hasAccess(Objects.requireNonNull(task), auth)) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        User freelancer = userService.findFreelancerByProposalId(proposalId);
        taskService.assignFreelancer(task, freelancer);
        return ResponseEntity.noContent().build();
    }

    /**
     * Accepts a proposal for a posted task.
     *
     * @param id ID of the task to accept a proposal for.
     * @return ResponseEntity indicating success or failure of the acceptance operation.
     */
    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping(value = "/posted/{id}/accept")
    public ResponseEntity<Void> accept(@PathVariable Integer id, Authentication auth) {
        final Task task = taskService.findById(id).orElse(null);
        if (!hasAccess(Objects.requireNonNull(task), auth)) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        taskService.accept(task);
        return ResponseEntity.noContent().build();
    }

    /**
     * Removes a freelancer from a posted task.
     *
     * @param id ID of the task to remove a freelancer from.
     * @return ResponseEntity indicating success or failure of the removal operation.
     */
    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping(value = "/{id}/remove-freelancer")
    public ResponseEntity<Void> removeFreelancer(@PathVariable Integer id, Authentication auth) {
        final Task task = taskService.findById(id).orElse(null);
        if (!hasAccess(Objects.requireNonNull(task), auth)) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        taskService.removeFreelancer(task);
        return ResponseEntity.noContent().build();
    }

    /**
     * Attaches a solution to a taken task.
     *
     * @param id       ID of the task to attach a solution to.
     * @param solution Solution object containing the solution details.
     * @return ResponseEntity indicating success or failure of the attachment operation.
     */
    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping(value = "/taken/{id}/attach-solution")
    public ResponseEntity<Void> attachSolution(@PathVariable Integer id, @RequestBody Solution solution) {
        taskService.attachSolution(id, solution);
        return ResponseEntity.noContent().build();
    }

    /**
     * Sends a taken task for review.
     *
     * @param id ID of the task to send for review.
     * @return ResponseEntity indicating success or failure of sending the task for review.
     */
    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping(value = "/taken/{id}")
    public ResponseEntity<Void> sendOnReview(@PathVariable Integer id) {
        final Task task = taskService.findById(id).orElse(null);
        Objects.requireNonNull(task);
        taskService.senOnReview(task);
        return ResponseEntity.noContent().build();
    }

    /**
     * Checks if the authenticated user has access to the given task.
     *
     * <p>This method verifies if the user associated with the given authentication
     * details is the same as the customer assigned to the provided task.
     *
     * @param task the task for which access is being checked
     * @param auth the authentication object containing the user details
     * @return true if the authenticated user is the customer assigned to the task, false otherwise
     */
    private boolean hasAccess(Task task, Authentication auth) {
        final User user = ((UserDetails) auth.getPrincipal()).getUser();
        return task.getCustomer().getId().equals(user.getId());
    }
}
