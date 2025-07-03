package com.group3.MockProject.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/cases")
public class CaseController {

    @GetMapping("{caseId}/evidences")
    public String getEvidencesByCaseId(String caseId) {
        return "List of evidences for case ID: " + caseId;
    }
}
