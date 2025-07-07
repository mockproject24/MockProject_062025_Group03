package com.group3.MockProject.controller;

import org.springframework.http.ResponseEntity;


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

    @GetMapping("/{caseId}/assigned-officers")
    public ResponseEntity<Map<String, Object>> getAssignedOfficers(
            @PathVariable String caseId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<UserResponseDto> pageResult = caseService.getAssignedOfficers(caseId, pageable);
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "Success");
        response.put("result", new HashMap<String, Object>() {{
            put("content", pageResult.getContent());
            put("totalElements", pageResult.getTotalElements());
            put("totalPages", pageResult.getTotalPages());
            put("size", pageResult.getSize());
            put("number", pageResult.getNumber());
        }});
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{caseId}/records")
    public ResponseEntity<ResponseDto<RecordInfoResponseDto>> createRecord(
            @PathVariable String caseId,
            @RequestBody CreateRecordInfoDto requestDto) {
        try {
            RecordInfoResponseDto createdRecord = caseService.createRecord(caseId, requestDto);
            ResponseDto<RecordInfoResponseDto> response = new ResponseDto<>(201, "Record created successfully", createdRecord);
            return ResponseEntity.status(201).body(response);
        } catch (Exception e) {
            ResponseDto<RecordInfoResponseDto> response = new ResponseDto<>(500, "Error creating record: " + e.getMessage(), null);
            return ResponseEntity.status(500).body(response);
        }
        return ResponseEntity.ok(ResponseDto.success(evidences));
    }
}
