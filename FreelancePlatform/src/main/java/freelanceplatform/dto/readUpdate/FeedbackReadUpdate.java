package freelanceplatform.dto.readUpdate;

import lombok.Value;

@Value
public class FeedbackReadUpdate {

    Integer id;
    Integer senderId;
    Integer receiverId;
    Integer rating;
    String comment;
}
