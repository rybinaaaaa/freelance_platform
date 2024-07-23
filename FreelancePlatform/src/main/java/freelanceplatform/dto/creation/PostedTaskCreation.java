package freelanceplatform.dto.creation;

import freelanceplatform.model.TaskStatus;
import freelanceplatform.model.User;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class PostedTaskCreation {

    private User customer;
    private User freelancer;
    private String title;
    private String problem;
    private Date deadline;
    private TaskStatus status;
    private Double payment;
}
