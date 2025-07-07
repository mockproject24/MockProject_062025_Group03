package com.group3.MockProject.repository;

import com.group3.MockProject.entity.UsersCases;
import com.group3.MockProject.entity.UsersCasesId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * UsersCasesRepository
 * <p>
 * Provides CRUD operations for UsersCases entity.
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
public interface UsersCasesRepository extends JpaRepository<UsersCases, UsersCasesId> {
} 