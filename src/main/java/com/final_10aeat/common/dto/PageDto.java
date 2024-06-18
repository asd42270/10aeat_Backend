package com.final_10aeat.common.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class PageDto<T> {

    private int pageSize;
    private int currentPage;
    private long totalElements;
    private int totalPages;
    private List<T> articles;
}
