package com.group3.MockProject.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;
import java.util.List;

/**
 * User
 * <p>
 * User entity
 * <p>
 * Version 1.0
 * Date: 01/07/2025
 * <p>
 * Copyright
 * <p>
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
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
    @Id
    @Column(name = "username")
    String username;

    @Column(name = "password_hash", nullable = false)
    String passwordHash;

    @Column(name = "fullname", nullable = false)
    String fullname;

    @Column(name = "avatar_url")
    String avatarUrl;

    @Column(name = "email")
    String email;

    @Column(name = "phone_number")
    String phoneNumber;

    @Column(name = "create_at", nullable=false)
    LocalDateTime createAt;

    @Column(name = "is_deleted")
    @ColumnDefault("false")
    boolean isDeleted = false;

    @OneToMany(mappedBy = "user")
    List<Report> reports;

    @OneToMany(mappedBy = "interviewer")
    List<Interview> interviews;

    @OneToMany(mappedBy = "createdOfficer")
    List<InvestigationPlan> investigationPlans;

    @OneToMany(mappedBy = "user")
    List<Evidence> evidences;

    @OneToMany(mappedBy = "user")
    List<ProsecutionsUser> prosecutionsUsers;

    @ManyToOne(fetch =  FetchType.LAZY)
    @JoinColumn(name = "role_id")
    Role role;

    @OneToMany(mappedBy = "user")
    List<UsersCases> usersCases;

    @OneToMany(mappedBy = "user")
    List<Question> questions;
}
