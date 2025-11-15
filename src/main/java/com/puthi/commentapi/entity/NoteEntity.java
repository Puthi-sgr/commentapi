package com.puthi.commentapi.entity;

import jakarta.persistence.*;
import java.time.Instant;

//Entity uses the application properties with db creds in order access the databse

//This represents a note from the database
@Entity//Tells JPA that this class represent a database table
@Table(name = "notes") //Specifies the table name in the database
public class NoteEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false) private String title;
    @Column(nullable = true, length = 255) private String content;
    @Column(name="owner_id", nullable = false) private Long ownerId;
    @Column(name="created_at", nullable = false) private Instant createdAt = Instant.now(); //When the note was created insert date now
    @Column(name="updated_at", nullable = false) private Instant updatedAt = Instant.now();

    @PreUpdate void onUpdate(){
        this.updatedAt = Instant.now(); //When updated, set the updated at to now
    }

    //getters and setters
    public Long getId(){
        return id;
    }
    public void setId(Long id){
        this.id = id;
    }

    public String getTitle(){
        return title;
    }
    public void setTitle(String title){
        this.title= title;
    }

    public String getContent() {
        return content;
    }
    public void setContent(String c) {
        this.content = c;
    }

    public Long getOwnerId() {
        return ownerId;
    }
    public void setOwnerId(Long o) {
        this.ownerId = o;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(Instant t) {
        this.createdAt = t;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
    public void setUpdatedAt(Instant t) {
        this.updatedAt = t;
    }
}
