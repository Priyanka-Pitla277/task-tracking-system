package com.tasktracker.repository;
import com.tasktracker.model.Task;
import com.tasktracker.model.TaskComment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface TaskCommentRepository extends JpaRepository<TaskComment,Long>{ List<TaskComment> findByTaskOrderByCreatedAtAsc(Task task); }
