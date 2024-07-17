package notificationService.consumers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import notificationService.notificationStrategies.SendAllUsersStrategy;
import notificationService.notificationStrategies.SendCustomerStrategy;
import notificationService.notificationStrategies.SendEmailStrategy;
import notificationService.notificationStrategies.SendFreelancerStrategy;
import notificationService.service.EmailSenderService;
import notificationService.service.NotificationSender;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;



@Slf4j
@Component
public class UserChangesConsumer extends ChangesConsumer {

    public UserChangesConsumer(ObjectMapper mapper, NotificationSender notificationSender, EmailSenderService emailSenderService, WebClient webClient) {
        super(mapper, notificationSender, emailSenderService, webClient);
    }

    /**
     * Consumes messages from Kafka topics related to user changes and sends an email notification
     * based on the type of change.
     *
     * @param record The Kafka {@link ConsumerRecord} containing the message to be processed.
     *               The message's value is expected to be a JSON string representing user details.
     *
     * @throws JsonProcessingException If there is an error processing the JSON string from the record.
     */
    @KafkaListener(
            topics = {"user_created","user_updated","user_deleted"},
            groupId = "myGroup"
    )
    public void consumeChange(ConsumerRecord<String, String> record) throws JsonProcessingException {
        String userJson = record.value();
        log.info("Received message: {}", userJson);
        String topic = record.topic();

        String username = mapper.readTree(userJson).get("username").asText();


        SendEmailStrategy sendEmailStrategy;
        switch (topic) {
            case "user_created" -> {
                sendEmailStrategy = new SendCustomerStrategy(webClient, emailSenderService, mapper);
                notificationSender.setStrategy(sendEmailStrategy);
                notificationSender.sendEmail(
                        null,
                        userJson,
                        "Your account has been created!",
                        String.format("Congratulations! You have successfully created your account : '%s'", username));
            }
            case "user_updated" -> {
                sendEmailStrategy = new SendCustomerStrategy(webClient, emailSenderService, mapper);
                notificationSender.setStrategy(sendEmailStrategy);
                notificationSender.sendEmail(
                        null,
                        userJson,
                        "Your profile has been updated!",
                        String.format("Your profile '%s' has been successfully updated' ", username));
            }
            case "user_deleted" -> {
                sendEmailStrategy = new SendCustomerStrategy(webClient, emailSenderService, mapper);
                notificationSender.setStrategy(sendEmailStrategy);
                notificationSender.sendEmail(
                        null,
                        userJson,
                        "Your account has been deleted",
                        String.format("Your account '%s' has been successfully deleted' ", username));
            }

        }
    }

}
