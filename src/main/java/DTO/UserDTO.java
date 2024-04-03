package DTO;




import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.main.ressources.User;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private String name;
    private String email;
    private String phone;
    private Set<String> roles;

    public UserDTO(User user){
        this.name = user.getName();
        this.email = user.getEmail();
        this.phone = user.getPhone();
        this.roles = user.getRolesAsStrings();
    }
    public UserDTO(String email, Set<String> rolesSet) {
        this.email = email;
        this.roles = rolesSet;
    }
}