package com.group3.MockProject.dto.response;

import com.group3.MockProject.constant.CaseSeverity;
import com.group3.MockProject.constant.CaseStatus;
import com.group3.MockProject.constant.CaseType;
import com.group3.MockProject.entity.Evidence;
import com.group3.MockProject.entity.Suspect;
import com.group3.MockProject.entity.Warrant;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;
import java.util.List;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CaseDetailResponse {
    String caseId;
    String caseName;
    String typeCase;
    String severity;
    String status;
    String summary;
    LocalDateTime createAt;
    boolean isDeleted = false;
    List<Suspect> suspects;
    List<Evidence> evidences;
    List<Warrant> warrants;
}