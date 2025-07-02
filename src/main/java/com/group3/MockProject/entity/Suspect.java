package com.group3.MockProject.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "suspect")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Suspect {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "suspect_id", length = 36, updatable = false, nullable = false)
    String suspectId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "case_id", referencedColumnName = "id", nullable = false)
    Case caseEntity; // Foreign key đến Case

    @Column(name = "fullname", length = 255, nullable = false)
    String fullname;

    @Column(name = "national", length = 100)
    String national;

    @Column(name = "gender", length = 10)
    String gender;

    @Column(name = "dob")
    LocalDate dob;

    @Column(name = "identification", length = 50)
    String identification;

    @Column(name = "phonenumber", length = 20)
    String phonenumber;

    @Column(name = "description", columnDefinition = "TEXT")
    String description;

    @Column(name = "address", length = 255)
    String address;

    @Column(name = "catch_time")
    LocalDateTime catchTime;

    @Column(name = "notes", columnDefinition = "TEXT")
    String notes;

    @Column(name = "status", length = 50)
    String status;

    @Column(name = "mugshot_url", length = 255)
    String mugshotUrl;

    @Column(name = "fingerprints_hash", length = 255)
    String fingerprintsHash;

    @Column(name = "health_status", length = 100)
    String healthStatus;

    @Column(name = "is_deleted", nullable = false)
    Boolean isDeleted;

    @PrePersist
    protected void onCreate() {
        this.isDeleted = false;
    }
}
