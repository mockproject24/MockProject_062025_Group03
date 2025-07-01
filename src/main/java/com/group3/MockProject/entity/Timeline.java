package com.group3.MockProject.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Timeline class
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
@Table(name = "timeline")
public class Timeline {
    @Id
    private String timeline_id;

    @Column(name = "start_time",nullable = false)
    private LocalDateTime startTime;

    @Column(name = "end_time",nullable = false)
    private LocalDateTime endTime;

    @Column(name = "attached_file",nullable = false)
    private String attachedFile;

    @Column(nullable = false)
    private String notes;

    @Column(nullable = false)
    private String activity;

    @Column(name = "is_deleted")
    private boolean isDeleted = false;
}
