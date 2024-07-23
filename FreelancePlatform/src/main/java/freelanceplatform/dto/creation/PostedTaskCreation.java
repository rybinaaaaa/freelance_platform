package freelanceplatform.dto.creation;

import freelanceplatform.model.TaskStatus;
import freelanceplatform.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class PostedTaskCreation {

    private User customer;
    private User freelancer;
    private String title;
    private String problem;
    private Date deadline;
    private TaskStatus status;
    private Double payment;
}
