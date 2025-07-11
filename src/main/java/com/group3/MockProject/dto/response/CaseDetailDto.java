package com.group3.MockProject.dto.response;

import com.group3.MockProject.constant.CaseSeverity;
import com.group3.MockProject.constant.CaseStatus;
import com.group3.MockProject.constant.CaseType;
import com.group3.MockProject.constant.TaskStatus;
import com.group3.MockProject.constant.WarrantStatus;
import com.group3.MockProject.constant.EvidenceType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CaseDetailDto {
    // Basic infos
    private String caseId;
    private String caseName;
    private CaseType typeCase;
    private CaseSeverity severity;
    private CaseStatus status;
    private String summary;
    private LocalDateTime createAt;

    // Collections
    private List<TaskDto> tasks;
    private List<SuspectDto> suspects;
    private List<WarrantDto> warrants;
    private List<EvidenceDto> evidences;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TaskDto {
        private String taskId;
        private String taskName;
        private String content;
        private TaskStatus status;
        private LocalDateTime startDate;
        private LocalDateTime dueDate;
        private LocalDateTime completedAt;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SuspectDto {
        private String suspectId;
        private String fullname;
        private String national;
        private String gender;
        private LocalDateTime dob;
        private String identification;
        private String phoneNumber;
        private String description;
        private String address;
        private LocalDateTime catchTime;
        private String notes;
        private String status;
        private String mugshotUrl;
        private String fingerprintsHash;
        private String healthStatus;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WarrantDto {
        private String warrantId;
        private String warrantName;
        private List<String> attachedFile;
        private LocalDateTime timePublish;
        private LocalDateTime deadline;
        private WarrantStatus status;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EvidenceDto {
        private String evidenceId;
        private String description;
        private LocalDateTime collectedAt;
        private String currentLocation;
        private String attachFile;
        private String status;
        private EvidenceType evidenceType;
    }
}