package com.group3.MockProject.repository;

import com.group3.MockProject.entity.Case;
import com.group3.MockProject.entity.Evidence;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EvidentRepository extends JpaRepository<Evidence, String> {
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
