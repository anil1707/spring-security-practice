package com.example.spring_security_demo.auth;

import com.example.spring_security_demo.dto.LoginRequestDto;
import com.example.spring_security_demo.dto.RegisterUserRequestDto;
import com.example.spring_security_demo.dto.RegisterUserResponseDto;
import com.example.spring_security_demo.user.CustomUserDetails;
import com.example.spring_security_demo.user.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private PasswordEncoder passwordEncoder;
    public AuthService(
            UserRepository userRepository,
            JwtService jwtService,
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder
    ){
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
    }

    public RegisterUserResponseDto register(RegisterUserRequestDto req){
        String email = req.getEmail();
        boolean isPresent = userRepository.existsByEmailIgnoreCase(email);
        if(isPresent){
            throw new IllegalArgumentException(
                    "Email is already registered"
            );
        }

        CustomUserDetails user = new CustomUserDetails();

        user.setEmail(email);
        user.setName(req.getName());
        user.setPassword(passwordEncoder.encode(req.getPassword()));

        userRepository.save(user);
        RegisterUserResponseDto response = new RegisterUserResponseDto();
        response.setEmail(email);
        response.setName(req.getName());

        return response;
    }

    public String login(LoginRequestDto request){
        System.out.println(request.getEmail());
        System.out.println(request.getPassword());
        String username = request.getEmail();
        String password = request.getPassword();

        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(username, password)
                );

        // Step 2: Get authenticated user.
        UserDetails userDetails =
                (UserDetails) authentication.getPrincipal();

        // Step 3: Generate JWT.
        return jwtService.generateToken(userDetails);
    }
}
