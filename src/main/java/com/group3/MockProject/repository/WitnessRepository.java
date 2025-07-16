package com.group3.MockProject.repository;

import com.group3.MockProject.entity.Witness;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * WitnessRepository
 * <p>
 * Repository for Witness entity operations.
 * Provides data access methods for witness management and identification.
 * <p>
 * Version 1.0
 * Date: 7/5/2025
 * <p>
 * Copyright
 * <p>
 * Modification Logs:
 * DATE         AUTHOR       DESCRIPTION
 * -------------------------------------
 * 5/7/2025      User        Create
 * 12/7/2025     FongFox     Update
 */
@Repository
public interface WitnessRepository extends JpaRepository<Witness, String> {
    Optional<Witness> findWitnessByFullName(String fullname);

    /**
     * Finds witness by ID card number
     * Used for interview participant verification
     */
    Optional<Witness> findByWitnessIdCard(Long witnessIdCard);
}
