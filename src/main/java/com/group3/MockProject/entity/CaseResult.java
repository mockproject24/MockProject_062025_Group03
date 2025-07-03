package com.group3.MockProject.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "case_result")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CaseResult {

    @Id
    @Column(name = "case_result_id")
    private String caseResultId;

    @Column(name = "report_time")
    private LocalDateTime reportTime;

    @Column(name = "report_analyst")
    private String reportAnalyst;

    @Column(name = "summary", columnDefinition = "MEDIUMTEXT")
    private String summary;

    @Column(name = "identify_motive")
    private String identifyMotive;

    @Column(name = "status")
    private String status;

    @Column(name = "is_deleted")
    private boolean isDeleted = false;

    @OneToMany(mappedBy = "caseResult")
    private List<Timeline> timelines;

    @ManyToOne
    @JoinColumn(name = "case_id")
    private Case caseEntity;

    @OneToMany(mappedBy = "caseResult")
    private List<Sentence> sentences;
}