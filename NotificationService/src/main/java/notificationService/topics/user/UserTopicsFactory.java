package notificationService.topics.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import notificationService.notificationStrategies.SendEmailStrategy;
import notificationService.notificationStrategies.SendCustomerStrategy;
import notificationService.service.EmailSenderService;
import org.springframework.web.reactive.function.client.WebClient;

public class UserTopicsFactory {

    private final WebClient webClient;
    private final EmailSenderService emailSenderService;
    private final ObjectMapper mapper;

    public UserTopicsFactory(WebClient webClient, EmailSenderService emailSenderService, ObjectMapper mapper) {
        this.webClient = webClient;
        this.emailSenderService = emailSenderService;
        this.mapper = mapper;
    }

    public SendEmailStrategy createStrategy(UserTopicsTypes topicType) {
        return new SendCustomerStrategy(webClient, emailSenderService, mapper);
    }

    public String createSubject(UserTopicsTypes topicType) {
        return switch (topicType) {
            case USER_CREATED -> "Your account has been created!";
            case USER_UPDATED -> "Your profile has been updated!";
            case USER_DELETED -> "Your account has been deleted";
        };
    }

    public String createBody(UserTopicsTypes topicType, String username) {
        return switch (topicType) {
            case USER_CREATED ->
                    String.format("Congratulations! You have successfully created your account: '%s'", username);
            case USER_UPDATED -> String.format("Your profile '%s' has been successfully updated", username);
            case USER_DELETED -> String.format("Your account '%s' has been successfully deleted", username);
        };
    }
}
