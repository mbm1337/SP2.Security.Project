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
    private int id;
    private String name;
    private String email;
    private String phone;
    private String password;
    private Set<String> roles;

    public UserDTO(User user){
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.phone = user.getPhone();
        this.password = user.getPassword();
        this.roles = user.getRolesAsStrings();
    }

    public UserDTO(String email, Set<String> rolesSet) {
        this.email = email;
        this.roles = rolesSet;
    }


    @Override
    public String toString() {
        return "UserDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", roles=" + roles +
                '}';
    }


}
