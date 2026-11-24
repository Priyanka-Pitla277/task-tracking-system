package com.tasktracker.controller;

import com.tasktracker.dto.Dtos.*;
import com.tasktracker.model.Team;
import com.tasktracker.service.*;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/teams")
public class TeamController {
    private final TeamService teams; private final CurrentUserService current;
    public TeamController(TeamService teams,CurrentUserService current){this.teams=teams;this.current=current;}
    @PostMapping public ResponseEntity<Team> create(@Valid @RequestBody TeamRequest r){return ResponseEntity.status(HttpStatus.CREATED).body(teams.create(r,current.get()));}
    @GetMapping public List<Team> list(){return teams.myTeams(current.get());}
    @GetMapping("/{id}") public Team get(@PathVariable Long id){Team t=teams.get(id);teams.requireMember(t,current.get());return t;}
    @PostMapping("/{id}/members") public Team invite(@PathVariable Long id,@Valid @RequestBody InviteRequest r){return teams.invite(id,r,current.get());}
}
