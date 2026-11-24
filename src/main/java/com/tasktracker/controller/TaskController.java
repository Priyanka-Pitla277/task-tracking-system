package com.tasktracker.controller;

import com.tasktracker.dto.Dtos.*;
import com.tasktracker.model.*;
import com.tasktracker.service.*;
import jakarta.validation.Valid;
import org.springframework.core.io.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.nio.file.Files;
import java.util.*;

@RestController
@RequestMapping("/api")
public class TaskController {
    private final TaskService tasks; private final CurrentUserService current;
    public TaskController(TaskService tasks,CurrentUserService current){this.tasks=tasks;this.current=current;}
    @PostMapping("/teams/{teamId}/tasks") public ResponseEntity<Task> create(@PathVariable Long teamId,@Valid @RequestBody TaskRequest r){return ResponseEntity.status(HttpStatus.CREATED).body(tasks.create(teamId,r,current.get()));}
    @GetMapping("/tasks") public List<Task> list(@RequestParam(required=false) Long teamId,@RequestParam(required=false) Long assigneeId,@RequestParam(required=false) String status,@RequestParam(required=false) String search,@RequestParam(required=false) String sort){return tasks.list(teamId,assigneeId,status,search,sort);}
    @GetMapping("/tasks/assigned-to-me") public List<Task> assigned(){return tasks.list(null,current.get().getId(),null,null,null);}
    @GetMapping("/tasks/{id}") public Task get(@PathVariable Long id){return tasks.get(id,current.get());}
    @PutMapping("/tasks/{id}") public Task update(@PathVariable Long id,@Valid @RequestBody TaskRequest r){return tasks.update(id,r,current.get());}
    @PatchMapping("/tasks/{id}/complete") public Task complete(@PathVariable Long id){return tasks.complete(id,current.get());}
    @DeleteMapping("/tasks/{id}") public ResponseEntity<?> delete(@PathVariable Long id){tasks.delete(id,current.get());return ResponseEntity.noContent().build();}
    @PostMapping("/tasks/{id}/comments") public ResponseEntity<TaskComment> comment(@PathVariable Long id,@Valid @RequestBody CommentRequest r){return ResponseEntity.status(HttpStatus.CREATED).body(tasks.comment(id,r,current.get()));}
    @GetMapping("/tasks/{id}/comments") public List<TaskComment> comments(@PathVariable Long id){return tasks.comments(id,current.get());}
    @PostMapping("/tasks/{id}/attachments") public ResponseEntity<Attachment> upload(@PathVariable Long id,@RequestParam("file") MultipartFile file)throws Exception{return ResponseEntity.status(HttpStatus.CREATED).body(tasks.upload(id,file,current.get()));}
    @GetMapping("/tasks/{id}/attachments") public List<Attachment> attachments(@PathVariable Long id){return tasks.attachments(id,current.get());}
    @GetMapping("/attachments/{attachmentId}/download") public ResponseEntity<Resource> download(@PathVariable Long attachmentId)throws Exception{
        Attachment attachment=tasks.getAttachment(attachmentId,current.get());
        Resource resource=new PathResource(tasks.attachmentPath(attachmentId,current.get()));
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=\""+attachment.getFileName()+"\"").contentType(MediaType.parseMediaType(attachment.getContentType())).body(resource);
    }
    @PostMapping("/ai/tasks/generate-description") public Map<String,String> generateDescription(@Valid @RequestBody AiRequest r){
        String prompt=r.prompt().trim(); String summary=prompt.length()>500?prompt.substring(0,497)+"...":prompt;
        return Map.of("description","Objective: "+summary+"\n\nSteps:\n1. Review the requirements.\n2. Implement and test the solution.\n3. Document the outcome.");
    }
}
