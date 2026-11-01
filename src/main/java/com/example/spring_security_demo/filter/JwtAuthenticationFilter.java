package com.example.spring_security_demo.filter;

import com.example.spring_security_demo.auth.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.JwtException;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(
            JwtService jwtService,
            UserDetailsService userDetailsService
    ) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        // 1. If token is missing, continue.
        if (authHeader == null) {
            filterChain.doFilter(request, response);
            return;
        }

        // 2. Validate Authorization header format.
        if (!authHeader.startsWith("Bearer ") || authHeader.length() == 7) {
            sendUnauthorized(response);
            return;
        }

        String token = authHeader.substring(7);

        try {

            // 3. Extract username from JWT.
            String username = jwtService.extractUsername(token);

            // 4. Check whether user is already authenticated.
            if (username != null &&
                    SecurityContextHolder.getContext().getAuthentication() == null) {

                // 5. Load user from database.
                UserDetails userDetails =
                        userDetailsService.loadUserByUsername(username);

                // 6. Validate JWT.
                if (!jwtService.isTokenValid(token, userDetails)) {
                    sendUnauthorized(response);
                    return;
                }

                // 7. Create authenticated Authentication object.
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                authentication.setDetails(
                        new WebAuthenticationDetailsSource()
                                .buildDetails(request)
                );

                // 8. Store authentication in SecurityContextHolder.
                SecurityContextHolder.getContext()
                        .setAuthentication(authentication);
            }

        } catch (JwtException | IllegalArgumentException |
                 UsernameNotFoundException exception) {

            sendUnauthorized(response);
            return;
        }

        // 9. Continue filter chain.
        filterChain.doFilter(request, response);
    }

    private void sendUnauthorized(HttpServletResponse response)
            throws IOException {

        SecurityContextHolder.clearContext();

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");

        response.getWriter().write(
                "{\"message\":\"Invalid or expired token\"}"
        );
    }
}