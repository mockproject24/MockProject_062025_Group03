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
@Table(name = "warrant")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Warrant {
    @Id
    @Column(name = "warrant_id", nullable = false)
    private String warrantId;

    // --- khóa ngoại sang CASE (nếu cần mapping quan hệ, bỏ comment @ManyToOne) ---
    @Column(name = "case_id", nullable = false)
    private Long caseId;
    // @ManyToOne(fetch = FetchType.LAZY)
    // @JoinColumn(name = "case_id", insertable = false, updatable = false)
    // private CaseEntity caseEntity;

    @Column(name = "warrant_name", nullable = false)
    private String warrantName;

    @Column(name = "attached_file", columnDefinition = "TEXT", nullable = true)
    private String attachedFile;

    @Column(name = "time_publish", nullable = false)
    private LocalDateTime timePublish;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;

}
