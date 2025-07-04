package com.group3.MockProject.controller;

import com.group3.MockProject.entity.Case;
import com.group3.MockProject.service.CaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cases")
public class CaseController {

    @Autowired
    private CaseService caseService;

    @GetMapping("/{caseId}")
    public ResponseEntity<Case> getCaseById(@PathVariable String caseId) {
        Case foundCase = caseService.getCaseById(caseId);
        return ResponseEntity.ok(foundCase);
    }
}
