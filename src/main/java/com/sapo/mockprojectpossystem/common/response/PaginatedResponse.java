package com.sapo.mockprojectpossystem.common.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaginatedResponse<T> {
    private List<T> data;

    private Integer currentPage;

    private Integer pageSize;

    private Integer totalPages;

    private Long totalElements;

    private Boolean hasNext;

    private Boolean hasPrevious;
}