package notificationService.consumers;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import notificationService.notificationStrategies.SendEmailStrategy;
import notificationService.service.EmailSenderService;
import notificationService.service.NotificationSender;
import notificationService.service.UserService;
import notificationService.topics.task.TaskTopicsTypes;
import notificationService.topics.task.TopicsFactory;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Component
public class TaskChangesConsumer extends ChangesConsumer {

    private final TopicsFactory topicsFactory;

    @Autowired
    public TaskChangesConsumer(ObjectMapper mapper, NotificationSender notificationSender, EmailSenderService emailSenderService, WebClient webClient, UserService userService) {
        super(mapper, notificationSender, emailSenderService, webClient);
        this.topicsFactory = new TopicsFactory(webClient, emailSenderService, mapper, userService);
    }

    /**
     * Consumes messages from specified Kafka topics and processes them.
     *
     * @param record the consumed Kafka record
     * @throws JsonProcessingException if there is an error processing the JSON in the record
     */
    @KafkaListener(
            topics = {"task_posted", "freelancer_assigned", "task_accepted", "freelancer_removed", "task_send_on_review"})
    public void consumeChange(ConsumerRecord<String, String> record) throws JsonProcessingException {
        String taskJson = record.value();
        log.info("Received message: {}", taskJson);
        String topic = record.topic();
        TaskTopicsTypes topicType = TaskTopicsTypes.valueOf(topic.toUpperCase());
        String taskTitle = mapper.readTree(taskJson).get("title").asText();
        String freelancerUsername = "";

        if (!mapper.readTree(taskJson).get("freelancer").asText().equals("null"))
            freelancerUsername = mapper.readTree(taskJson).get("freelancer").get("username").asText();

        SendEmailStrategy sendEmailStrategy = topicsFactory.createStrategy(topicType);
        notificationSender.setStrategy(sendEmailStrategy);
        notificationSender.sendEmail(
                taskJson,
                null,
                topicsFactory.createSubject(topicType),
                topicsFactory.createBody(topicType, taskTitle, freelancerUsername)
        );
    }
}
