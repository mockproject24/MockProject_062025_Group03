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
@Table(name = "case_result")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class CaseResult {
    @Id
    @Column(name = "case_result_id", nullable = false)
    private String caseResultId;

    // --- foreign key sang CASE (nếu cần mapping quan hệ, sẽ dùng @ManyToOne) ---
    @Column(name = "case_id", nullable = false)
    private String caseId;
    // @ManyToOne(fetch = FetchType.LAZY)
    // @JoinColumn(name = "case_id", insertable = false, updatable = false)
    // private Case parentCase;

    @Column(name = "report_time", nullable = false)
    private LocalDateTime reportTime;

    @Column(name = "report_analyst", nullable = false)
    private String reportAnalyst;

    @Column(name = "summary", columnDefinition = "TEXT", nullable = false)
    private String summary;

    @Column(name = "identify_motive", nullable = true)
    private String identifyMotive;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted;

}
