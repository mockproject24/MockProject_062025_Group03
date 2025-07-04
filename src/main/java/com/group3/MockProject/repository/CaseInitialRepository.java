package com.group3.MockProject.repository;

import com.group3.MockProject.entity.Case;
import com.group3.MockProject.entity.SceneResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * CaseInitialRepository
 * <p>
 * Provides CRUD operations for save case's initial response.
 * <p>
 * Version 1.0
 * Date: 04-Jul-25
 * <p>
 * Copyright
 * <p>
 * Modification Logs:
 * DATE         AUTHOR       DESCRIPTION
 * -------------------------------------
 * 04-Jul-25      Hoang Tran      Create
 */
@Repository
public interface CaseInitialRepository extends JpaRepository<Case, String> {
    
    /**
     * Find case by caseId and not deleted
     * @param caseId the case ID
     * @return Optional of Case
     */
    Optional<Case> findByCaseIdAndIsDeletedFalse(String caseId);
}
