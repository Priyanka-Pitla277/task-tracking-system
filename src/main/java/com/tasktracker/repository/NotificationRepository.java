package com.tasktracker.repository;
import com.tasktracker.model.Notification;
import com.tasktracker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface NotificationRepository extends JpaRepository<Notification,Long>{ List<Notification> findByRecipientOrderByCreatedAtDesc(User recipient); }
