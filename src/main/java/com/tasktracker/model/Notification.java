package com.tasktracker.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.Instant;

@Entity
@Table(name="notifications")
public class Notification {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @JsonIgnore @ManyToOne(optional=false) private User recipient;
    @Column(nullable=false) private String message;
    private Long taskId;
    private boolean read;
    private Instant createdAt=Instant.now();

    public Long getId(){return id;} public void setId(Long id){this.id=id;}
    public User getRecipient(){return recipient;} public void setRecipient(User recipient){this.recipient=recipient;}
    public String getMessage(){return message;} public void setMessage(String message){this.message=message;}
    public Long getTaskId(){return taskId;} public void setTaskId(Long taskId){this.taskId=taskId;}
    public boolean isRead(){return read;} public void setRead(boolean read){this.read=read;}
    public Instant getCreatedAt(){return createdAt;} public void setCreatedAt(Instant createdAt){this.createdAt=createdAt;}
}
