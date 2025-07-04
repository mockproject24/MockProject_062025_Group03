package com.group3.MockProject.repository;

import com.group3.MockProject.entity.InvestigationPlan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * InvestigationPlanRepository
 *
 * Provides business logic for managing  details.
 *
 * Version 1.0
 *
 * Date: 04/07/2025
 *
 * Copyright
 *
 * Modification Logs:
 * DATE        AUTHOR        DESCRIPTION
 * -------------------------------------------------------------
 * 04/07/2025        Nguyễn Bảo Kha        Create
 */

@Repository
public interface InvestigationPlanRepository extends JpaRepository<InvestigationPlan, String> {
    @Query("SELECT ip FROM InvestigationPlan ip WHERE ip.isDeleted = false")
    Page<InvestigationPlan> findAllActivePlans(Pageable pageable);
}
