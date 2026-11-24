package com.tasktracker.service;

import com.tasktracker.dto.Dtos.*;
import com.tasktracker.exception.ApiException;
import com.tasktracker.model.User;
import com.tasktracker.repository.UserRepository;
import com.tasktracker.security.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository users; private final PasswordEncoder encoder; private final JwtService jwt;
    public AuthService(UserRepository users,PasswordEncoder encoder,JwtService jwt){this.users=users;this.encoder=encoder;this.jwt=jwt;}
    public AuthResponse register(RegisterRequest r){
        if(users.existsByEmail(r.email().toLowerCase())) throw new ApiException(HttpStatus.CONFLICT,"Email already registered");
        User u=new User(); u.setName(r.name()); u.setEmail(r.email().toLowerCase()); u.setPassword(encoder.encode(r.password())); users.save(u);
        return new AuthResponse(jwt.generateToken(u.getEmail()),"Bearer",toResponse(u));
    }
    public AuthResponse login(LoginRequest r){
        User u=users.findByEmail(r.email().toLowerCase()).orElseThrow(()->new ApiException(HttpStatus.UNAUTHORIZED,"Invalid credentials"));
        if(!encoder.matches(r.password(),u.getPassword())) throw new ApiException(HttpStatus.UNAUTHORIZED,"Invalid credentials");
        return new AuthResponse(jwt.generateToken(u.getEmail()),"Bearer",toResponse(u));
    }
    public UserResponse toResponse(User u){return new UserResponse(u.getId(),u.getName(),u.getEmail(),u.getBio());}
}
