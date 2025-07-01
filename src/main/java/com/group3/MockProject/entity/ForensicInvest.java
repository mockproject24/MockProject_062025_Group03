package com.group3.MockProject.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "forensic_invest")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ForensicInvest {
    @Id
    @Column(name = "evidence_id")
    private String evidenceId;

    @Column(name = "lab_name")
    private String labName;

    @Column(name = "report", columnDefinition = "TEXT")
    private String report;

    @Column(name = "result_summary", columnDefinition = "TEXT")
    private String resultSummary;

    @Column(name = "received_at")
    private LocalDateTime receivedAt;

    @Column(name = "is_deleted")
    private Boolean isDeleted = false;
}