package com.group3.MockProject.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;

@Entity
@Data
public class Question {
    @Id

    private String questionId;

    @NotNull
    @Column(columnDefinition = "TEXT")
    private String content;

    @NotNull
    @Column(columnDefinition = "MEDIUMTEXT")
    private String answer;

    @NotNull
    private String reliability;

    @NotNull
    @ColumnDefault("false")
    private Boolean is_deleted;

    // n-1
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="created_by", referencedColumnName = "username")
    private User createdBy;

    // n-1
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="interview_id", referencedColumnName = "interview_id")
    private Interview interview;

}
