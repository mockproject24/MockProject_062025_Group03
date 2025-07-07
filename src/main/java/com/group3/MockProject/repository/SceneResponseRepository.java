package com.group3.MockProject.repository;

import com.group3.MockProject.entity.SceneResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * SceneResponseRepository
 * <p>
 * Provides CRUD operations for SceneResponse entity.
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
public interface SceneResponseRepository extends JpaRepository<SceneResponse, String> {
} 