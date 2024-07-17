package notificationService.notificationStrategies;

import com.fasterxml.jackson.databind.ObjectMapper;
import notificationService.service.EmailSenderService;
import notificationService.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;


@Component
public class SendAllUsersStrategy extends SendEmailStrategy{

    private final UserService userService;

    @Autowired
    public SendAllUsersStrategy(WebClient webClient, EmailSenderService emailSender, ObjectMapper mapper, UserService userService) {
        super(webClient, emailSender, mapper);
        this.userService = userService;
    }

    /**
     * Sends an email with the specified subject and body to all users.
     *
     * <p>This method retrieves a list of email addresses from the user service,
     * and for each email address in the list, it sends an email with the provided
     * subject and body. The email sending is handled by the {@code emailSender}.</p>
     *
     * @param taskJson a JSON string representing task-related data (not used in this implementation)
     * @param userJson a JSON string representing user-related data (not used in this implementation)
     * @param subject the subject of the email
     * @param body the body of the email
     */
    @Override
    public void sendEmail(String taskJson, String userJson , String subject, String body) {
        List<String> toEmails = userService.getAllUserEmails();

        if (toEmails != null) {
            toEmails.forEach(email -> emailSender.sendEmail(email, subject, body));
        }
    }
}
