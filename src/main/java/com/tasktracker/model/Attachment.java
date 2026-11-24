package com.tasktracker.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.Instant;

@Entity
@Table(name="attachments")
public class Attachment {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @JsonIgnore @ManyToOne(optional=false) private Task task;
    @ManyToOne(optional=false) private User uploader;
    @Column(nullable=false) private String fileName;
    @Column(nullable=false) private String storedName;
    @Column(nullable=false) private String contentType;
    private long size;
    private Instant uploadedAt=Instant.now();

    public Long getId(){return id;} public void setId(Long id){this.id=id;}
    public Task getTask(){return task;} public void setTask(Task task){this.task=task;}
    public User getUploader(){return uploader;} public void setUploader(User uploader){this.uploader=uploader;}
    public String getFileName(){return fileName;} public void setFileName(String fileName){this.fileName=fileName;}
    public String getStoredName(){return storedName;} public void setStoredName(String storedName){this.storedName=storedName;}
    public String getContentType(){return contentType;} public void setContentType(String contentType){this.contentType=contentType;}
    public long getSize(){return size;} public void setSize(long size){this.size=size;}
    public Instant getUploadedAt(){return uploadedAt;} public void setUploadedAt(Instant uploadedAt){this.uploadedAt=uploadedAt;}
}
