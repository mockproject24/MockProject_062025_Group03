package com.group3.MockProject.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;

/**
 * User
 *
 * User entity
 *
 * Version 1.0
 * Date: 01/07/2025
 *
 * Copyright
 *
 * Modification Logs:
 * DATE         AUTHOR       DESCRIPTION
 * -------------------------------------
 * 01/07/2025     DQMinh      Create
 */


@Entity
@Data
@Table(name="user")

public class User {
    @Id
    @Column(name="username", nullable = false)
    private String username;

    @NotNull
    @Column(name="password_hash")
    private String passwordHash;

    @NotNull
    @Column(name="fullname")
    private String fullname;

    @Column(name="avatar_url",  nullable = true)
    private String avatarUrl;

    @Column(name="email",  nullable = true)
    private String email;

    @NotNull
    @Column(name="phonenumber")
    private String phonenumber;

    @NotNull
    @Column(name="create_at")
    private LocalDateTime createAt;

    @NotNull
    @ColumnDefault("false")
    private Boolean isDeleted;

    // foreign key to role
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name="role_id", referencedColumnName = "role_id")
//    private Role role;
}
