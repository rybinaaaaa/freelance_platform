package freelanceplatform.dto.creation;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SolutionCreation {

    private Integer taskId;
    private String link;
    private String description;
}
