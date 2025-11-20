package com.puthi.commentapi.entity;

import jakarta.persistence.*;
import java.time.Instant;
import com.puthi.commentapi.entity.NoteEntity;


@Entity
@Table(name = "comments")
public class CommentEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) //Tells JPA to use the database's identity column feature to generate primary keys.
    private Long id;

    //Also to specify that this a relationship
    @ManyToOne(optional = false)//Many comments can belong to one note
    @JoinColumn(name="note_id", nullable = false) //Telling jpa to join this colun
    private NoteEntity note;

    @ManyToOne
    @JoinColumn(name="parent_id")
    private CommentEntity parent; //Self-referencing many-to-one relationship for parent comment

    @Column(nullable = false, length = 2000)
    private String content;

    @Column(nullable = false)
    private Long authorId;

    @Column(nullable = false)
    private int depth = 0;

    @Column(nullable = false, length = 500)
    private String path;

    @Column(nullable = false)
    private boolean delete = false;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt = Instant.now(); //When the note was created insert date now

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt = Instant.now();


    @PrePersist
    void onCreate(){
        this.depth = (this.parent == null) ? 0 : this.parent.depth + 1; //Set depth based on whether it's a reply or a top-level comment
        this.createdAt = Instant.now(); //When created, set the created at to now
        this.updatedAt = Instant.now(); //When created, set the updated at to now
    }

    @PreUpdate
    void onUpdate(){
        this.updatedAt = Instant.now(); //When updated, set the updated at to now
    }

    //Getters and setters
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public NoteEntity getNote() {
        return note;
    }
    public void setNote(NoteEntity note) {
        this.note = note;
    }
    public CommentEntity getParent() {
        return parent;
    }
    public void setParent(CommentEntity parent) {
        this.parent  = parent;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public Long getAuthorId() {
        return authorId;
    }
    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }
    public int getDepth() {
        return depth;
    }
    public void setDepth(int depth) {
        this.depth = depth;
    }
    public String getPath() {
        return path;
    }
    public void setPath(String path) {
        this.path = path;
    }
    public boolean isDelete() {
        return delete;
    }
    public void setDelete(boolean delete) {
        this.delete = delete;
    }
    public Instant getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
    public Instant getUpdatedAt() {
        return updatedAt;
    }
    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}