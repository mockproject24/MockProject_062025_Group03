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
@Table(name = "warrants_results")
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WarrantResult {
    @Id
    @Column(name = "warrant_result_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    String warrantResultId;

    @Column(name = "police_response", columnDefinition = "MEDIUMTEXT")
    String policeResponse;

    @Column(name = "location")
    String location;

    @Column(name = "notes", columnDefinition = "MEDIUMTEXT")
    String notes;

    @Column(name = "time_active")
    LocalDateTime timeActive;

    @Column(name = "is_deleted")
    @ColumnDefault("false")
    boolean isDeleted = false;

    @ManyToOne
    @JoinColumn(name = "warrant_id")
    Warrant warrant;
} 