package freelanceplatform.dto.creation;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProposalCreation {

    private Integer freelancerId;
    private Integer taskId;
}

