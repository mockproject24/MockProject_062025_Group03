package com.group3.MockProject.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "report")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Report {
    @Id
    @Column(name = "report_id")
    private String reportId;

    @Column(name = "type_report")
    private String typeReport;

    @Column(name = "severity")
    private String severity;

    @Column(name = "incident_date")
    private LocalDateTime incidentDate;

    @Column(name = "description", columnDefinition = "MEDIUMTEXT")
    private String description;

    @Column(name = "case_location")
    private String caseLocation;

    @Column(name = "reported_at")
    private LocalDateTime reportedAt;

    @Column(name = "reporter_location")
    private String reporterLocation;

    @Column(name = "reporter_fullname")
    private String reporterFullname;

    @Column(name = "reporter_email")
    private String reporterEmail;

    @Column(name = "reporter_phone_number")
    private String reporterPhoneNumber;

    @Column(name = "is_deleted")
    private boolean isDeleted = false;

    @Column(name = "status")
    private String status;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "case_id")
    private Case caseEntity;

    @OneToMany(mappedBy = "report")
    private List<Evidence> evidences;

    @OneToMany(mappedBy = "report")
    private List<Suspect> suspects;

    @OneToMany(mappedBy = "report")
    private List<ReportsVictims> reportsVictims;
}
