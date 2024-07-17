package freelanceplatform.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

import static freelanceplatform.kafka.topics.TaskChangesTopic.*;
import static freelanceplatform.kafka.topics.UserChangesTopic.UserCreated;

@Configuration
public class KafkaConfig {

    /**
     * Creates kafka topic for user creation
     * @return
     */
    @Bean
    public NewTopic userCreatedTopic() {
        return TopicBuilder.name(UserCreated.name())
                .build();
    }

    /**
     * Creates kafka topic for new task creation
     * @return
     */
    @Bean
    public NewTopic taskPostedTopic() {
        return TopicBuilder.name(TaskPosted.name())
                .build();
    }

    /**
     * Creates Kafka topic for freelancer assignment.
     *
     * @return a new {@link NewTopic} instance for the freelancer assignment topic
     */
    @Bean
    public NewTopic freelancerAssignedTopic(){
        return TopicBuilder.name(FreelancerAssigned.name())
                .build();
    }

    /**
     * Creates Kafka topic for task acceptance.
     *
     * @return a new {@link NewTopic} instance for the task acceptance topic
     */
    @Bean
    public NewTopic TaskAcceptedTopic(){
        return TopicBuilder.name(TaskAccepted.name())
                .build();
    }

    /**
     * Creates Kafka topic for freelancer removal.
     *
     * @return a new {@link NewTopic} instance for the freelancer removal topic
     */
    @Bean
    public NewTopic freelancerRemovedTopic(){
        return TopicBuilder.name(FreelancerRemoved.name())
                .build();
    }

    /**
     * Creates Kafka topic for sending tasks for review.
     *
     * @return a new {@link NewTopic} instance for the task review topic
     */
    @Bean
    public NewTopic taskSendOnReviewTopic(){
        return TopicBuilder.name(TaskSendOnReview.name())
                .build();
    }
}
