package com.group3.MockProject.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "cases")
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Case {

    @Id
    @Column(name = "case_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    String caseId;

    @Column(name = "case_number")
    Integer caseNumber;

    @Column(name = "type_case")
    String typeCase;

    @Column(name = "severity")
    String severity;

    @Column(name = "status")
    String status;

    @Column(name = "summary", columnDefinition = "TEXT")
    String summary;

    @Column(name = "create_at")
    LocalDateTime createAt;

    @Column(name = "is_deleted")
    @ColumnDefault("false")
    boolean isDeleted = false;

    @OneToMany(mappedBy = "caseEntity")
    List<CaseResult> caseResults;

    @OneToMany(mappedBy = "caseEntity")
    List<Warrant> warrants;

    @OneToMany(mappedBy = "caseEntity")
    List<Report> reports;

    @OneToMany(mappedBy = "caseEntity")
    List<Victim> victims;

//    @OneToMany(mappedBy = "caseEntity")
//    List<Suspect> suspects;

    @OneToMany(mappedBy = "caseEntity")
    List<Witness> witnesses;

    @OneToMany(mappedBy = "caseEntity")
    List<InvestigationPlan> investigationPlans;

    @OneToMany(mappedBy = "caseEntity")
    List<Evidence> evidences;

    @OneToMany(mappedBy = "caseEntity")
    List<Arrest> arrests;

    @OneToMany(mappedBy = "caseEntity")
    List<Prosecution> prosecutions;

//    @OneToMany(mappedBy = "caseEntity")
//    List<Sentence> sentences;

    @OneToMany(mappedBy = "caseEntity")
    List<UsersCases> usersCases;

    @OneToMany(mappedBy = "caseEntity")
    List<CaseEvidence> casesEvidences;
}