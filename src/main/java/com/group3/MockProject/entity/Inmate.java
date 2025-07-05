package com.group3.MockProject.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;

@Entity
@Table(name = "inmates")
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Inmate {
    @Id
    @Column(name = "inmate_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    String inmateId;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "sentence_id")
//    private Sentence sentence;

    @Column(name = "fullname")
    String fullname;

    @Column(name = "assigned_facility")
    String assignedFacility;

    @Column(name = "start_date")
    LocalDateTime startDate;

    @Column(name = "expected_release")
    LocalDateTime expectedRelease;

    @Column(name = "health_status")
    String healthStatus;

    @Column(name = "status")
    String status;

    @Column(name = "is_deleted")
    @ColumnDefault("false")
    boolean isDeleted = false;
}