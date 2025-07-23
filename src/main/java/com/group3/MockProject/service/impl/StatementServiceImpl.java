package com.group3.MockProject.service.impl;

import com.group3.MockProject.dto.response.StatementResponse;
import com.group3.MockProject.entity.Statement;
import com.group3.MockProject.exception.AppException;
import com.group3.MockProject.exception.ErrorCode;
import com.group3.MockProject.mapper.StatementMapper;
import com.group3.MockProject.repository.StatementRepository;
import com.group3.MockProject.service.IStatementService;
import com.group3.MockProject.validator.StatementValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * StatementServiceImpl
 * <p>
 * Implementation of statement service operations
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
@Service
@RequiredArgsConstructor
@Slf4j
public class StatementServiceImpl implements IStatementService {
    private final StatementRepository statementRepository;
    private final StatementValidator statementValidator;
    private final StatementMapper statementMapper;

    /**
     * Retrieves a statement by case ID and statement ID
     *
     * @param caseId The case identifier
     * @param statementId The statement identifier
     * @return StatementResponse containing statement details
     * @throws AppException if case or statement not found
     */
    @Override
    @Transactional(readOnly = true)
    public StatementResponse getStatementByCaseIdAndStatementId(String caseId, String statementId) {
        log.info("Getting statement - caseId: {}, statementId: {}", caseId, statementId);

        try {
            // Step 1: Validate input parameters
            statementValidator.validateGetStatementParams(caseId, statementId);

            // Step 2: Query statement with evidence links (using JOIN FETCH for performance)
            Statement statement = statementRepository.findByCaseIdAndStatementIdWithEvidences(caseId, statementId)
                    .orElseThrow(() -> {
                        log.error("Statement not found - caseId: {}, statementId: {}", caseId, statementId);
                        return new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION, "Statement not found");
                    });

            log.debug("Successfully retrieved statement: {}", statement.getStatementId());

            // Step 3: Convert entity to DTO using mapper
            StatementResponse response = statementMapper.toStatementResponse(statement);

            log.info("Successfully processed get statement request - caseId: {}, statementId: {}", caseId, statementId);
            return response;

        } catch (AppException e) {
            // Re-throw AppException to be handled by GlobalExceptionHandler
            log.error("AppException in getStatementByCaseIdAndStatementId: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            // Wrap unexpected exceptions
            log.error("Unexpected error in getStatementByCaseIdAndStatementId", e);
            throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION,
                    "Error retrieving statement: " + e.getMessage());
        }
    }
}
