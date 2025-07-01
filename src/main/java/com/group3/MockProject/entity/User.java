package com.group3.MockProject.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class User {
    @Id
    String username;

    @Column(name = "password_hash", nullable = false)
    String passwordHash;

    String fullname;

    String avatarUrl;

    @Column(unique = true)
    String email;

    String phonenumber;

    @Column(name = "create_at")
    LocalDateTime createAt;

    String roleId; // Foreign key to Role

    @Column(name = "is_deleted")
    Boolean isDeleted;

    @PrePersist
    protected void onCreate() {
        this.createAt = LocalDateTime.now();
        this.isDeleted = false;
    }
}
