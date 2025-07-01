package com.group3.MockProject.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Table(name = "victim")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class Victim {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String victimId;

    String caseId; // Foreign key to Case

    String fullname;

    String contact;

    @Column(columnDefinition = "TEXT")
    String injurie;

    String status;

    Boolean isDeleted;

    @PrePersist
    protected void onCreate() {
        this.isDeleted = false;
    }
}

