package freelanceplatform.dto.creation;

import freelanceplatform.model.TaskStatus;
import freelanceplatform.model.TaskType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class TaskCreation {

    private Integer customerId;
    private String title;
    private String problem;
    private LocalDateTime deadline;
    private TaskStatus taskStatus;
    private Double payment;
    private TaskType type;
}