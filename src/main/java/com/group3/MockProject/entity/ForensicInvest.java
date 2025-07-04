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
@Table(name = "forensics_invests")
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ForensicInvest {
    @Id
    @Column(name = "evidence_id")
    String evidenceId;

    @Column(name = "lab_name")
    String labName;

    @Column(name = "report")
    String report;

    @Column(name = "received_at")
    LocalDateTime receivedAt;

    @Column(name = "is_deleted")
    @ColumnDefault("false")
    boolean isDeleted = false;

    @OneToOne
    @JoinColumn(name = "evidence_id")
    Evidence evidence;
}