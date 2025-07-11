package com.group3.MockProject.mapper;

import com.group3.MockProject.dto.response.PatrolOfficersDto;
import com.group3.MockProject.entity.User;
import org.springframework.stereotype.Component;

@Component
public class PatrolOfficersMapper {
    public PatrolOfficersDto toPatrolOfficersDto(User user, Integer serial){
        return new PatrolOfficersDto(
                serial,
                user.getFullName(),
                user.getStatus().toString(),
                user.getRole() != null ? user.getRole().getDescription() : null,
                user.getPhoneNumber(),
                // Thêm logic lấy zone nếu có
                null
        );
    }}

