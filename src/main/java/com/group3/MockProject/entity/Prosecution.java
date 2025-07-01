package com.group3.MockProject.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "prosecution")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Prosecution {
    
    @Id
    @Column(name = "prosecution_id", length = 50)
    private String prosecutionId;
    
    @Column(name = "case_id", length = 50, nullable = false)
    private String caseId;
    
    @Column(name = "prosecutor_id", length = 50, nullable = false)
    private String prosecutorId;
    
    @Column(name = "decision", length = 500)
    private String decision;
    
    @Column(name = "decision_date")
    private LocalDate decisionDate;
    
    @Column(name = "reason", length = 1000)
    private String reason;
    
    @Column(name = "is_deleted", nullable = false)
    @Builder.Default
    private Boolean isDeleted = false;
    
    // Quan hệ với INDICTMENTS (1-n)
    @OneToMany(mappedBy = "prosecutionId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Indictment> indictments;
    
    // Quan hệ với CASES (n-1)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "case_id", insertable = false, updatable = false)
    private Case caseEntity;
    
    // Quan hệ với USERS (n-1) - Prosecutor
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prosecutor_id", insertable = false, updatable = false)
    private User prosecutor;
}