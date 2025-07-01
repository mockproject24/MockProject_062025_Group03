package com.group3.MockProject.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Table(name = "suspect")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class Suspect {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String suspectId;

    String caseId; // Foreign key to Case

    String fullname;

    String national;

    String gender;

    LocalDate dob;

    String identification;

    String phonenumber;

    @Column(columnDefinition = "TEXT")
    String description;

    String address;

    LocalDateTime catchTime;

    @Column(columnDefinition = "TEXT")
    String notes;

    String status;

    String mugshotUrl;

    String fingerprintsHash;

    String healthStatus;

    Boolean isDeleted;

    @PrePersist
    protected void onCreate() {
        this.isDeleted = false;
    }
}
