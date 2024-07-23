package freelanceplatform.dto.readUpdate;

import freelanceplatform.model.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserReadUpdate {

    private Integer id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private int rating;
    private Role role;
}
