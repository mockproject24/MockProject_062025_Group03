package com.group3.MockProject.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Table(name = "warrant_result")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class WarrantResult {
    @Id
    @Column(name = "warrant_result_id", nullable = false)
    private String warrantResultId;

    // --- foreign key sang WARRANT (nếu cần mapping quan hệ, bỏ comment @ManyToOne) ---
    @Column(name = "warrant_id", nullable = false)
    private Long warrantId;
    // @ManyToOne(fetch = FetchType.LAZY)
    // @JoinColumn(name = "warrant_id", insertable = false, updatable = false)
    // private Warrant warrant;

    @Column(name = "police_response", nullable = false)
    private String policeResponse;

    @Column(name = "location", nullable = false)
    private String location;

    @Column(name = "notes", columnDefinition = "TEXT", nullable = true)
    private String notes;

    @Column(name = "time_active", nullable = false)
    private LocalDateTime timeActive;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;
}
