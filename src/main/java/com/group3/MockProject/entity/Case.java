package com.group3.MockProject.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "case")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Case {

    @Id
    @Column(name = "case_id", length = 36)
    private String caseId;

    @Column(name = "case_number", length = 50, unique = true)
    private String caseNumber;

    @Column(name = "type_case", length = 100)
    private String typeCase;

    @Column(name = "severity", length = 50)
    private String severity;

    @Column(name = "status", length = 50)
    private String status;

    @Column(name = "summary", columnDefinition = "TEXT")
    private String summary;

    @Column(name = "create_at")
    private LocalDateTime createdAt;

    @Column(name = "is_deleted")
    private Boolean isDeleted = false;
}
