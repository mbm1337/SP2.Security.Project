package org.main.ressources;

import jakarta.persistence.*;
import lombok.*;
import org.mindrot.jbcrypt.BCrypt;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor

@Table(name = "users")
public class User {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String email;

    private String phone;

    private String password;
    @ManyToMany
    private Set<Role> roles = new HashSet<>();
    @ManyToMany
    private Set<Event> registeredEvents = new HashSet<>();



    public User(String email, String password) {
        this.email = email;
        this.password = password;
        String salt = BCrypt.gensalt();
        this.password = BCrypt.hashpw(password, salt);
    }
    public boolean verifyUser(String password)
    {
        return BCrypt.checkpw(password, this.password);
    }
         public void addRole(Role role ){
          roles.add(role);
           role.getUsers().add(this );


  }


    public void removeRole(Role role) {
        roles.remove(role);
        role.getUsers().remove(this);
    }

    public Set<String> getRolesAsStrings() {
        if (roles.isEmpty()) {
            return null;
        }
        Set<String> rolesAsStrings = new HashSet<>();
        roles.forEach((role) -> {
            rolesAsStrings.add(role.getName());
        });
        return rolesAsStrings;
    }

    public void addEvent(Event event) {
        registeredEvents.add(event);
        //event

    }
    public void removeEvent(Event event) {
        registeredEvents.remove(event);

        //event
    }
}



