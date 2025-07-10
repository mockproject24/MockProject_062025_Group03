package com.group3.MockProject.dto.request.InvestigationPlanRequest;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class CreateInvestigationPlanDto {
    private String type;
    private String analysist;
    private List<MultipartFile> file;
}
