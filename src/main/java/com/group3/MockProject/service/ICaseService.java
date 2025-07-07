package com.group3.MockProject.service;

import com.group3.MockProject.dto.response.CaseListDto;
import com.group3.MockProject.dto.response.EvidentDto;

import java.util.List;

public interface ICaseService {
    List<EvidentDto<?>> getEvidences(String caseId);

    CaseListDto getListCase(int page, int pageSize, String search);
}
