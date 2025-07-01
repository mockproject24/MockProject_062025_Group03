package com.group3.MockProject.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "inmate")
public class Inmate {
    @Id
    @Column(name = "inmate_id", nullable = false)
    private String inmateId;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "sentence_id")
//    private Sentence sentence;

    @Column(name = "fullname")
    private String fullName;

    @Column(name = "assigned_facility")
    private String assignedFacility;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "expected_release")
    private LocalDateTime expectedRelease;

    @Column(name = "health_status")
    private String healthStatus;

    @Column(name = "status")
    private String status;

    @Column(name = "is_deleted")
    private Boolean isDeleted = false;
}