package com.group3.MockProject.entity;

import com.group3.MockProject.constant.CrimeType;
import com.group3.MockProject.constant.ReportStatus;
import com.group3.MockProject.constant.SeverityType;
import com.group3.MockProject.enums.ReporterIncidentRelationship;
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

    @Column(name = "crime_type")
    @Enumerated(EnumType.STRING)
    CrimeType crimeType;

    @Column(name = "severity")
    @Enumerated(EnumType.STRING)
    SeverityType severity;

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

//    @Column(name = "reporter_incident_relationship")
//    ReporterIncidentRelationshipType reporterIncidentRelationship;

    @Column(name = "is_deleted")
    @ColumnDefault("false")
    boolean isDeleted = false;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    ReportStatus status;

    @Column(name="reporter_incident_relationship")
    @Enumerated(EnumType.STRING)
    ReporterIncidentRelationship reporterIncidentRelationship;

    @ManyToOne
    @JoinColumn(name = "username")
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
