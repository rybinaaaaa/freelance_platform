package freelanceplatform.dto.creation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCreation {

    private  String username;
    private  String firstName;
    private  String lastName;
    private  String email;
    private  String password;

}
