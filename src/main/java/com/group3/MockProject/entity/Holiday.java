package com.group3.MockProject.entity;

import com.group3.MockProject.constant.HolidayType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;

/**
 * Holiday
 * <p>
 * Provides business logic for managing employment details.
 * <p>
 * Version 1.0
 * Date: 07/07/2025
 * <p>
 * Copyright
 * <p>
 * Modification Logs:
 * DATE         AUTHOR       DESCRIPTION
 * -------------------------------------
 * 07/07/2025     Hải Đăng      Create
 */
@Entity
@Table(name = "holidays")
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Holiday {

    @Id
    @Column(name = "holiday_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    String holidayId;

    @Column(name = "holiday_name")
    String holidayName;

    @Column(name = "type_of_holiday")
    @Enumerated(EnumType.STRING)
    HolidayType typeOfHoliday;

    @Column(name = "date_of_holiday")
    LocalDateTime dateOfHoliday;

    @Column(name = "notes", columnDefinition = "MEDIUMTEXT")
    String notes;

    @Column(name = "is_deleted")
    @ColumnDefault("false")
    boolean isDeleted = false;
}
