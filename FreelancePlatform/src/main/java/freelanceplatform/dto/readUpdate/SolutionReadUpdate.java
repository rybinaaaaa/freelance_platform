package freelanceplatform.dto.readUpdate;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SolutionReadUpdate {

    private Integer id;
    private Integer taskId;
    private String link;
    private String description;
}
