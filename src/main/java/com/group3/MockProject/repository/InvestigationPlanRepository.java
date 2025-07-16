package com.group3.MockProject.repository;

import com.group3.MockProject.dto.response.InvestigationPlanProjection;
import com.group3.MockProject.dto.response.InvestigationPlanResponse;
import com.group3.MockProject.entity.InvestigationPlan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * InvestigationPlanRepository
 * <p>
 * Provides business logic for managing  details.
 * <p>
 * Version 1.0
 * <p>
 * Date: 04/07/2025
 * <p>
 * Copyright
 * <p>
 * Modification Logs:
 * DATE        AUTHOR        DESCRIPTION
 * -------------------------------------------------------------
 * 04/07/2025        Nguyễn Bảo Kha        Create
 */

@Repository
public interface InvestigationPlanRepository extends JpaRepository<InvestigationPlan, String> {
    @Query("SELECT ip FROM InvestigationPlan ip WHERE ip.isDeleted = false")
    Page<InvestigationPlan> findAllActivePlans(Pageable pageable);

    @Query(value = """
    SELECT
        ip.investigation_plan_id AS investigationPlanId,
        c.case_id AS caseId,
        r.crime_type AS typeOfCrime,
        c.severity AS levelSeverity,
        r.incident_date AS date,
        r.reporter_fullname AS reporter,
        r.case_location AS location,
        ip.status AS status
    FROM investigations_plans ip
    JOIN cases c ON ip.case_id = c.case_id
    LEFT JOIN reports r ON r.case_id = c.case_id
    WHERE ip.is_deleted = FALSE
    """,
            countQuery = "SELECT COUNT(*) FROM investigations_plans ip WHERE ip.is_deleted = FALSE",
            nativeQuery = true)
    Page<InvestigationPlanProjection> findAllInvestigationPlans(Pageable pageable);
}
