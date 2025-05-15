package com.example.CGC.Database;

import com.example.CGC.Schemas.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface UserDatabase extends JpaRepository<User, UUID> {
    User findFirstByEmail(String email);
    User findFirstByEmailAndPassword(String email, String password);
    @Query(value = "SELECT * FROM users WHERE id = :userId", nativeQuery = true)
    User getUserById(UUID userId);
}
