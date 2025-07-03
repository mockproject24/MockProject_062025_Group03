package com.group3.MockProject.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;

@Entity
@Table(name = "sentences")
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Sentence {
    @Id
    @Column(name = "sentence_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    String sentenceId;

    @Column(name = "sentence_type")
    String sentenceType;

    @Column(name = "duration")
    String duration;

    @Column(name = "condition")
    String condition;

    @Column(name = "sentencing_date")
    LocalDateTime sentencingDate;

    @Column(name = "is_deleted")
    @ColumnDefault("false")
    boolean isDeleted = false;

//    @ManyToOne
//    @JoinColumn(name = "case_id")
//    Case caseEntity;

    @ManyToOne
    @JoinColumn(name = "case_result_id")
    CaseResult caseResult;
}