package org.main.ressources;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;

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


}
