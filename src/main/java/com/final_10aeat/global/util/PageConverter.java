package com.final_10aeat.global.util;

import com.final_10aeat.common.dto.PageDto;
import org.springframework.data.domain.Page;

public class PageConverter {

    public static <T> PageDto<T> convertToCustomPageDto(Page<T> page) {
        return new PageDto<>(
            page.getSize(),
            page.getNumber(),
            page.getTotalElements(),
            page.getTotalPages(),
            page.getContent()
        );
    }
}
