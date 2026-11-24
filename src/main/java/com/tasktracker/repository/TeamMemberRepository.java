package com.tasktracker.repository;
import com.tasktracker.model.TeamMember;
import com.tasktracker.model.Team;
import com.tasktracker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
public interface TeamMemberRepository extends JpaRepository<TeamMember,Long>{ Optional<TeamMember> findByTeamAndUser(Team team,User user); List<TeamMember> findByTeam(Team team); List<TeamMember> findByUser(User user); }
