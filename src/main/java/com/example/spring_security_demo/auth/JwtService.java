package com.example.spring_security_demo.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    // Generate signing key
    public SecretKey getSigningKey(){
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

//    Generate token
    public String generateToken(UserDetails userDetails){
        Date issueAt = new Date();

        Date expireAt = new Date(issueAt.getTime() + expiration);

        String token = Jwts.builder()
                .subject(userDetails.getUsername())
                .issuedAt(issueAt)
                .signWith(getSigningKey())
                .expiration(expireAt)
                .compact();

        System.out.println("Token: " + token);

        return token;
    }

//    extract all claims
    public Claims extractAllClaims(String token) {

        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    //extract user name
    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    public Date extractExpiration(String token) {
        return extractAllClaims(token).getExpiration();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {

        try {

            Claims claims = extractAllClaims(token);

            String username = claims.getSubject();

            Date expiration = claims.getExpiration();

            return username != null
                    && username.equals(userDetails.getUsername())
                    && expiration != null
                    && expiration.after(new Date())
                    && userDetails.isEnabled()
                    && userDetails.isAccountNonExpired()
                    && userDetails.isAccountNonLocked()
                    && userDetails.isCredentialsNonExpired();

        } catch (Exception exception) {

            return false;
        }
    }


}



