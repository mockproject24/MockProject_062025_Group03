package com.group3.MockProject.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "case")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Case {

    @Id
    @Column(name = "case_id")
    private String caseId;

    @Column(name = "case_number")
    private Integer caseNumber;

    @Column(name = "type_case")
    private String typeCase;

    @Column(name = "severity")
    private String severity;

    @Column(name = "status")
    private String status;

    @Column(name = "summary")
    private String summary;

    @Column(name = "create_at")
    private LocalDateTime createAt;

    @Column(name = "is_deleted")
    private boolean isDeleted = false;

    @OneToMany(mappedBy = "caseEntity")
    private List<CaseResult> caseResults;

    @OneToMany(mappedBy = "caseEntity")
    private List<Warrant> warrants;

    @OneToMany(mappedBy = "caseEntity")
    private List<Report> reports;

    @OneToMany(mappedBy = "caseEntity")
    private List<Victim> victims;

    @OneToMany(mappedBy = "caseEntity")
    private List<Suspect> suspects;

    @OneToMany(mappedBy = "caseEntity")
    private List<Witness> witnesses;

    @OneToMany(mappedBy = "caseEntity")
    private List<InvestigationPlan> investigationPlans;

    @OneToMany(mappedBy = "caseEntity")
    private List<Envidency> evidences;

    @OneToMany(mappedBy = "caseEntity")
    private List<Arrest> arrests;

    @OneToMany(mappedBy = "caseEntity")
    private List<Prosecution> prosecutions;

    @OneToMany(mappedBy = "caseEntity")
    private List<Sentence> sentences;

    @OneToMany(mappedBy = "caseEntity")
    private List<CaseEnvidence> casesEvidences;
}