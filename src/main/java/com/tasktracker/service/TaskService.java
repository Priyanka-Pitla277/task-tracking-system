package com.tasktracker.service;

import com.tasktracker.dto.Dtos.*;
import com.tasktracker.exception.ApiException;
import com.tasktracker.model.*;
import com.tasktracker.repository.*;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Value;
import java.nio.file.*;
import java.util.*;

@Service
public class TaskService {
    private final TaskRepository tasks; private final TaskCommentRepository comments; private final AttachmentRepository attachments;
    private final TeamService teams; private final UserRepository users; private final NotificationService notifications;
    private final Path uploadDir;
    public TaskService(TaskRepository tasks,TaskCommentRepository comments,AttachmentRepository attachments,TeamService teams,UserRepository users,NotificationService notifications,@Value("${app.upload-dir}") String uploadDir){
        this.tasks=tasks;this.comments=comments;this.attachments=attachments;this.teams=teams;this.users=users;this.notifications=notifications;this.uploadDir=Paths.get(uploadDir);
    }
    @Transactional
    public Task create(Long teamId,TaskRequest r,User creator){
        Team team=teams.get(teamId);teams.requireMember(team,creator);
        Task task=new Task();task.setTeam(team);task.setCreator(creator);apply(task,r);tasks.save(task);
        if(task.getAssignee()!=null && !task.getAssignee().getId().equals(creator.getId())) notifications.notify(task.getAssignee(),"You were assigned task: "+task.getTitle(),task.getId());
        return task;
    }
    public List<Task> list(Long teamId,Long assigneeId,String status,String search,String sort){
        return tasks.findAll((root,query,b)->{List<Predicate> p=new ArrayList<>();
            if(teamId!=null)p.add(b.equal(root.get("team").get("id"),teamId));
            if(assigneeId!=null)p.add(b.equal(root.get("assignee").get("id"),assigneeId));
            if(status!=null)p.add(b.equal(root.get("status"),Task.Status.valueOf(status.toUpperCase())));
            if(search!=null&&!search.isBlank()){String like="%"+search.toLowerCase()+"%";p.add(b.or(b.like(b.lower(root.get("title")),like),b.like(b.lower(root.get("description")),like)));}
            return b.and(p.toArray(new Predicate[0]));
        },"dueDateAsc".equalsIgnoreCase(sort)?Sort.by(Sort.Direction.ASC,"dueDate"):Sort.by(Sort.Direction.DESC,"createdAt"));
    }
    public Task get(Long id,User user){Task task=tasks.findById(id).orElseThrow(()->new ApiException(HttpStatus.NOT_FOUND,"Task not found"));teams.requireMember(task.getTeam(),user);return task;}
    @Transactional
    public Task update(Long id,TaskRequest r,User user){Task task=get(id,user);apply(task,r);task.setUpdatedAt(java.time.Instant.now());tasks.save(task);notifyUpdate(task,"Task updated: "+task.getTitle());return task;}
    @Transactional
    public Task complete(Long id,User user){Task task=get(id,user);task.setStatus(Task.Status.COMPLETED);task.setUpdatedAt(java.time.Instant.now());tasks.save(task);notifyUpdate(task,"Task completed: "+task.getTitle());return task;}
    @Transactional
    public void delete(Long id,User user){Task task=get(id,user);tasks.delete(task);}
    private void apply(Task task,TaskRequest r){task.setTitle(r.title());task.setDescription(r.description());task.setDueDate(r.dueDate());
        if(r.status()!=null)task.setStatus(Task.Status.valueOf(r.status().toUpperCase()));
        if(r.priority()!=null)task.setPriority(Task.Priority.valueOf(r.priority().toUpperCase()));
        if(r.assigneeId()!=null){User assignee=users.findById(r.assigneeId()).orElseThrow(()->new ApiException(HttpStatus.NOT_FOUND,"Assignee not found"));teams.requireMember(task.getTeam(),assignee);task.setAssignee(assignee);}
    }
    private void notifyUpdate(Task task,String message){if(task.getAssignee()!=null)notifications.notify(task.getAssignee(),message,task.getId());}
    @Transactional
    public TaskComment comment(Long taskId,CommentRequest r,User user){Task task=get(taskId,user);TaskComment c=new TaskComment();c.setTask(task);c.setAuthor(user);c.setContent(r.content());comments.save(c);notifyUpdate(task,"New comment on task: "+task.getTitle());return c;}
    public List<TaskComment> comments(Long taskId,User user){Task task=get(taskId,user);return comments.findByTaskOrderByCreatedAtAsc(task);}
    @Transactional
    public Attachment upload(Long taskId,MultipartFile file,User user)throws Exception{Task task=get(taskId,user);Files.createDirectories(uploadDir);
        String original=file.getOriginalFilename()==null?"attachment":Paths.get(file.getOriginalFilename()).getFileName().toString();String stored=UUID.randomUUID()+"-"+original;Files.copy(file.getInputStream(),uploadDir.resolve(stored),StandardCopyOption.REPLACE_EXISTING);
        Attachment a=new Attachment();a.setTask(task);a.setUploader(user);a.setFileName(original);a.setStoredName(stored);a.setContentType(file.getContentType()==null?"application/octet-stream":file.getContentType());a.setSize(file.getSize());attachments.save(a);notifyUpdate(task,"Attachment added to task: "+task.getTitle());return a;
    }
    public List<Attachment> attachments(Long taskId,User user){Task task=get(taskId,user);return attachments.findByTask(task);}
    public Attachment getAttachment(Long attachmentId,User user){Attachment a=attachments.findById(attachmentId).orElseThrow(()->new ApiException(HttpStatus.NOT_FOUND,"Attachment not found"));get(a.getTask().getId(),user);return a;}
    public Path attachmentPath(Long attachmentId,User user){return uploadDir.resolve(getAttachment(attachmentId,user).getStoredName());}
}
