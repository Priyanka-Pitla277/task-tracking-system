package com.tasktracker.service;

import com.tasktracker.exception.ApiException;
import com.tasktracker.model.User;
import com.tasktracker.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class CurrentUserService {
    private final UserRepository users;
    public CurrentUserService(UserRepository users){this.users=users;}
    public User get(){
        var authentication=SecurityContextHolder.getContext().getAuthentication();
        if(authentication==null || !authentication.isAuthenticated()) throw new ApiException(HttpStatus.UNAUTHORIZED,"Authentication required");
        return users.findByEmail(authentication.getName()).orElseThrow(()->new ApiException(HttpStatus.UNAUTHORIZED,"User not found"));
    }
}
