package com.group3.MockProject.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name = "measures_surveys")
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MeasureSurvey {
    @Id
    @Column(name = "measure_survey_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    String measureSurveyId;

    @Column(name = "type_name")
    String typeName;

    @Column(name = "source")
    String source;

    @Column(name = "result")
    String result;

    @Column(name = "is_deleted")
    @ColumnDefault("false")
    boolean isDeleted = false;

    @ManyToOne
    @JoinColumn(name = "evidence_id")
    Evidence evidence;
} 