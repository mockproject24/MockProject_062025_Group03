package com.group3.MockProject.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "reports")
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Report {
    @Id
    @Column(name = "report_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    String reportId;

    @Column(name = "type_report")
    String typeReport;

    @Column(name = "severity")
    String severity;

    @Column(name = "incident_date")
    LocalDateTime incidentDate;

    @Column(name = "description", columnDefinition = "MEDIUMTEXT")
    String description;

    @Column(name = "case_location")
    String caseLocation;

    @Column(name = "reported_at")
    LocalDateTime reportedAt;

    @Column(name = "reporter_location")
    String reporterLocation;

    @Column(name = "reporter_fullname")
    String reporterFullname;

    @Column(name = "reporter_email")
    String reporterEmail;

    @Column(name = "reporter_phone_number")
    String reporterPhoneNumber;

    @Column(name = "is_deleted")
    @ColumnDefault("false")
    boolean isDeleted = false;

    @Column(name = "status")
    String status;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

    @ManyToOne
    @JoinColumn(name = "case_id")
    Case caseEntity;

    @OneToMany(mappedBy = "report")
    List<Evidence> evidences;

    @OneToMany(mappedBy = "report")
    List<Suspect> suspects;

    @OneToMany(mappedBy = "report")
    List<ReportsVictims> reportsVictims;

    @OneToMany(mappedBy = "report")
    List<ReportsVictims> reportsWitnesses;
}
