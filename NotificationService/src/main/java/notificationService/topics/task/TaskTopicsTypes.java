package notificationService.topics.task;

public enum TaskTopicsTypes {
    TASK_POSTED("task_posted"),
    FREELANCER_ASSIGNED("freelancer_assigned"),
    TASK_ACCEPTED("task_accepted"),
    FREELANCER_REMOVED("freelancer_removed"),
    TASK_SEND_ON_REVIEW("task_send_on_review");

    private final String topicName;

    TaskTopicsTypes(String topicName) {
        this.topicName = topicName;
    }

    public String getTopicName() {
        return topicName;
    }
}
