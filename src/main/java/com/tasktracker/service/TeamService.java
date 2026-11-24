package com.tasktracker.service;

import com.tasktracker.dto.Dtos.*;
import com.tasktracker.exception.ApiException;
import com.tasktracker.model.*;
import com.tasktracker.repository.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class TeamService {
    private final TeamRepository teams; private final TeamMemberRepository members; private final UserRepository users;
    public TeamService(TeamRepository teams,TeamMemberRepository members,UserRepository users){this.teams=teams;this.members=members;this.users=users;}
    @Transactional
    public Team create(TeamRequest r,User owner){Team team=new Team();team.setName(r.name());team.setDescription(r.description());team.setOwner(owner);teams.save(team);TeamMember member=new TeamMember();member.setTeam(team);member.setUser(owner);member.setRole(TeamMember.Role.OWNER);members.save(member);return team;}
    public List<Team> myTeams(User user){return members.findByUser(user).stream().map(TeamMember::getTeam).toList();}
    public Team get(Long id){return teams.findById(id).orElseThrow(()->new ApiException(HttpStatus.NOT_FOUND,"Team not found"));}
    @Transactional
    public Team invite(Long teamId,InviteRequest r,User requester){
        Team team=get(teamId); var requesterMember=members.findByTeamAndUser(team,requester).orElseThrow(()->new ApiException(HttpStatus.FORBIDDEN,"Not a team member"));
        if(requesterMember.getRole()==TeamMember.Role.MEMBER) throw new ApiException(HttpStatus.FORBIDDEN,"Only owners or admins may invite users");
        User user=users.findByEmail(r.email().toLowerCase()).orElseThrow(()->new ApiException(HttpStatus.NOT_FOUND,"User not found"));
        if(members.findByTeamAndUser(team,user).isPresent()) throw new ApiException(HttpStatus.CONFLICT,"User is already a member");
        TeamMember member=new TeamMember();member.setTeam(team);member.setUser(user);member.setRole(TeamMember.Role.MEMBER);members.save(member);return team;
    }
    public void requireMember(Team team,User user){members.findByTeamAndUser(team,user).orElseThrow(()->new ApiException(HttpStatus.FORBIDDEN,"Not a team member"));}
}
