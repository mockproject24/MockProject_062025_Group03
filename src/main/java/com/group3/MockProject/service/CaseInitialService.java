package com.group3.MockProject.service;

import com.group3.MockProject.dto.ResponseDto;
import com.group3.MockProject.dto.request.SaveInitialRequest;

/**
 * CaseInitialService
 * <p>
 * Provides business logic for save case's initial response.
 * <p>
 * Version 1.0
 * Date: 04-Jul-25
 * <p>
 * Copyright
 * <p>
 * Modification Logs:
 * DATE         AUTHOR       DESCRIPTION
 * -------------------------------------
 * 07-Jul-25      Hoang Tran      Create
 */
public interface CaseInitialService {
    ResponseDto<Void> saveInitialResponse(String caseId, SaveInitialRequest request);
}
