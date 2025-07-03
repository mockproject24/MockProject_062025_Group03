package com.group3.MockProject.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "sentences")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Sentence {
    @Id
    @Column(name = "sentence_id")
    private String sentenceId;

    @Column(name = "sentence_type")
    private String sentenceType;

    @Column(name = "duration")
    private String duration;

    @Column(name = "sentence_condition")
    private String sentenceCondition;

    @Column(name = "sentencing_date")
    private LocalDateTime sentencingDate;

    @Column(name = "is_deleted")
    private boolean isDeleted = false;

    @ManyToOne
    @JoinColumn(name = "case_id")
    private Case caseEntity;

    @ManyToOne
    @JoinColumn(name = "case_result_id")
    private CaseResult caseResult;
}