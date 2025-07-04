package com.group3.MockProject.repository;

import com.group3.MockProject.entity.Case;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CaseRepository extends JpaRepository<Case, String> {
    Page<Case> findByCaseNumberContains(String caseNumber, Pageable pageable);
}
