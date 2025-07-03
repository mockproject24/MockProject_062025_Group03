package com.group3.MockProject.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "investigation_plan")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InvestigationPlan implements Serializable {

    @Id
    @Column(name = "investigation_plan_id")
    private String investigationPlanId;

    @Column(name = "created_officer_id", insertable = false, updatable = false)
    private String createdOfficerId;

    @ManyToOne
    @JoinColumn(name = "created_officer_id")
    private User createdOfficer;

    @Column(name = "case_id", insertable = false, updatable = false)
    private String caseId;

    @ManyToOne
    @JoinColumn(name = "case_id")
    private Case caseEntity;

    @Column(name = "deadline_date", nullable = false)
    private LocalDateTime deadlineDate;

    @Column(name = "result", columnDefinition = "TEXT", nullable = false)
    private String result;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "plan_content", columnDefinition = "TEXT", nullable = false)
    private String planContent;

    @Column(name = "is_deleted", columnDefinition = "boolean default false")
    private Boolean isDeleted;

    @OneToMany(mappedBy = "investigationPlan")
    private List<Interview> interviews;
}
