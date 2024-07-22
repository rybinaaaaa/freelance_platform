package notificationService.consumers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import notificationService.notificationStrategies.SendEmailStrategy;
import notificationService.service.EmailSenderService;
import notificationService.service.NotificationSender;
import notificationService.topics.user.UserTopicsFactory;
import notificationService.topics.user.UserTopicsTypes;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Component
public class UserChangesConsumer extends ChangesConsumer {

    private final UserTopicsFactory userTopicsFactory;

    public UserChangesConsumer(ObjectMapper mapper, NotificationSender notificationSender, EmailSenderService emailSenderService, WebClient webClient) {
        super(mapper, notificationSender, emailSenderService, webClient);
        this.userTopicsFactory = new UserTopicsFactory(webClient, emailSenderService, mapper);
    }

    /**
     * Consumes messages from Kafka topics related to user changes and sends an email notification
     * based on the type of change.
     *
     * @param record The Kafka {@link ConsumerRecord} containing the message to be processed.
     *               The message's value is expected to be a JSON string representing user details.
     * @throws JsonProcessingException If there is an error processing the JSON string from the record.
     */
    @KafkaListener(
            topics = {"user_created", "user_updated", "user_deleted"}
    )
    public void consumeChange(ConsumerRecord<String, String> record) throws JsonProcessingException {
        String userJson = record.value();
        log.info("Received message: {}", userJson);
        String topic = record.topic();
        UserTopicsTypes topicType = UserTopicsTypes.fromTopicName(topic);
        String username = mapper.readTree(userJson).get("username").asText();

        SendEmailStrategy sendEmailStrategy = userTopicsFactory.createStrategy(topicType);
        notificationSender.setStrategy(sendEmailStrategy);
        notificationSender.sendEmail(
                null,
                userJson,
                userTopicsFactory.createSubject(topicType),
                userTopicsFactory.createBody(topicType, username)
        );
    }
}
