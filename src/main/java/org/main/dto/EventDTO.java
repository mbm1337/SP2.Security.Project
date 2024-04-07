package org.main.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.main.ressources.Event;
import org.main.ressources.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventDTO {
    private String title;
    private String description;
    private LocalDate date;
    private LocalTime time;
    private int duration;
    private int capacity;
    private String location;
    private String image;
    private Event.Status status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    private Set<User> registeredUsers;


    public EventDTO(Event event){
        this.title = event.getTitle();
        this.description = event.getDescription();
        this.date = event.getDate();
        this.time = event.getTime();
        this.duration = event.getDuration();
        this.capacity = event.getCapacity();
        this.location = event.getLocation();
        this.image = event.getImage();
        this.status = event.getStatus();
        this.createdAt = event.getCreatedAt();
        this.updatedAt = event.getUpdatedAt();
        this.deletedAt = event.getDeletedAt();
        //this.registeredUsers = event.getUsers();
    }



}
