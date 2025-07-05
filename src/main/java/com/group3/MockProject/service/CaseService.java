package com.group3.MockProject.service;

import com.group3.MockProject.dto.request.ApiResponse;
import com.group3.MockProject.dto.request.PaginatedResponse;
import com.group3.MockProject.dto.response.CaseResponseDto;
import com.group3.MockProject.entity.Case;

import java.util.List;

public interface CaseService {

    ApiResponse<PaginatedResponse<CaseResponseDto>> getAllCase(int page, int pageSize);
}
