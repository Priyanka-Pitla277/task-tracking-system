package com.tasktracker.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="team_members", uniqueConstraints=@UniqueConstraint(columnNames={"team_id","user_id"}))
public class TeamMember {
    public enum Role { OWNER, ADMIN, MEMBER }
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @JsonIgnore @ManyToOne(optional=false) private Team team;
    @ManyToOne(optional=false) private User user;
    @Enumerated(EnumType.STRING) @Column(nullable=false) private Role role;

    public Long getId(){return id;} public void setId(Long id){this.id=id;}
    public Team getTeam(){return team;} public void setTeam(Team team){this.team=team;}
    public User getUser(){return user;} public void setUser(User user){this.user=user;}
    public Role getRole(){return role;} public void setRole(Role role){this.role=role;}
}
