package notificationService.notificationStrategies;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import notificationService.service.EmailSenderService;
import org.springframework.web.reactive.function.client.WebClient;

public class SendFreelancerStrategy extends SendEmailStrategy {

    public SendFreelancerStrategy(WebClient webClient, EmailSenderService emailSender, ObjectMapper mapper) {
        super(webClient, emailSender, mapper);
    }

    /**
     * Sends an email to the specified recipients based on the provided JSON data, subject, and body.
     *
     * @param taskJson JSON string representing task details which includes the email address of a freelancer.
     * @param userJson JSON string representing user details which includes the email address of a user.
     * @param subject The subject of the email to be sent.
     * @param body The body content of the email to be sent.
     *
     * @throws JsonProcessingException If there is an error processing the JSON input.
     */
    @Override
    public void sendEmail(String taskJson, String userJson , String subject, String body) throws JsonProcessingException {
        if (taskJson!=null){
            String toEmail = mapper.readTree(taskJson).get("freelancer").get("email").asText();
            emailSender.sendEmail(toEmail, subject, body);
        }
        if (userJson!=null){
            String toEmail = mapper.readTree(userJson).get("email").asText();
            emailSender.sendEmail(toEmail, subject, body);
        }
    }
}


