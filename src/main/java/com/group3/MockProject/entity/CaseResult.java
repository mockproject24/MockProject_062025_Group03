package com.group3.MockProject.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "case_result")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CaseResult {

    @Id
    @Column(name = "case_result_id", length = 36)
    private String caseResultId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "case_id", referencedColumnName = "case_id")
    private Case case_id;

    @Column(name = "report_time")
    private LocalDateTime reportTime;

    @Column(name = "report_analyst", length = 255)
    private String reportAnalyst;

    @Column(name = "summary", columnDefinition = "TEXT")
    private String summary;

    @Column(name = "identify_motive", columnDefinition = "TEXT")
    private String identifyMotive;

    @Column(name = "status", length = 50)
    private String status;

    @Column(name = "is_deleted")
    private Boolean isDeleted = false;
}