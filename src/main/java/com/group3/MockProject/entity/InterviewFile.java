package com.group3.MockProject.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * InterviewFile
 * <p>
 * Provides business logic for managing employment details.
 * <p>
 * Version 1.0
 * Date: 7/10/2025
 * <p>
 * Copyright
 * <p>
 * Modification Logs:
 * DATE         AUTHOR       DESCRIPTION
 * -------------------------------------
 * 7/10/2025      User      Create
 */
@Entity
@Table(name = "interview_files")
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InterviewFile {
    @Id
    @Column(name = "interview_file_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    String interviewFileId;

    @Column(name = "attached_file", columnDefinition = "MEDIUMTEXT")
    String attachedFile;

    @Column(name = "is_deleted")
    @ColumnDefault("false")
    boolean isDeleted = false;

    @ManyToOne
    @JoinColumn(name = "interview_id")
    Interview interview;

    @Column(name = "create_at")
    @CreationTimestamp
    LocalDateTime createAt;
}
