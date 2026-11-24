package com.tasktracker.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public class Dtos {
    public record RegisterRequest(@NotBlank String name,@NotBlank @Email String email,@Size(min=8,max=72) String password){}
    public record LoginRequest(@NotBlank @Email String email,@NotBlank String password){}
    public record AuthResponse(String token,String tokenType,UserResponse user){}
    public record UserResponse(Long id,String name,String email,String bio){}
    public record UpdateProfileRequest(@NotBlank String name,String bio){}
    public record TeamRequest(@NotBlank String name,String description){}
    public record InviteRequest(@NotBlank @Email String email){}
    public record TaskRequest(@NotBlank String title,String description,String status,String priority,LocalDate dueDate,Long assigneeId){}
    public record CommentRequest(@NotBlank @Size(max=4000) String content){}
    public record AiRequest(@NotBlank @Size(max=2000) String prompt){}
}
