package com.example.spring_security_demo.auth;

import com.example.spring_security_demo.dto.LoginRequestDto;
import com.example.spring_security_demo.dto.RegisterUserRequestDto;
import com.example.spring_security_demo.dto.RegisterUserResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService){
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterUserResponseDto> register(@RequestBody RegisterUserRequestDto request){
        RegisterUserResponseDto response = authService.register(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequestDto request){
        String response  = authService.login(request);
        return ResponseEntity.ok(response);
    }

}
