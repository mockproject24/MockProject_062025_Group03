package com.group3.MockProject.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "inmate")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Inmate {
    @Id
    @Column(name = "inmate_id")
    private String inmateId;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "sentence_id")
//    private Sentence sentence;

    @Column(name = "fullname")
    private String fullname;

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
    private boolean isDeleted = false;
}