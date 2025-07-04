package com.group3.MockProject.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CaseListDto {
    private int page;
    private int pageSize;
    private long total;
    private List<CaseDto> data;
}