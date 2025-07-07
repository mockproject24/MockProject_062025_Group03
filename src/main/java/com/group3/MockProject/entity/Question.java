package com.group3.MockProject.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.ColumnDefault;

/**
 * Question
 * <p>
 * Question entity
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
@Table(name = "question")
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Question {
    @Id
    @Column(name = "question_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    String questionId;

    @Column(columnDefinition = "MEDIUMTEXT")
    String content;

    @Column(columnDefinition = "MEDIUMTEXT")
    String answer;

    @Column(name = "reliability")
    Float reliability; // Same as "levelOfTrust" ("A": 1.0, "B": 0.7, "C": 0.4)

    @Column(name = "is_deleted")
    @ColumnDefault("false")
    boolean isDeleted = false;

    @ManyToOne
    @JoinColumn(name = "interview_id")
    Interview interview;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;
} 