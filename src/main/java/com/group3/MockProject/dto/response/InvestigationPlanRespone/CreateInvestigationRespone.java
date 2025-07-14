package com.group3.MockProject.dto.response.InvestigationPlanRespone;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class CreateInvestigationRespone {
    private String type;
    private String analysist;
    private List<MultipartFile> file;
}
