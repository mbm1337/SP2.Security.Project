package org.main.ressources;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "events")
public class Event {
    @Id
    private String id;
    @Column(name = "title")
    private String title;
    @Column(name = "description")
    private String description;
    @Column(name = "date")
    private LocalDateTime date;
    @Column(name = "time")
    private LocalDateTime time;
    @Column(name = "duration")
    private int duration;
    @Column(name = "capacity")
    private int capacity;
    @Column(name = "location")
    private String location;
    @Column(name = "image")
    private String image; // ???

    //@Column(name = "status")
    private enum status {ACTIVE, INACTIVE, CANCELLED};
    @Column(name = "createdAt")
    private LocalDateTime createdAt;
    @Column(name = "updatedAt")
    private LocalDateTime updatedAt;
    @Column(name = "deletedAt")
    private LocalDateTime deletedAt;

    @ManyToMany
    Set<User>users = new HashSet<>();



    public Event(String description) {
        this.description = description;
    }



}



