package com.tasktracker.repository;
import com.tasktracker.model.Attachment;
import com.tasktracker.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface AttachmentRepository extends JpaRepository<Attachment,Long>{ List<Attachment> findByTask(Task task); }
