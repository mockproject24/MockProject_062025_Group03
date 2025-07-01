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
@Table(name = "evidence")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Evidence {
    @Id
    @Column(name = "evidence_id", nullable = false)
    private String evidenceId;

    // --- FK sang các bảng khác (khi cần mapping quan hệ, bỏ comment @ManyToOne) ---
    @Column(name = "measure_survey_id", nullable = false)
    private Long measureSurveyId;
    // @ManyToOne(fetch = FetchType.LAZY)
    // @JoinColumn(name = "measure_survey_id", insertable = false, updatable = false)
    // private MeasureSurvey measureSurvey;

    @Column(name = "warrant_result_id", nullable = false)
    private Long warrantResultId;
    // @ManyToOne(fetch = FetchType.LAZY)
    // @JoinColumn(name = "warrant_result_id", insertable = false, updatable = false)
    // private WarrantResult warrantResult;

    @Column(name = "report_id", nullable = false)
    private Long reportId;
    // @ManyToOne(fetch = FetchType.LAZY)
    // @JoinColumn(name = "report_id", insertable = false, updatable = false)
    // private Report report;

    @Column(name = "collected_by", nullable = false)
    private Long collectedBy;
    // @ManyToOne(fetch = FetchType.LAZY)
    // @JoinColumn(name = "collected_by", insertable = false, updatable = false)
    // private User collector;

    @Column(name = "description", columnDefinition = "TEXT", nullable = false)
    private String description;

    @Column(name = "collected_at", nullable = false)
    private LocalDateTime collectedAt;

    @Column(name = "current_location", nullable = false)
    private String currentLocation;

    @Column(name = "attached_file", columnDefinition = "TEXT", nullable = true)
    private String attachedFile;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted;
}
