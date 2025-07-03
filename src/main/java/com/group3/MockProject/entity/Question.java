package com.group3.MockProject.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

/**
 * Question
 *
 * Question entity
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
@Table(name = "question")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Question {
    @Id
    @Column(name = "question_id")
    private String questionId;

    @Column(columnDefinition = "TEXT", name="content")
    private String content;

    @Column(columnDefinition = "TEXT", name = "answer")
    private String answer;

    @Column(name="reliability")
    private Float reliability;

    @ColumnDefault("false")
    @Column(name="is_deleted")
    private Boolean isDeleted = false;

    @ManyToOne
    @JoinColumn(name="interview_id", referencedColumnName = "interview_id")
    private Interview interview;

    @ManyToOne
    @JoinColumn(name="created_by", referencedColumnName = "username")
    private User user;
} 