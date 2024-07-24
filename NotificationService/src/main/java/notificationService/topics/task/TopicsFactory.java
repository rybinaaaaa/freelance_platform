package notificationService.topics.task;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import notificationService.notificationStrategies.SendAllUsersStrategy;
import notificationService.notificationStrategies.SendCustomerStrategy;
import notificationService.notificationStrategies.SendEmailStrategy;
import notificationService.notificationStrategies.SendFreelancerStrategy;
import notificationService.service.EmailSenderService;
import notificationService.service.UserService;
import org.springframework.web.reactive.function.client.WebClient;

@AllArgsConstructor
public class TopicsFactory {

    private final WebClient webClient;
    private final EmailSenderService emailSenderService;
    private final ObjectMapper mapper;
    private final UserService userService;

    /**
     * Creates a strategy for sending emails based on the task topic type.
     *
     * @param topicType the type of the task topic
     * @return the SendEmailStrategy corresponding to the given task topic type
     */
    public SendEmailStrategy createStrategy(TaskTopicsTypes topicType) {
        return switch (topicType) {
            case TASK_POSTED -> new SendAllUsersStrategy(webClient, emailSenderService, mapper, userService);
            case FREELANCER_ASSIGNED, TASK_ACCEPTED, FREELANCER_REMOVED -> new SendFreelancerStrategy(webClient, emailSenderService, mapper);
            case TASK_SEND_ON_REVIEW -> new SendCustomerStrategy(webClient, emailSenderService, mapper);
            default -> throw new IllegalArgumentException("Unsupported topic type: " + topicType);
        };
    }

    /**
     * Creates the subject of the email based on the task topic type.
     *
     * @param topicType the type of the task topic
     * @return the subject string for the email
     */
    public String createSubject(TaskTopicsTypes topicType) {
        return switch (topicType) {
            case TASK_POSTED -> "New task was posted!";
            case FREELANCER_ASSIGNED -> "You have been assigned to a task!";
            case TASK_ACCEPTED -> "Congratulations! One of your completed tasks has been accepted";
            case FREELANCER_REMOVED -> "We are sorry! You were removed as task assignee!";
            case TASK_SEND_ON_REVIEW -> "One of your tasks was sent on review!";
            default -> throw new IllegalArgumentException("Unsupported topic type: " + topicType);
        };
    }

    /**
     * Creates the body of the email based on the task topic type, task title, and freelancer username.
     *
     * @param topicType          the type of the task topic
     * @param taskTitle          the title of the task
     * @param freelancerUsername the username of the freelancer (if applicable)
     * @return the body string for the email
     */
    public String createBody(TaskTopicsTypes topicType, String taskTitle, String freelancerUsername) {
        return switch (topicType) {
            case TASK_POSTED -> String.format("Task: '%s' was posted recently. This opportunity could be perfect for you!", taskTitle);
            case FREELANCER_ASSIGNED -> String.format("We are pleased to inform you that you have been assigned to a task '%s'", taskTitle);
            case TASK_ACCEPTED -> String.format("We are pleased to inform you that one of your completed tasks '%s' has been accepted by the customer.", taskTitle);
            case FREELANCER_REMOVED -> String.format("We are sorry to inform you that you were removed as task assignee from task '%s'", taskTitle);
            case TASK_SEND_ON_REVIEW -> String.format("We wanted to inform you that the freelancer '%s' has submitted the task '%s' for your review", freelancerUsername, taskTitle);
            default -> throw new IllegalArgumentException("Unsupported topic type: " + topicType);
        };
    }
}
