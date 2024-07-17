package notificationService.notificationStrategies;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import notificationService.service.EmailSenderService;
import org.springframework.web.reactive.function.client.WebClient;

public class SendCustomerStrategy extends SendEmailStrategy {


    public SendCustomerStrategy(WebClient webClient, EmailSenderService emailSender, ObjectMapper mapper) {
        super(webClient, emailSender, mapper);
    }

    /**
     * Sends an email with the specified subject and body based on provided JSON data.
     *
     * @param taskJson a JSON string representing task-related data, which should contain
     *                 the customer's email address in the path "customer.email"
     * @param userJson a JSON string representing user-related data, which should contain
     *                 the user's email address in the path "email"
     * @param subject the subject of the email
     * @param body the body of the email
     * @throws JsonProcessingException if there is an error processing the JSON data
     */
    @Override
    public void sendEmail(String taskJson, String userJson , String subject, String body) throws JsonProcessingException {
        if (taskJson!=null){
            String toEmail = mapper.readTree(taskJson).get("customer").get("email").asText();
            emailSender.sendEmail(toEmail, subject, body);
        }
        if (userJson!=null){
            String toEmail = mapper.readTree(userJson).get("email").asText();
            emailSender.sendEmail(toEmail, subject, body);
        }
    }
}
