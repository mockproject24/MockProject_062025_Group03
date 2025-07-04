package com.group3.MockProject.repository;

import com.group3.MockProject.entity.Case;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CaseRepository extends JpaRepository<Case, String> {
}
