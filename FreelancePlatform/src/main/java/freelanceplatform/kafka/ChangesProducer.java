package freelanceplatform.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ChangesProducer<T> {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Autowired
    public ChangesProducer(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    /**
     * Sends a message to a specified Kafka topic.
     *
     * <p>This method logs the message before sending it to the topic.
     *
     * @param message the message to be sent
     * @param topic the topic to which the message is to be sent
     */
    public void sendMessage(String message, T topic) {
        log.info(String.format("event - %s", message));
        kafkaTemplate.send(topic.toString(), message);
    }

    /**
     * Converts an object to its JSON string representation.
     *
     * @param o the object to be converted
     * @return the JSON string representation of the object
     * @throws RuntimeException if the object cannot be converted to JSON
     */
    public String toJsonString(Object o){
        try {
            return objectMapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
