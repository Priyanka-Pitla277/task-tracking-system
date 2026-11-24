package com.tasktracker.repository;
import com.tasktracker.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;
public interface TeamRepository extends JpaRepository<Team,Long>{}
