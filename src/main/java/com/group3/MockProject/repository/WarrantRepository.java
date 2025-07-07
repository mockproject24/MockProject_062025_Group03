package com.group3.MockProject.repository;

import com.group3.MockProject.entity.Warrant;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * WarrantRepository
 * <p>
 * Provides business logic for managing employment details.
 * <p>
 * Version 1.0
 * Date: 7/4/2025
 * <p>
 * Copyright
 * <p>
 * Modification Logs:
 * DATE         AUTHOR       DESCRIPTION
 * -------------------------------------
 * 7/4/2025      NGUYEN NGOC SY      Create
 */

public interface WarrantRepository extends JpaRepository<Warrant, String> {
}