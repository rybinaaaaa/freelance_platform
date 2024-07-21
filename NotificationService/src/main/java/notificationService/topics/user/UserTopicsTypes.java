package notificationService.topics.user;

import lombok.Getter;

@Getter
public enum UserTopicsTypes {
    USER_CREATED("user_created"),
    USER_UPDATED("user_updated"),
    USER_DELETED("user_deleted");

    private final String topicName;

    UserTopicsTypes(String topicName) {
        this.topicName = topicName;
    }

    public static UserTopicsTypes fromTopicName(String topicName) {
        for (UserTopicsTypes type : values()) {
            if (type.getTopicName().equals(topicName)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unsupported topic name: " + topicName);
    }
}

