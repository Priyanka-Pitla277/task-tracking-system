package com.tasktracker.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="teams")
public class Team {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @Column(nullable=false) private String name;
    private String description;
    @ManyToOne(optional=false) private User owner;
    private Instant createdAt=Instant.now();
    @JsonIgnore @OneToMany(mappedBy="team", cascade=jakarta.persistence.CascadeType.ALL, orphanRemoval=true)
    private Set<TeamMember> members=new HashSet<>();

    public Long getId(){return id;} public void setId(Long id){this.id=id;}
    public String getName(){return name;} public void setName(String name){this.name=name;}
    public String getDescription(){return description;} public void setDescription(String description){this.description=description;}
    public User getOwner(){return owner;} public void setOwner(User owner){this.owner=owner;}
    public Instant getCreatedAt(){return createdAt;} public void setCreatedAt(Instant createdAt){this.createdAt=createdAt;}
    public Set<TeamMember> getMembers(){return members;} public void setMembers(Set<TeamMember> members){this.members=members;}
}
