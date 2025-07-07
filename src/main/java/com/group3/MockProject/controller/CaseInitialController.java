package com.group3.MockProject.controller;

import com.group3.MockProject.dto.ResponseDto;
import com.group3.MockProject.dto.request.SaveInitialResponseDto;
import com.group3.MockProject.service.CaseInitialService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * CaseInitialController
 * <p>
 * Provides an API to store all initial response information.
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
@RestController
@RequestMapping("/api/cases")
@RequiredArgsConstructor
public class CaseInitialController {

    private final CaseInitialService caseInitialService;

    /**
     * Save initial response information for a case
     *
     * @param caseId  the case ID
     * @param request the initial response data
     * @return ResponseDto with success or error message
     */
    @PostMapping("/{case_id}/initial-response")
    public ResponseDto<Void> saveInitialResponse(@PathVariable("case_id") String caseId,
                                                 @RequestBody SaveInitialResponseDto request) {
        return caseInitialService.saveInitialResponse(caseId, request);
    }
}
