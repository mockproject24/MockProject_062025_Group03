package com.group3.MockProject.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
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

@Entity
@Table(name = "arrest")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Arrest {
    @Id
    @Column(name = "arrest_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private String arrestId;

    @Column(name = "suspect_miranda_signature")
    private String suspectMirandaSignature;

    @Column(name = "arrest_start_time", nullable = false)
    private LocalDateTime arrestStartTime;

    @Column(name = "arrest_end_time")
    private LocalDateTime arrestEndTime;

    @Column(name = "is_deleted")
    private boolean isDeleted = false;

    @ManyToOne
    @JoinColumn(name = "case_id")
    private Case caseEntity;

    @ManyToOne
    @JoinColumn(name = "suspect_id")
    private Suspect suspect;
}
