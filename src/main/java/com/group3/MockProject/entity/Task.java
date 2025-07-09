package com.group3.MockProject.entity;

import com.group3.MockProject.constant.TaskStatus;
import com.group3.MockProject.constant.UserStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;

/**
 * Task
 * <p>
 * Provides business logic for managing employment details.
 * <p>
 * Version 1.0
 * Date: 08/07/2025
 * <p>
 * Copyright
 * <p>
 * Modification Logs:
 * DATE         AUTHOR       DESCRIPTION
 * -------------------------------------
 * 08/07/2025   Hải Đăng      Create
 */

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Task {
    @Id
    @Column(name = "task_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    String taskId;

    @Column(name = "task_name")
    String taskName;

    @Column(name = "content", columnDefinition = "MEDIUMTEXT")
    String content;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    TaskStatus status;

    @Column(name = "start_date")
    LocalDateTime startDate;

    @Column(name = "due_date")
    LocalDateTime dueDate;

    @Column(name = "completed_at")
    LocalDateTime completedAt;

    @Column(name = "is_deleted")
    @ColumnDefault("false")
    boolean isDeleted = false;

    @Column(name = "case_id", nullable = false)
    private String caseId;

    @Column(name = "username", nullable = false)
    private String username;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "case_id", referencedColumnName = "case_id"),
            @JoinColumn(name = "username", referencedColumnName = "username")
    })
     UsersCases caseUser;
}
