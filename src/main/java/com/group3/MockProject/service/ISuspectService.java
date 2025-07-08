package com.group3.MockProject.service;

import com.group3.MockProject.dto.request.CreateSuspectRequest;
import com.group3.MockProject.dto.response.SuspectResponse;
import org.springframework.web.multipart.MultipartFile;

/**
 * ISuspectService
 * <p>
 * Provides business logic for managing employment details.
 * <p>
 * Version 1.0
 * Date: 04/07/2025
 * <p>
 * Copyright
 * <p>
 * Modification Logs:
 * DATE         AUTHOR       DESCRIPTION
 * -------------------------------------
 * 04/07/2025   Hải Đăng      Create
 */
public interface ISuspectService {
    SuspectResponse createSuspect(String caseId, CreateSuspectRequest request, MultipartFile file);
}
