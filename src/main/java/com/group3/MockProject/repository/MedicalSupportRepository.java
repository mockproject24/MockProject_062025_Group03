package com.group3.MockProject.repository;

import com.group3.MockProject.entity.MedicalSupport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * MedicalSupportRepository
 * <p>
 * Provides CRUD operations for MedicalSupport entity.
 * <p>
 * Version 1.0
 * Date: 04-Jul-25
 * <p>
 * Copyright
 * <p>
 * Modification Logs:
 * DATE         AUTHOR       DESCRIPTION
 * -------------------------------------
 * 04-Jul-25     Hoang Tran     Create
 */
@Repository
public interface MedicalSupportRepository extends JpaRepository<MedicalSupport, String> {
} 