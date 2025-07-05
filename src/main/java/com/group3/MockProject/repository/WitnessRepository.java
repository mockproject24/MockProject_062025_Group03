package com.group3.MockProject.repository;

import com.group3.MockProject.entity.Witness;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * WitnessRepository
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
public interface WitnessRepository extends JpaRepository<Witness, String> {
    Optional<Witness> findWitnessByWitnessId(String witnessId);

    Optional<Witness> findWitnessByFullname(String fullname);
}
