package com.tasktracker.controller;

import com.tasktracker.dto.Dtos.*;
import com.tasktracker.model.User;
import com.tasktracker.repository.UserRepository;
import com.tasktracker.service.*;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class ProfileController {
    private final CurrentUserService current; private final AuthService auth; private final UserRepository users;
    public ProfileController(CurrentUserService current,AuthService auth,UserRepository users){this.current=current;this.auth=auth;this.users=users;}
    @GetMapping("/me") public UserResponse me(){return auth.toResponse(current.get());}
    @PutMapping("/me") public UserResponse update(@Valid @RequestBody UpdateProfileRequest r){User u=current.get();u.setName(r.name());u.setBio(r.bio());users.save(u);return auth.toResponse(u);}
}
