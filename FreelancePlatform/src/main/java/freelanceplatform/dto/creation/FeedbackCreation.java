package freelanceplatform.dto.creation;

import lombok.Value;

@Value
public class FeedbackCreation {

    Integer senderId;
    Integer receiverId;
    Integer rating;
    String comment;
}
