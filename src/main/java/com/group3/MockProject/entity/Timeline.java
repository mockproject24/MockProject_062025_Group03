package com.group3.MockProject.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Table(name = "timeline")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Timeline {
    @Id
    @Column(name = "timeline_id", nullable = false)
    private String timelineId;

    // --- foreign key sang CASE_RESULT (nếu cần mapping quan hệ, sẽ dùng @ManyToOne) ---
    @Column(name = "case_result_id", nullable = false)
    private Long caseResultId;
    // @ManyToOne(fetch = FetchType.LAZY)
    // @JoinColumn(name = "case_result_id", insertable = false, updatable = false)
    // private CaseResult caseResult;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "end_time", nullable = true)
    private LocalDateTime endTime;

    @Column(name = "attached_file", columnDefinition = "TEXT", nullable = true)
    private String attachedFile;

    @Column(name = "notes", columnDefinition = "TEXT", nullable = true)
    private String notes;

    @Column(name = "activity", nullable = false)
    private String activity;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted;
}
