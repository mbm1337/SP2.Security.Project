package org.main.ressources;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "title")
    private String title;
    @Column(name = "description")
    private String description;
    @Column(name = "date")
    private LocalDate date;
    @Column(name = "time")
    private LocalTime time;
    @Column(name = "duration")
    private Integer duration;
    @Column(name = "capacity")
    private int capacity;
    @Column(name = "location")
    private String location;
    @Column(name = "image")
    private String image; // ???
    @Column(name="category")
    private String category;
    @Column(name = "status")
    private Status status;
    public enum Status {
        ACTIVE,
        INACTIVE,
        CANCELLED};
    @Column(name = "createdAt")
    private LocalDateTime createdAt;
    @Column(name = "updatedAt")
    private LocalDateTime updatedAt;
    @Column(name = "deletedAt")
    private LocalDateTime deletedAt;
    @PrePersist
    private void onCreate() {
        createdAt = LocalDateTime.now();

    }
    @PreUpdate
    private void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    @PreRemove
    private void onDelete() {
        deletedAt = LocalDateTime.now();
    }

    public Event(String description) {
        this.description = description;
    }

    public Event(String description, String category, Status status) {
        this.description = description;
        this.category = category;
        this.status = status;
    }

}



