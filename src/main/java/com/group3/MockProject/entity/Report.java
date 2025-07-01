package com.group3.MockProject.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Table(name = "report")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String reportId;

    String caseId; // Foreign key to Case

    String typeReport;

    @Column(columnDefinition = "TEXT")
    String description;

    String caseLocation;

    LocalDateTime reportedAt;

    String reporterFullname;

    String reporterEmail;

    String reporterPhonenumber;

    String officerApproveId; // Foreign key to User/Officer

    Boolean isDeleted;

    @PrePersist
    protected void onCreate() {
        this.isDeleted = false;
        if (this.reportedAt == null) {
            this.reportedAt = LocalDateTime.now();
        }
    }
}
