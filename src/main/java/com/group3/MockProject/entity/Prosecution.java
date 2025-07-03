package com.group3.MockProject.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "prosecution")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Prosecution {
    @Id
    @Column(name = "prosecution_id")
    private String prosecutionId;

    @Column(name = "decision")
    private String decision;

    @Column(name = "decision_date")
    private LocalDateTime decisionDate;

    @Column(name = "reason")
    private String reason;

    @Column(name = "is_deleted")
    private boolean isDeleted = false;

    @ManyToOne
    @JoinColumn(name = "case_id")
    private Case caseEntity;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "prosecution")
    private List<Indictment> indictments;
}