package com.tasktracker.controller;

import com.tasktracker.model.Notification;
import com.tasktracker.service.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {
    private final NotificationService notifications; private final CurrentUserService current;
    public NotificationController(NotificationService notifications,CurrentUserService current){this.notifications=notifications;this.current=current;}
    @GetMapping public List<Notification> list(){return notifications.list(current.get());}
    @GetMapping(value="/stream",produces="text/event-stream") public SseEmitter stream(){return notifications.stream(current.get());}
}
