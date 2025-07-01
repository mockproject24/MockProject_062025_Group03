package com.group3.MockProject.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Arrest class
 * <p>
 * Provides business logic for managing details.
 * <p>
 * Version 1.0
 * <p>
 * Date: 01/07/2025
 * <p>
 * Copyright
 * <p>
 * Modification Logs:
 * DATE        AUTHOR        DESCRIPTION
 * -------------------------------------------------------------
 * 01/07/2025        Nguyễn Bảo Kha        Create
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "arrest")
public class Arrest {
    @Id
    private String suspectId;

    @Column(nullable = false)
    private String caseId;

    @Column(nullable = false)
    private String officerId;

    @Column(name = "suspect_miranda_signature",nullable = false)
    private String suspectMirandaSignature;

    @Column(name = "arrest_start_time", nullable = false)
    private LocalDateTime arrestStartTime;

    @Column(name = "arrest_end_time", nullable = false)
    private LocalDateTime arrestEndTime;

    @Column(name = "is_deleted")
    private boolean isDeleted = false;
}
