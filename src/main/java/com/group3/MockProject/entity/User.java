package com.group3.MockProject.entity;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
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
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @Column(name = "username")
    private String username;

    @Column(name = "password_hash")
    private String passwordHash;

    @Column(name = "fullname")
    private String fullname;

    @Column(name = "avatar_url", nullable = true)
    private String avatarUrl;

    @Column(name = "email")
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "create_at")
    private LocalDateTime createAt;

    @ColumnDefault("false")
    @Column(name = "is_deleted")
    private Boolean isDeleted = false;

    @OneToMany(mappedBy = "user")
    private List<Report> reports;

    @OneToMany(mappedBy = "interviewer")
    private List<Interview> interviews;

    @OneToMany(mappedBy = "createdOfficer")
    private List<InvestigationPlan> investigationPlans;

    @OneToMany(mappedBy = "user")
    private List<Envidency> evidences;

    @OneToMany(mappedBy = "user")
    private List<Prosecution> prosecutions;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;
}
