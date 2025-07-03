package com.group3.MockProject.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "suspect")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Suspect {

    @Id
    @Column(name = "suspect_id")
    private String suspectId;

    @Column(name = "fullname")
    private String fullname;

    @Column(name = "national")
    private String national;

    @Column(name = "gender")
    private String gender;

    @Column(name = "dob")
    private LocalDateTime dob;

    @Column(name = "identification")
    private String identification;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "description", columnDefinition = "MEDIUMTEXT")
    private String description;

    @Column(name = "address")
    private String address;

    @Column(name = "catch_time")
    private LocalDateTime catchTime;

    @Column(name = "notes", columnDefinition = "MEDIUMTEXT")
    private String notes;

    @Column(name = "status")
    private String status;

    @Column(name = "mugshot_url")
    private String mugshotUrl;

    @Column(name = "fingerprints_hash")
    private String fingerprintsHash;

    @Column(name = "health_status")
    private String healthStatus;

    @ManyToOne
    @JoinColumn(name = "case_id")
    private Case caseEntity;

    @OneToMany(mappedBy = "suspect")
    private List<Arrest> arrests;

    @ManyToOne
    @JoinColumn(name = "report_id")
    private Report report;

    @OneToMany(mappedBy = "suspect")
    private List<SuspectEvidence> suspectEvidences;
}
