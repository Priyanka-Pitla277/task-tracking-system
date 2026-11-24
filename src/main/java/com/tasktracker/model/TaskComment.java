package com.tasktracker.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.Instant;

@Entity
@Table(name="task_comments")
public class TaskComment {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @JsonIgnore @ManyToOne(optional=false) private Task task;
    @ManyToOne(optional=false) private User author;
    @Column(nullable=false,length=4000) private String content;
    private Instant createdAt=Instant.now();

    public Long getId(){return id;} public void setId(Long id){this.id=id;}
    public Task getTask(){return task;} public void setTask(Task task){this.task=task;}
    public User getAuthor(){return author;} public void setAuthor(User author){this.author=author;}
    public String getContent(){return content;} public void setContent(String content){this.content=content;}
    public Instant getCreatedAt(){return createdAt;} public void setCreatedAt(Instant createdAt){this.createdAt=createdAt;}
}
