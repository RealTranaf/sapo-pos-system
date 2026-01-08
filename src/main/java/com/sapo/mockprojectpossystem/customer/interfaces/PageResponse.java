package com.sapo.mockprojectpossystem.customer.interfaces;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

@Getter
@NoArgsConstructor
public class PageResponse<T> {
    private Map<String, List<T>> data;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
    private long currentPage;
    private boolean last;

    public PageResponse(String fieldName, Page<T> page) {
        this.data = Map.of(fieldName, page.getContent());
        this.page = page.getNumber();
        this.size = page.getSize();
        this.totalElements = page.getTotalElements();
        this.totalPages = page.getTotalPages();
        this.currentPage = page.getNumber();
        this.last = page.isLast();
    }

    @JsonAnyGetter
    public Map<String, List<T>> getData() {
        return data;
    }
}
