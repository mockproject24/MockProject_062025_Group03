package com.group3.MockProject.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "warrant_result")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WarrantResult {
    @Id
    @Column(name = "warrant_result_id")
    private String warrantResultId;

    @Column(name = "police_response")
    private String policeResponse;

    @Column(name = "location")
    private String location;

    @Column(name = "notes", columnDefinition = "MEDIUMTEXT")
    private String notes;

    @Column(name = "time_active")
    private LocalDateTime timeActive;

    @Column(name = "is_deleted")
    private boolean isDeleted = false;

    @ManyToOne
    @JoinColumn(name = "warrant_id")
    private Warrant warrant;
} 