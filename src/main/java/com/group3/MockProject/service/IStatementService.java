package com.group3.MockProject.service;

import com.group3.MockProject.dto.response.StatementResponse;
import com.group3.MockProject.exception.AppException;

/**
 * IStatementService
 * <p>
 * Service interface for statement operations
 * <p>
 * Version 1.0
 * Date: 7/23/2025
 * <p>
 * Copyright
 * <p>
 * Modification Logs:
 * DATE         AUTHOR       DESCRIPTION
 * -------------------------------------
 * 7/23/2025      FongFox      Create
 */
public interface IStatementService {
    /**
     * Retrieves a statement by case ID and statement ID
     *
     * @param caseId The case identifier
     * @param statementId The statement identifier
     * @return StatementResponse containing statement details
     * @throws AppException if case or statement not found
     */
    StatementResponse getStatementByCaseIdAndStatementId(String caseId, String statementId);
}
