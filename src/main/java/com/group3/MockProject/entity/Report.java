package com.group3.MockProject.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "report")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Report implements Serializable {

    @Id
    @Column(name = "report_id")
    private String reportId;

    @Column(name = "case_id", nullable = false)
    private String caseId;

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "report", columnDefinition = "TEXT", nullable = false)
    private String report;

    @Column(name = "description", columnDefinition = "TEXT", nullable = false)
    private String description;

    @Column(name = "case_location", nullable = false)
    private String caseLocation;

    @Column(name = "reported_at", nullable = false)
    private LocalDateTime reportedAt;

    @Column(name = "reporter_fullname", nullable = false)
    private String reporterFullname;

    @Column(name = "reporter_email", nullable = false)
    private String reporterEmail;

    @Column(name = "reporter_phonenumber", nullable = false)
    private String reporterPhonenumber;

    @Column(name = "officer_approve_id", nullable = false)
    private String officerApproveId;

    @Column(name = "is_deleted", columnDefinition = "boolean default false")
    private Boolean isDeleted;

    @Override
    public String toString() {
        return "Report{" +
                "reportId=" + reportId +
                ", caseId=" + caseId +
                ", type='" + type + '\'' +
                ", report='" + report + '\'' +
                ", description='" + description + '\'' +
                ", caseLocation='" + caseLocation + '\'' +
                ", reportedAt=" + reportedAt +
                ", reporterFullname='" + reporterFullname + '\'' +
                ", reporterEmail='" + reporterEmail + '\'' +
                ", reporterPhonenumber='" + reporterPhonenumber + '\'' +
                ", officerApproveId=" + officerApproveId +
                ", isDeleted=" + isDeleted +
                '}';
    }
}
