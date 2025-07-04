package com.group3.MockProject.controller;

import com.group3.MockProject.dto.ResponseDto;
import com.group3.MockProject.dto.response.CaseListDto;
import com.group3.MockProject.dto.response.EvidentDto;
import com.group3.MockProject.service.ICaseService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/cases")
public class CaseController {
    private final ICaseService caseService;

    /**
     * Retrieves a paginated list of cases with optional search functionality.
     *
     * @param page     the page number, starting from 0 (must be >= 0)
     * @param pageSize the number of items per page (must be > 0)
     * @param search   an optional search keyword to filter cases
     * @return a ResponseEntity containing a ResponseDto with the list of cases
     */
    @GetMapping("")
    public ResponseEntity<ResponseDto<CaseListDto>> getCaseLists(@RequestParam(defaultValue = "0") int page,
                                                                  @RequestParam(defaultValue = "10") int pageSize,
                                                                  @RequestParam(required = false) String search){
        if(page < 0 || pageSize <= 0) {
            return ResponseEntity.badRequest().body(ResponseDto.error("Page and pageSize must be greater than 0"));
        }
        CaseListDto caseListDtos = caseService.getListCase(page, pageSize, search);
        return ResponseEntity.ok(ResponseDto.success(caseListDtos));
    }

    /**
     * Retrieves a list of evidences associated with a specific case.
     *
     * @param caseId the unique identifier of the case
     * @return a ResponseEntity containing a ResponseDto with the list of evidences,
     *         or a no-content response if no evidences are found
     */
    @GetMapping("{caseId}/evidences")
    public ResponseEntity<ResponseDto<List<EvidentDto<?>>>> getEvidences(@PathVariable String caseId) {
        List<EvidentDto<?>> evidences = caseService.getEvidences(caseId);
        if (evidences.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(ResponseDto.success(evidences));
    }
}

