package com.group3.MockProject.entity;

import com.group3.MockProject.constant.GenderType;
import com.group3.MockProject.constant.UserStatus;
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

    @Column(name = "password_hash")
    String passwordHash;

    @Column(name = "full_name")
    String fullName;

    @Column(name = "avatar_url")
    String avatarUrl;

    @Column(name = "phone_number")
    String phoneNumber;

    @Column(name = "gender")
    GenderType gender;

    @Column(name = "dob")
    LocalDateTime dob;

    @Column(name = "date_attended")
    LocalDateTime dateAttended;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    UserStatus status;

    @Column(name = "create_at")
    LocalDateTime createAt;

    @Column(name = "refresh_token")
    String refreshToken;

    @Column(name = "is_deleted")
    @ColumnDefault("false")
    boolean isDeleted = false;

    @OneToMany(mappedBy = "user")
    List<Report> reports;

    @OneToMany(mappedBy = "userInterviewer")
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

    @OneToMany(mappedBy = "user")
    List<Warrant> warrants;
}
