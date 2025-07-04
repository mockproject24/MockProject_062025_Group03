package com.group3.MockProject.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.ColumnDefault;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "investigations_plans")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InvestigationPlan{

    @Id
    @Column(name = "investigation_plan_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    String investigationPlanId;

    @Column(name = "deadline_date")
    LocalDateTime deadlineDate;

    @Column(name = "result", columnDefinition = "MEDIUMTEXT")
    String result;

    @Column(name = "status")
    String status;

    @Column(name = "created_at")
    LocalDateTime createdAt;

    @Column(name = "plan_content", columnDefinition = "MEDIUMTEXT")
    String planContent;

    @Column(name = "is_deleted")
    @ColumnDefault("false")
    boolean isDeleted = false;
    
    @ManyToOne
    @JoinColumn(name = "created_officer_id")
    User createdOfficer;

    @ManyToOne
    @JoinColumn(name = "case_id")
    Case caseEntity;

    @OneToMany(mappedBy = "investigationPlan")
    List<Interview> interviews;
}
