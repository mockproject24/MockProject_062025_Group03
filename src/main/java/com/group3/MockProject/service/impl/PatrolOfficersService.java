package com.group3.MockProject.service.impl;

import com.group3.MockProject.dto.response.PaginatedResponse;
import com.group3.MockProject.dto.response.PatrolOfficersDto;
import com.group3.MockProject.entity.User;
import com.group3.MockProject.mapper.PatrolOfficersMapper;
import com.group3.MockProject.repository.UserRepository;
import com.group3.MockProject.service.IPatrolOfficersService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * PatrolOfficersService
 * <p>
 * Retrieves a paginated and filtered list of patrol officers for assignment to the scene.
 * <p>
 * Version 1.0
 * <p>
 * Date: 04/07/2025
 * <p>
 * Copyright
 * <p>
 * Modification Logs:
 * DATE              AUTHOR                 DESCRIPTION
 * -------------------------------------------------------------
 * 10/07/2025        Nguyen Quoc Tri        Create
 */
@Service
@RequiredArgsConstructor
public class PatrolOfficersService implements IPatrolOfficersService {

    private final UserRepository userRepository;
    private final PatrolOfficersMapper patrolOfficersMapper;

    @Override
    public PaginatedResponse<PatrolOfficersDto> getAllPatrolOfficers(Pageable pageable) {
        return getAllPatrolOfficers(pageable, null);
    }

    @Override
    public PaginatedResponse<PatrolOfficersDto> getAllPatrolOfficers(Pageable pageable, String search) {
        Page<User> userPage;

        if (search != null && !search.trim().isEmpty()) {
            // Tìm kiếm theo tên hoặc số điện thoại
            userPage = userRepository.findByFullNameContainingIgnoreCaseOrPhoneNumberContaining(
                    search.trim(), search.trim(), pageable);
        } else {
            userPage = userRepository.findAll(pageable);
        }

        int offset = pageable.getPageNumber() * pageable.getPageSize();

        List<PatrolOfficersDto> dtos = IntStream.range(0, userPage.getContent().size())
                .mapToObj(i -> {
                    User user = userPage.getContent().get(i);
                    int serial = offset + i + 1;
                    return patrolOfficersMapper.toPatrolOfficersDto(user, serial);
                })
                .collect(Collectors.toList());

        Page<PatrolOfficersDto> resultPage = new PageImpl<>(
                dtos,
                pageable,
                userPage.getTotalElements()
        );

        return PaginatedResponse.fromPage(resultPage);
    }
}
