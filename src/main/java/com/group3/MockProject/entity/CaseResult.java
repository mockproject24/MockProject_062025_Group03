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
@Table(name = "cases_results")
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CaseResult {

    @Id
    @Column(name = "case_result_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    String caseResultId;

    @Column(name = "report_time")
    LocalDateTime reportTime;

    @Column(name = "report_analyst")
    String reportAnalyst;

    @Column(name = "summary", columnDefinition = "TEXT")
    String summary;

    @Column(name = "identify_motive")
    String identifyMotive;

    @Column(name = "status")
    String status;

    @Column(name = "is_deleted")
    @ColumnDefault("false")
    boolean isDeleted = false;

    @OneToMany(mappedBy = "caseResult")
    List<Timeline> timelines;

    @ManyToOne
    @JoinColumn(name = "case_id")
    Case caseEntity;

    @OneToMany(mappedBy = "caseResult")
    List<Sentence> sentences;
}