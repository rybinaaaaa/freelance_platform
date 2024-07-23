package freelanceplatform.dto.creation;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FeedbackCreation {

    private Integer senderId;
    private Integer receiverId;
    private Integer rating;
    private String comment;
}
