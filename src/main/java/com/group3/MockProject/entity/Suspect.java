package com.group3.MockProject.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "suspects")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Suspect {

    @Id
    @Column(name = "suspect_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    String suspectId;

    @Column(name = "fullname")
    String fullname;

    @Column(name = "national")
    String national;

    @Column(name = "gender")
    String gender;

    @Column(name = "dob")
    LocalDateTime dob;

    @Column(name = "identification")
    String identification;

    @Column(name = "phone_number")
    String phoneNumber;

    @Column(name = "description", columnDefinition = "MEDIUMTEXT")
    String description;

    @Column(name = "address")
    String address;

    @Column(name = "catch_time")
    LocalDateTime catchTime;

    @Column(name = "notes", columnDefinition = "MEDIUMTEXT")
    String notes;

    @Column(name = "status")
    String status;

    @Column(name = "mugshot_url")
    String mugshotUrl;

    @Column(name = "fingerprints_hash")
    String fingerprintsHash;

    @Column(name = "health_status")
    String healthStatus;

    @ManyToOne
    @JoinColumn(name = "case_id")
    Case caseEntity;

    @OneToMany(mappedBy = "suspect")
    List<Arrest> arrests;

    @ManyToOne
    @JoinColumn(name = "report_id")
    Report report;

    @OneToMany(mappedBy = "suspect")
    List<SuspectEvidence> suspectEvidences;
}
