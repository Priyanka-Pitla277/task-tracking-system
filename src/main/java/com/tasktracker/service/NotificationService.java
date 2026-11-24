package com.tasktracker.service;

import com.tasktracker.model.Notification;
import com.tasktracker.model.User;
import com.tasktracker.repository.NotificationRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class NotificationService {
    private final NotificationRepository repo;
    private final Map<Long,SseEmitter> emitters=new ConcurrentHashMap<>();
    public NotificationService(NotificationRepository repo){this.repo=repo;}
    public void notify(User recipient,String message,Long taskId){
        Notification n=new Notification(); n.setRecipient(recipient); n.setMessage(message); n.setTaskId(taskId); repo.save(n);
        SseEmitter emitter=emitters.get(recipient.getId());
        if(emitter!=null) try{emitter.send(SseEmitter.event().name("notification").data(Map.of("message",message,"taskId",taskId==null?0:taskId)));}catch(IOException e){emitters.remove(recipient.getId());}
    }
    public List<Notification> list(User recipient){return repo.findByRecipientOrderByCreatedAtDesc(recipient);}
    public SseEmitter stream(User recipient){SseEmitter emitter=new SseEmitter(30*60*1000L); emitters.put(recipient.getId(),emitter); emitter.onCompletion(()->emitters.remove(recipient.getId())); emitter.onTimeout(()->emitters.remove(recipient.getId())); return emitter;}
}
