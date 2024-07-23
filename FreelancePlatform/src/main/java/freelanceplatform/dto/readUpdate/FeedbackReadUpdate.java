package freelanceplatform.dto.readUpdate;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FeedbackReadUpdate {

    private Integer id;
    private Integer senderId;
    private Integer receiverId;
    private Integer rating;
    private String comment;
}
