package org.main.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.main.ressources.User;

import java.util.Set;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private String name;
    private String email;
    private String phone;
    private String password;
    private Set<String> roles;

    public UserDTO(User user){
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.roles = user.getRolesAsStrings();
    }
    public UserDTO(String email, Set<String> rolesSet) {
        this.email = email;
        this.roles = rolesSet;
    }

}
