package com.group3.MockProject.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Table(name = "warrant")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class Warrant {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String warrantId;

    String caseId; // Foreign key to Case

    String warrantName;

    String attachedFile;

    LocalDateTime timePublish;

    Boolean isDeleted;

    @PrePersist
    protected void onCreate() {
        this.isDeleted = false;
        if (this.timePublish == null) {
            this.timePublish = LocalDateTime.now();
        }
    }
}
