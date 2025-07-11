package com.group3.MockProject.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatrolOfficersDto {
    private Integer serial;
    private String fullName;
    private String presentStatus;
    private String role;
    private String phoneNumber;
    private String zone;
}
