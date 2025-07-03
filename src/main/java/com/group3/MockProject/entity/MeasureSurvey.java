package com.group3.MockProject.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "measure_survey")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MeasureSurvey {
    @Id
    @Column(name = "measure_survey_id")
    private String measureSurveyId;

    @Column(name = "type_name")
    private String typeName;

    @Column(name = "source")
    private String source;

    @Column(name = "result")
    private String result;

    @Column(name = "is_deleted")
    private boolean isDeleted = false;

    @ManyToOne
    @JoinColumn(name = "evidence_id")
    private Evidence evidence;
} 