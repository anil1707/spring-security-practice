package com.example.spring_security_demo.user;

import com.example.spring_security_demo.dto.LoginRequestDto;
import com.example.spring_security_demo.dto.UserResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/v1")
public class UserController {

    CustomUserDetailsService userService;

    public UserController(CustomUserDetailsService userService){
        this.userService = userService;
    }

    @PostMapping("/user")
    public ResponseEntity<UserDetails> user(@RequestBody LoginRequestDto req){
        UserDetails userData = userService.loadUserByUsername(req.getEmail());
        return ResponseEntity.ok(userData);
    }
}
