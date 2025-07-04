package com.group3.MockProject.service;

import com.group3.MockProject.entity.Case;
import com.group3.MockProject.repository.CaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CaseService {

    @Autowired
    private CaseRepository caseRepository;

    public Case getCaseById(String caseId) {
        Optional<Case> optionalCase = caseRepository.findById(caseId);
        if (optionalCase.isEmpty()) {
            throw new RuntimeException("Case not found with id: " + caseId);
        }
        return optionalCase.get();
    }
}
