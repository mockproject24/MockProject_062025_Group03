package com.group3.MockProject.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "sentence")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Sentence {
    
    @Id
    @Column(name = "sentence_id", length = 50)
    private String sentenceId;
    
    @Column(name = "case_id", length = 50, nullable = false)
    private String caseId;
    
    @Column(name = "case_result_id", length = 50)
    private String caseResultId;
    
    @Column(name = "sentence_type", length = 100)
    private String sentenceType;
    
    @Column(name = "duration")
    private Integer duration; // Thời gian thi hành án (tháng)
    
    @Column(name = "condition", length = 1000)
    private String condition;
    
    @Column(name = "sentencing_date")
    private LocalDate sentencingDate;
    
    @Column(name = "is_deleted", nullable = false)
    @Builder.Default
    private Boolean isDeleted = false;
    
    // Quan hệ với CASES (n-1)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "case_id", insertable = false, updatable = false)
    private Case caseEntity;
    
    // Quan hệ với CASE_RESULTS (n-1)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "case_result_id", insertable = false, updatable = false)
    private CaseResult caseResult;
    
    // Quan hệ với INMATES (1-n)
    @OneToMany(mappedBy = "sentenceId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Inmate> inmates;
}