package org.main.ressources;

import jakarta.persistence.*;
import lombok.*;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Collections;

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
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;


    @Column(name = "email" ,unique = true)
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name = "password")
    private String password;



    @ManyToMany
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();


    public User(String name,String email,String phone, String password) {
        this.name = name;
        this.email = email;
        this.phone = phone;
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

    public void updatePassword(String newPassword) {
        String salt = BCrypt.gensalt();
        this.password = BCrypt.hashpw(newPassword, salt);
    }



//    public void addEvent(Event event) {
//        registeredEvents.add(event);
//        event.getUsers().add(this );
//
//    }
//    public void removeEvent(Event event) {
//        registeredEvents.remove(event);
//        event.getUsers().remove(this);
//
//    }
//    public Set<String> getEventsAsStrings() {
//        if (registeredEvents.isEmpty()) {
//            return Collections.emptySet();
//        }
//        Set<String> eventsAsStrings = new HashSet<>();
//        registeredEvents.forEach((event) -> {
//            eventsAsStrings.add(event.getDescription());
//        });
//        return eventsAsStrings;
//    }

}






