package com.group3.MockProject.repository;

import com.group3.MockProject.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * WitnessInterviewRepository
 * <p>
 * Provides business logic for managing employment details.
 * <p>
 * Version 1.0
 * Date: 7/5/2025
 * <p>
 * Copyright
 * <p>
 * Modification Logs:
 * DATE         AUTHOR       DESCRIPTION
 * -------------------------------------
 * 7/5/2025      User      Create
 */
@Repository
public interface WitnessInterviewRepository extends JpaRepository<WitnessInterview, WitnessInterviewId> {
    boolean existsByInterviewAndWitness(Interview interview, Witness witness);
}
