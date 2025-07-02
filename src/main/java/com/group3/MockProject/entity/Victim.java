package com.group3.MockProject.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "victim")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Victim {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "victim_id", length = 36, updatable = false, nullable = false)
    String victimId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "case_id", referencedColumnName = "id", nullable = false)
    Case caseEntity; // Foreign key đến Case

    @Column(name = "fullname", length = 255, nullable = false)
    String fullname;

    @Column(name = "contact", length = 100)
    String contact;

    @Column(name = "injurie", columnDefinition = "TEXT")
    String injurie;

    @Column(name = "status", length = 50)
    String status;

    @Column(name = "is_deleted", nullable = false)
    Boolean isDeleted;

    @PrePersist
    protected void onCreate() {
        this.isDeleted = false;
    }
}
