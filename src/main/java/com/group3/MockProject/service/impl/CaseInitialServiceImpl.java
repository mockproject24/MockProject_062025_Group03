package com.group3.MockProject.service.impl;

import com.group3.MockProject.dto.ResponseDto;
import com.group3.MockProject.dto.request.SaveInitialRequest;
import com.group3.MockProject.service.CaseInitialService;
import org.springframework.stereotype.Service;

/**
 * CaseInitialServiceImpl
 * <p>
 * Provides business logic for managing employment details.
 * <p>
 * Version 1.0
 * Date: 7/9/2025
 * <p>
 * Copyright
 * <p>
 * Modification Logs:
 * DATE         AUTHOR       DESCRIPTION
 * -------------------------------------
 * 7/9/2025      doanm      Create
 */
@Service
public class CaseInitialServiceImpl implements CaseInitialService {

    @Override
    public ResponseDto<Void> saveInitialResponse(String caseId, SaveInitialRequest request) {
        return null;
    }
}
