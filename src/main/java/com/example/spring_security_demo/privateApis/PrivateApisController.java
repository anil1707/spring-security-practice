package com.example.spring_security_demo.privateApis;

import com.example.spring_security_demo.dto.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/private")
public class PrivateApisController {

    @GetMapping("/greet")
    public ResponseEntity<ApiResponse<String >> greet(){
        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        ApiResponse<String> response = new ApiResponse<>(true, "date fetched successfully", "Hello from "+ authentication.getName());
        return ResponseEntity.ok().body(response);
    }

}
