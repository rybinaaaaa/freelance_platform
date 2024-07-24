package notificationService.topics.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import notificationService.notificationStrategies.SendEmailStrategy;
import notificationService.notificationStrategies.SendCustomerStrategy;
import notificationService.service.EmailSenderService;
import org.springframework.web.reactive.function.client.WebClient;

@AllArgsConstructor
public class UserTopicsFactory {

    private final WebClient webClient;
    private final EmailSenderService emailSenderService;
    private final ObjectMapper mapper;

    /**
     * Creates a strategy for sending emails based on the user topic type.
     *
     * @param topicType the type of the user topic
     * @return the SendEmailStrategy corresponding to the given user topic type
     */
    public SendEmailStrategy createStrategy(UserTopicsTypes topicType) {
        return new SendCustomerStrategy(webClient, emailSenderService, mapper);
    }

    /**
     * Creates the subject of the email based on the user topic type.
     *
     * @param topicType the type of the user topic
     * @return the subject string for the email
     */
    public String createSubject(UserTopicsTypes topicType) {
        return switch (topicType) {
            case USER_CREATED -> "Your account has been created!";
            case USER_UPDATED -> "Your profile has been updated!";
            case USER_DELETED -> "Your account has been deleted";
        };
    }

    /**
     * Creates the body of the email based on the user topic type and username.
     *
     * @param topicType the type of the user topic
     * @param username  the username of the user
     * @return the body string for the email
     */
    public String createBody(UserTopicsTypes topicType, String username) {
        return switch (topicType) {
            case USER_CREATED ->
                    String.format("Congratulations! You have successfully created your account: '%s'", username);
            case USER_UPDATED -> String.format("Your profile '%s' has been successfully updated", username);
            case USER_DELETED -> String.format("Your account '%s' has been successfully deleted", username);
        };
    }
}
