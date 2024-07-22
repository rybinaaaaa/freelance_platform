package notificationService.notificationStrategies;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import notificationService.service.EmailSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public abstract class SendEmailStrategy {


    protected final WebClient webClient;

    protected final EmailSenderService emailSender;

    protected final ObjectMapper mapper;

    @Autowired
    public SendEmailStrategy(WebClient webClient, EmailSenderService emailSender, ObjectMapper mapper) {
        this.webClient = webClient;
        this.emailSender = emailSender;
        this.mapper = mapper;
    }

    /**
     * Sends an email with the specified subject and body based on provided JSON data.
     *
     * @param taskJson a JSON string representing task-related data. The specific structure
     *                 and the path to the email address within this JSON should be defined
     *                 by the concrete implementation.
     * @param userJson a JSON string representing user-related data. The specific structure
     *                 and the path to the email address within this JSON should be defined
     *                 by the concrete implementation.
     * @param subject the subject of the email.
     * @param body the body of the email.
     * @throws JsonProcessingException if there is an error processing the JSON data.
     */
    public abstract void sendEmail(String taskJson, String userJson , String subject, String body) throws JsonProcessingException;
}
