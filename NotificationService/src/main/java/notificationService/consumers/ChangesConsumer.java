package notificationService.consumers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import notificationService.service.EmailSenderService;
import notificationService.service.NotificationSender;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;


@Component
public abstract class ChangesConsumer {

    protected final ObjectMapper mapper;

    protected final NotificationSender notificationSender;

    protected final EmailSenderService emailSenderService;

    protected final WebClient webClient;

    @Autowired
    public ChangesConsumer(ObjectMapper mapper, NotificationSender notificationSender, EmailSenderService emailSenderService, WebClient webClient) {
        this.mapper = mapper;
        this.notificationSender = notificationSender;
        this.emailSenderService = emailSenderService;
        this.webClient = webClient;
    }

    /**
     * An abstract method to process Kafka messages related to changes in tasks or users.
     *
     * @param record The Kafka {@link ConsumerRecord} containing the message to be processed.
     *               The message's value is expected to be a JSON string that contains task or user details.
     *
     * @throws JsonProcessingException If there is an error processing the JSON string from the record.
     */
    abstract void consumeChange(ConsumerRecord<String, String> record) throws JsonProcessingException;
}
