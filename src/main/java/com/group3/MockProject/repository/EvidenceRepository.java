package com.group3.MockProject.repository;


import com.group3.MockProject.entity.Case;
import com.group3.MockProject.entity.Evidence;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * EvidenceRepository
 * <p>
 * Provides business logic for managing employment details.
 * <p>
 * Version 1.0
 * Date: 7/4/2025
 * <p>
 * Copyright
 * <p>
 * Modification Logs:
 * DATE         AUTHOR       DESCRIPTION
 * -------------------------------------
 * 7/4/2025      Phuong Dong      Create
 */

@Repository
public interface EvidenceRepository extends JpaRepository<Evidence, String> {
    @EntityGraph(
            attributePaths = {
                    "user",
                    "report",
                    "warrant",
                    "digitalInvest",
                    "financialInvest",
                    "physicalInvest",
                    "forensicInvest",
                    "recordInfos",
                    "measureSurveys"
            },
            type = EntityGraph.EntityGraphType.LOAD
    )
    List<Evidence> findByCaseEntity(Case caseEntity);

}