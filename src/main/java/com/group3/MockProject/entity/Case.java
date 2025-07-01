package com.group3.MockProject.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "case")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Case {
    @Id
    @Column(name = "case_id", nullable = false)
    private String caseId;

    @Column(name = "case_name", nullable = false)
    private String caseName;

    @Column(name = "type_case", nullable = true)
    private String typeCase;
    // nếu sau này muốn mapping enum hoặc lookup table:
    // @Enumerated(EnumType.STRING)
    // private CaseType typeCase;

    @Column(name = "severity", nullable = false)
    private Integer severity;

    @Column(name = "status", nullable = false)
    private String status;
    // tương tự có thể dùng enum:
    // @Enumerated(EnumType.STRING)
    // private CaseStatus status;

    @Column(name = "summary", columnDefinition = "TEXT", nullable = true)
    private String summary;

    @Column(name = "create_at", nullable = false)
    private LocalDateTime createAt;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted;

}
