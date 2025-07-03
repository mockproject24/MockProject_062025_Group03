package com.group3.MockProject.controller;

import com.group3.MockProject.dto.ResponseDto;
import com.group3.MockProject.dto.response.EvidentDto;
import com.group3.MockProject.service.ICaseService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/cases")
public class CaseController {
    private final ICaseService caseService;
    @GetMapping("{caseId}/evidences")
    public ResponseEntity<ResponseDto<List<EvidentDto<?>>>> getEvidences(@PathVariable String caseId) {
        List<EvidentDto<?>> evidences = caseService.getEvidences(caseId);
        if (evidences.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(ResponseDto.success(evidences));
    }
}
