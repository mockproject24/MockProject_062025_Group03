package com.group3.MockProject.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaginatedResponse<T> {
    private List<T> data;
    private PageInfo page;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PageInfo {
        private int number;
        private int pageSize;
        private long totalElements;
        private int totalPages;
    }

    public static <T> PaginatedResponse<T> fromPage(Page<T> page) {
        return new PaginatedResponse<>(
            page.getContent(),
            new PageInfo(
                page.getNumber() + 1,
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
            )
        );
    }
}