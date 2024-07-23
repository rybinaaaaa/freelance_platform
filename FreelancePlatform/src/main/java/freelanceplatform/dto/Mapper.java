package freelanceplatform.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import freelanceplatform.dto.creation.FeedbackCreation;
import freelanceplatform.dto.creation.ProposalCreation;
import freelanceplatform.dto.creation.TaskCreation;
import freelanceplatform.dto.creation.UserCreation;
import freelanceplatform.dto.readUpdate.*;
import freelanceplatform.model.*;
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

    /**
     * Converts a User entity to a UserDTO.
     *
     * @param user the User entity to convert
     * @return the converted UserDTO
     */
    public UserReadUpdate toReadUser(User user) {
        return new UserReadUpdate(
                user.getId(),
                user.getUsername(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getRating(),
                user.getRole());
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
        return new ProposalReadUpdate(
                proposal.getId(),
                Optional.ofNullable(proposal.getFreelancer())
                        .map(User::getId)
                        .orElse(null),
                Optional.ofNullable(proposal.getTask())
                        .map(Task::getId)
                        .orElse(null));
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
                        .map(userService::find)
                        .orElse(null));
        proposal.setTask(
                Optional.ofNullable(proposalReadUpdate.getTaskId())
                        .map(taskService::getById)
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
        TaskReadUpdate taskReadUpdate = new TaskReadUpdate();
        taskReadUpdate.setId(task.getId());
        taskReadUpdate.setCustomerUsername(task.getCustomer().getUsername());
        if (task.getFreelancer() != null) {
            taskReadUpdate.setFreelancerUsername(task.getFreelancer().getUsername());
        }
        taskReadUpdate.setTitle(task.getTitle());
        taskReadUpdate.setProblem(task.getProblem());
        taskReadUpdate.setDeadline(task.getDeadline());
        taskReadUpdate.setStatus(task.getStatus());
        taskReadUpdate.setType(task.getType());
        taskReadUpdate.setPayment(task.getPayment());
        return taskReadUpdate;
    }

    /**
     * Converts a TaskCreationDTO to a Task entity.
     *
     * @param taskCreation the TaskCreationDTO to convert
     * @return the converted Task entity
     */
    public Task toTask(TaskCreation taskCreation) {
        Task task = new Task(
                taskCreation.getCustomer(),
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
                .map(userService::find)
                .orElse(null);

        User sender = Optional.ofNullable(fb.getSenderId())
                .map(userService::find)
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
                .map(userService::find)
                .orElse(null);

        User sender = Optional.ofNullable(fb.getSenderId())
                .map(userService::find)
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
        return new FeedbackReadUpdate(
                fb.getId(),
                fb.getSender().getId(),
                fb.getReceiver().getId(),
                fb.getRating(),
                fb.getComment()
        );
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
                        .map(userService::find)
                        .orElse(null),
                Optional.ofNullable(proposalCreation.getTaskId())
                        .map(taskService::getById)
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
        return new ProposalCreation(
                Optional.ofNullable(proposal.getFreelancer()).map(User::getId).orElse(null), // freelancer
                Optional.ofNullable(proposal.getTask()).map(Task::getId).orElse(null) // task
        );
    }
}
