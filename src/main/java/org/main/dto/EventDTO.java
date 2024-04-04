package org.main.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.main.ressources.Event;
import org.main.ressources.User;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventDTO {
    private String title;
    private String description;
    private String date;
    private String time;
    private int duration;
    private int capacity;
    private String location;
    private String image;
    private String status;
    private String createdAt;
    private String updatedAt;
    private String deletedAt;

    private Set<User> registeredUsers;


    public EventDTO(Event event){
        this.title = event.getTitle();
        this.description = event.getDescription();
        this.date = event.getDate().toString();
        this.time = event.getTime().toString();
        this.duration = event.getDuration();
        this.capacity = event.getCapacity();
        this.location = event.getLocation();
        this.image = event.getImage();
        this.status = event.getStatus().toString();
        this.createdAt = event.getCreatedAt().toString();
        this.updatedAt = event.getUpdatedAt().toString();
        this.deletedAt = event.getDeletedAt().toString();
        this.registeredUsers = event.getUsers();
    }



}
