package com.tasktracker.security;

import com.tasktracker.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService; private final UserRepository users;
    public JwtAuthenticationFilter(JwtService jwtService,UserRepository users){this.jwtService=jwtService;this.users=users;}
    @Override
    protected void doFilterInternal(HttpServletRequest request,HttpServletResponse response,FilterChain chain)throws ServletException,IOException{
        String header=request.getHeader("Authorization");
        if(header!=null && header.startsWith("Bearer ")){
            try{String token=header.substring(7); if(jwtService.isRevoked(token)) throw new IllegalArgumentException("Token revoked"); String email=jwtService.extractEmail(token); users.findByEmail(email).ifPresent(user->{
                var auth=new UsernamePasswordAuthenticationToken(email,null,List.of()); auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request)); SecurityContextHolder.getContext().setAuthentication(auth);
            });}catch(Exception ignored){SecurityContextHolder.clearContext();}
        }
        chain.doFilter(request,response);
    }
}
