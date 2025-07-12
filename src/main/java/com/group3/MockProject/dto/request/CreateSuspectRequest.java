package com.group3.MockProject.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * CreateSuspectRequest
 * <p>
 * Provides business logic for managing employment details.
 * <p>
 * Version 1.0
 * Date: 04/07/2025
 * <p>
 * Copyright
 * <p>
 * Modification Logs:
 * DATE         AUTHOR       DESCRIPTION
 * -------------------------------------
 * 04/07/2025   Hải Đăng      Create
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateSuspectRequest {

    @NotBlank(message = "SUSPECT_INVALID_NAME")
    private String fullName;

    @NotBlank(message = "SUSPECT_INVALID_ADDRESS")
    private String address;
    private String moreInfo;
}
