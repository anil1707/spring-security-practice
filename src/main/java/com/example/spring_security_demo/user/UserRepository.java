package com.example.spring_security_demo.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<CustomUserDetails, Long> {
    Optional<CustomUserDetails> findByEmailIgnoreCase(String email);

    boolean existsByEmailIgnoreCase(String email);
}
