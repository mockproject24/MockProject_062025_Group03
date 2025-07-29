package com.group3.MockProject.validator;

import com.group3.MockProject.exception.AppException;
import com.group3.MockProject.exception.ErrorCode;
import com.group3.MockProject.repository.CaseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * StatementValidator
 * <p>
 * Validation logic for statement operations
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
@Component
@RequiredArgsConstructor
@Slf4j
public class StatementValidator {
    private final CaseRepository caseRepository;

    /**
     * Validates case ID and statement ID parameters
     *
     * @param caseId The case identifier to validate
     * @param statementId The statement identifier to validate
     * @throws AppException if validation fails
     */
    public void validateGetStatementParams(String caseId, String statementId) {
        log.debug("Validating parameters - caseId: {}, statementId: {}", caseId, statementId);

        validateCaseId(caseId);
        validateStatementId(statementId);
        validateCaseExists(caseId);
    }

    /**
     * Validates case ID parameter
     *
     * @param caseId The case identifier to validate
     * @throws AppException if case ID is invalid
     */
    private void validateCaseId(String caseId) {
        if (caseId == null || caseId.trim().isEmpty()) {
            log.error("Case ID is null or empty");
            throw new AppException(ErrorCode.INVALID_KEY, "Case ID is required");
        }
    }

    /**
     * Validates statement ID parameter
     *
     * @param statementId The statement identifier to validate
     * @throws AppException if statement ID is invalid
     */
    private void validateStatementId(String statementId) {
        if (statementId == null || statementId.trim().isEmpty()) {
            log.error("Statement ID is null or empty");
            throw new AppException(ErrorCode.INVALID_KEY, "Statement ID is required");
        }
    }

    /**
     * Validates that case exists in database
     *
     * @param caseId The case identifier to check
     * @throws AppException if case does not exist
     */
    private void validateCaseExists(String caseId) {
        boolean caseExists = caseRepository.existsById(caseId);
        if (!caseExists) {
            log.error("Case not found with ID: {}", caseId);
            throw new AppException(ErrorCode.CASE_NOT_EXISTED);
        }
        log.debug("Case validation passed for ID: {}", caseId);
    }
}
