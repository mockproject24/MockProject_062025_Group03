package com.group3.MockProject.entity;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
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

@Entity
@Table(name = "timelines")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Timeline {
    @Id
    @Column(name = "timeline_id")
    private String timelineId;

    @Column(name = "case_result_id")
    private String caseResultId;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Column(name = "attached_file", columnDefinition = "json")
    private String[] attachedFile;

    @Column(name = "notes")
    private String notes;

    @Column(name = "activity")
    private String activity;

    @Column(name = "is_deleted")
    private boolean isDeleted = false;

    @ManyToOne
    @JoinColumn(name = "case_result_id", insertable = false, updatable = false)
    private CaseResult caseResult;
}
