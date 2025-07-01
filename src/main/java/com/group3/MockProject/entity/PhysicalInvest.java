package com.group3.MockProject.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "physical_invest")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class PhysicalInvest {
    @Id
    @Column(name = "evidence_id", nullable = false)
    private String evidenceId;

    // --- foreign key sang bảng khác (nếu cần mapping quan hệ, bỏ comment @ManyToOne) ---
    // @Column(name = "case_id", nullable = false)
    // private Long caseId;     // FK
    // @ManyToOne(fetch = FetchType.LAZY)
    // @JoinColumn(name = "case_id", insertable = false, updatable = false)
    // private CaseEntity caseEntity;

    @Column(name = "image_url", columnDefinition = "TEXT", nullable = true)
    private String imageUrl;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;
}
