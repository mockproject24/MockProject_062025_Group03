package com.group3.MockProject.repository;

import com.group3.MockProject.entity.MeasureSurvey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MeasureSurveyRepository extends JpaRepository<MeasureSurvey, String> {
}
