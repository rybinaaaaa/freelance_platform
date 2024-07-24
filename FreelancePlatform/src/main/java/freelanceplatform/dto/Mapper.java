package freelanceplatform.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import freelanceplatform.dto.creation.*;
import freelanceplatform.dto.readUpdate.*;
import freelanceplatform.exceptions.NotFoundException;
import freelanceplatform.model.*;
import freelanceplatform.services.SolutionService;
import freelanceplatform.services.TaskService;
import freelanceplatform.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Mapper class for converting between entities and DTOs.
 * This class provides methods to map User, Proposal, Task, and Feedback entities to their corresponding DTOs and vice versa.
 */
@Component
@RequiredArgsConstructor
public class Mapper {

    private final UserService userService;
    private final TaskService taskService;
    private final ObjectMapper objectMapper;
    private final SolutionService solutionService;

    /**
     * Converts a User entity to a UserDTO.
     *
     * @param user the User entity to convert
     * @return the converted UserDTO
     */
    public UserReadUpdate toReadUser(User user) {
        return UserReadUpdate.builder()
                .id(user.getId())
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .rating(user.getRating())
                .role(user.getRole())
                .build();
    }

    /**
     * Converts a UserCreationDTO to a User entity.
     *
     * @param userCreation the UserCreationDTO to convert
     * @return the converted User entity
     */
    public User toUser(UserCreation userCreation) {
        return User.builder()
                .username(userCreation.getUsername())
                .firstName(userCreation.getFirstName())
                .lastName(userCreation.getLastName())
                .email(userCreation.getEmail())
                .password(userCreation.getPassword())
                .role(Role.USER)
                .build();
    }

    /**
     * Converts user to json
     *
     * @param user to convert
     * @return json string
     */
    public String convertUserToJson(User user) {
        try {
            return objectMapper.writeValueAsString(user);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting User to JSON", e);
        }
    }

    /**
     * Converts a Proposal entity to a ProposalDTO.
     *
     * @param proposal the Proposal entity to convert
     * @return the converted ProposalDTO
     */
    public ProposalReadUpdate toProposalReadUpdate(Proposal proposal) {
        return ProposalReadUpdate.builder()
                .id(proposal.getId())
                .freelancerId(Optional.ofNullable(proposal.getFreelancer())
                        .map(User::getId)
                        .orElse(null))
                .taskId(Optional.ofNullable(proposal.getTask())
                        .map(Task::getId)
                        .orElse(null))
                .build();
    }

    /**
     * Converts a ProposalDTO to a Proposal entity.
     *
     * @param proposalReadUpdate the ProposalDTO to convert
     * @return the converted Proposal entity
     */
    public Proposal toProposal(ProposalReadUpdate proposalReadUpdate) {
        Proposal proposal = new Proposal();
        proposal.setId(proposalReadUpdate.getId());
        proposal.setFreelancer(
                Optional.ofNullable(proposalReadUpdate.getFreelancerId())
                        .map(userService::findById)
                        .map(Optional::get)
                        .orElse(null));
        proposal.setTask(
                Optional.ofNullable(proposalReadUpdate.getTaskId()).flatMap(taskService::findById)
                        .orElse(null)
        );

        return proposal;
    }

    /**
     * Converts a Task entity to a TaskDTO.
     *
     * @param task the Task entity to convert
     * @return the converted TaskDTO
     */
    public TaskReadUpdate toTaskReadUpdate(Task task) {
        return TaskReadUpdate.builder()
                .id(task.getId())
                .customerUsername(Optional.ofNullable(task.getCustomer())
                        .map(User::getUsername)
                        .orElse(null))
                .title(task.getTitle())
                .problem(task.getProblem())
                .deadline(task.getDeadline())
                .status(task.getStatus())
                .type(task.getType())
                .payment(task.getPayment())
                .freelancerUsername(Optional.ofNullable(task.getFreelancer())
                        .map(User::getUsername)
                        .orElse(null))
                .build();
    }

    /**
     * Converts a TaskCreationDTO to a Task entity.
     *
     * @param taskCreation the TaskCreationDTO to convert
     * @return the converted Task entity
     */
    public Task toTask(TaskCreation taskCreation) {
        Task task = new Task(
                Optional.ofNullable(taskCreation.getCustomerId()).map(userService::findById).map(Optional::get).orElse(null),
                taskCreation.getTitle(),
                taskCreation.getProblem(),
                taskCreation.getDeadline(),
                taskCreation.getPayment(),
                taskCreation.getType());
        task.setStatus(taskCreation.getTaskStatus());
        return task;
    }

    /**
     * Converts a FeedbackDTO to a Feedback entity.
     *
     * @param fb the FeedbackDTO to convert
     * @return the converted Feedback entity
     */
    public Feedback toFeedback(FeedbackReadUpdate fb) {
        Feedback feedback = new Feedback();
        feedback.setId(fb.getId());
        feedback.setRating(fb.getRating());
        feedback.setComment(fb.getComment());

        User receiver = Optional.ofNullable(fb.getReceiverId())
                .map(userService::findById)
                .map(Optional::get)
                .orElse(null);

        User sender = Optional.ofNullable(fb.getSenderId())
                .map(userService::findById)
                .map(Optional::get)
                .orElse(null);

        feedback.setReceiver(receiver);
        feedback.setSender(sender);

        return feedback;
    }

    /**
     * Converts a FeedbackCreationDTO to a Feedback entity.
     *
     * @param fb the FeedbackCreationDTO to convert
     * @return the converted Feedback entity
     */
    public Feedback toFeedback(FeedbackCreation fb) {
        Feedback feedback = new Feedback();
        feedback.setRating(fb.getRating());
        feedback.setComment(fb.getComment());

        User receiver = Optional.ofNullable(fb.getReceiverId())
                .map(userService::findById)
                .map(Optional::get)
                .orElse(null);

        User sender = Optional.ofNullable(fb.getSenderId())
                .map(userService::findById)
                .map(Optional::get)
                .orElse(null);

        feedback.setReceiver(receiver);
        feedback.setSender(sender);

        return feedback;
    }

    /**
     * Converts a Feedback entity to a FeedbackDTO.
     *
     * @param fb the Feedback entity to convert
     * @return the converted FeedbackDTO
     */
    public FeedbackReadUpdate toFeedbackReadUpdate(Feedback fb) {
        return FeedbackReadUpdate.builder()
                .id(fb.getId())
                .senderId(fb.getSender().getId())
                .receiverId(fb.getReceiver().getId())
                .rating(fb.getRating())
                .comment(fb.getComment())
                .build();
    }

    /**
     * Converts a ProposalCreationDTO to a Proposal entity.
     *
     * @param proposalCreation the ProposalCreationDTO to convert
     * @return the converted Proposal entity
     */
    public Proposal toProposal(ProposalCreation proposalCreation) {
        return new Proposal(
                Optional.ofNullable(proposalCreation.getFreelancerId())
                        .map(userService::findById)
                        .map(Optional::get)
                        .orElse(null),
                Optional.ofNullable(proposalCreation.getTaskId()).flatMap(taskService::findById)
                        .orElse(null)
        );
    }

    /**
     * Converts a ProposalCreationDTO to a Proposal entity.
     *
     * @param proposal the Proposal to convert
     * @return the converted ProposalCreationDTO
     */
    public ProposalCreation toProposalCreation(Proposal proposal) {
        return ProposalCreation.builder()
                .freelancerId(Optional.ofNullable(proposal.getFreelancer()).map(User::getId).orElse(null))
                .taskId(Optional.ofNullable(proposal.getTask()).map(Task::getId).orElse(null))
                .build();
    }

    /**
     * Converts a {@link SolutionCreation} object to a {@link Solution} object.
     *
     * @param solutionCreation the solution creation object to convert
     * @return the corresponding {@link Solution} object
     */
    public Solution toSolution(SolutionCreation solutionCreation) {

        Solution solution = new Solution();

        solution.setTask(
                Optional.ofNullable(solutionCreation.getTaskId()).flatMap(taskService::findById)
                        .orElse(null)
        );
        solution.setLink(solutionCreation.getLink());
        solution.setDescription(solutionCreation.getDescription());

        return solution;
    }

    /**
     * Converts a {@link Solution} object to a {@link SolutionReadUpdate} object.
     *
     * @param solution the solution object to convert
     * @return the corresponding {@link SolutionReadUpdate} object
     */
    public SolutionReadUpdate toSolutionReadUpdate(Solution solution) {
        return SolutionReadUpdate.builder()
                .id(solution.getId())
                .taskId(Optional.ofNullable(solution.getTask()).map(Task::getId).orElse(null))
                .link(solution.getLink())
                .description(solution.getDescription())
                .build();
    }

    /**
     * Converts a {@link Solution} object to a {@link SolutionCreation} object.
     *
     * @param solution the solution object to convert
     * @return the corresponding {@link SolutionCreation} object
     */
    public SolutionCreation toSolutionCreation(Solution solution) {
        return SolutionCreation.builder()
                .link(solution.getLink())
                .description(solution.getDescription())
                .taskId(Optional.ofNullable(solution.getTask()).map(Task::getId).orElse(null))
                .build();
    }

    /**
     * Converts a {@link Task} object to a {@link TaskCreation} object.
     *
     * @param task the task object to convert
     * @return the corresponding {@link TaskCreation} object
     */
    public TaskCreation toTaskCreation(Task task) {
        return TaskCreation.builder()
                .customerId(Optional.ofNullable(task.getCustomer())
                        .map(User::getId)
                        .orElse(null))
                .title(task.getTitle())
                .problem(task.getProblem())
                .deadline(task.getDeadline())
                .taskStatus(task.getStatus())
                .payment(task.getPayment())
                .type(task.getType())
                .build();
    }

    /**
     * Updates a {@link Solution} object based on the provided {@link SolutionReadUpdate} object and ID.
     *
     * @param updatedSolution the solution read update object containing the updated values
     * @param id the ID of the solution to update
     * @return the updated {@link Solution} object
     * @throws NotFoundException if no solution is found with the provided ID
     */
    public Solution toSolution(SolutionReadUpdate updatedSolution, Integer id) {
        return solutionService.findById(id)
                .map(solution -> {
                    solution.setDescription(updatedSolution.getDescription());
                    solution.setLink(updatedSolution.getLink());
                    return solution;
                })
                .orElseThrow(() -> new NotFoundException("Solution has not found"));
    }
}
