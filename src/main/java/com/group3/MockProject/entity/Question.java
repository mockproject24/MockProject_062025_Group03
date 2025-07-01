package com.group3.MockProject.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
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
@Data
@Table(name="question")
public class Question {
    @Id
    @Column(name="question_id")
    private String questionId;

    @NotNull
    @Column(columnDefinition = "TEXT", name="content")
    private String content;

    @NotNull
    @Column(columnDefinition = "MEDIUMTEXT", name = "answer")
    private String answer;

    @NotNull
    @Column(name="reliability")
    private String reliability;

    @NotNull
    @ColumnDefault("false")
    @Column(name="is_deleted")
    private Boolean idDeleted;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="created_by", referencedColumnName = "username")
    private User createdBy;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="interview_id", referencedColumnName = "interview_id")
    private Interview interview;

}
