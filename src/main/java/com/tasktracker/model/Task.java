package com.tasktracker.model;

import jakarta.persistence.*;
import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name="tasks")
public class Task {
    public enum Status { OPEN, IN_PROGRESS, COMPLETED, CANCELLED }
    public enum Priority { LOW, MEDIUM, HIGH, URGENT }
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @Column(nullable=false) private String title;
    @Column(length=4000) private String description;
    @Enumerated(EnumType.STRING) @Column(nullable=false) private Status status=Status.OPEN;
    @Enumerated(EnumType.STRING) private Priority priority=Priority.MEDIUM;
    private LocalDate dueDate;
    @ManyToOne(optional=false) private User creator;
    @ManyToOne private User assignee;
    @ManyToOne(optional=false) private Team team;
    private Instant createdAt=Instant.now();
    private Instant updatedAt=Instant.now();

    public Long getId(){return id;} public void setId(Long id){this.id=id;}
    public String getTitle(){return title;} public void setTitle(String title){this.title=title;}
    public String getDescription(){return description;} public void setDescription(String description){this.description=description;}
    public Status getStatus(){return status;} public void setStatus(Status status){this.status=status;}
    public Priority getPriority(){return priority;} public void setPriority(Priority priority){this.priority=priority;}
    public LocalDate getDueDate(){return dueDate;} public void setDueDate(LocalDate dueDate){this.dueDate=dueDate;}
    public User getCreator(){return creator;} public void setCreator(User creator){this.creator=creator;}
    public User getAssignee(){return assignee;} public void setAssignee(User assignee){this.assignee=assignee;}
    public Team getTeam(){return team;} public void setTeam(Team team){this.team=team;}
    public Instant getCreatedAt(){return createdAt;} public void setCreatedAt(Instant createdAt){this.createdAt=createdAt;}
    public Instant getUpdatedAt(){return updatedAt;} public void setUpdatedAt(Instant updatedAt){this.updatedAt=updatedAt;}
}
