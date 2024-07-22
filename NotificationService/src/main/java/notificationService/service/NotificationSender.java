package notificationService.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Setter;
import notificationService.notificationStrategies.SendEmailStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class NotificationSender {

    @Setter
    private SendEmailStrategy strategy;

    /**
     * Sends an email with the specified subject and body using the provided JSON data.
     *
     * <p>This method delegates the email sending process to the configured strategy,
     * passing along the JSON data for tasks and users, along with the email subject
     * and body.</p>
     *
     * @param taskJson a JSON string representing task-related data
     * @param userJson a JSON string representing user-related data
     * @param subject the subject of the email
     * @param body the body of the email
     * @throws JsonProcessingException if there is an error processing the JSON data
     */
    public void sendEmail(String taskJson, String userJson , String subject, String body) throws JsonProcessingException {
        strategy.sendEmail(taskJson, userJson , subject, body);
    }

}
