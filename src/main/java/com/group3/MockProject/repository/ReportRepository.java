package com.group3.MockProject.repository;

import com.group3.MockProject.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReportRepository extends JpaRepository<Report, String> {

    Optional<Object> findFirstByCaseEntity_CaseIdAndIsDeletedFalse(String caseId);
}
