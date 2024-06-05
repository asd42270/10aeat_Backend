package com.final_10aeat.common.dto.util;

import com.final_10aeat.common.dto.CustomPageDto;
import org.springframework.data.domain.Page;

public class PageConverter {

    public static <T> CustomPageDto<T> convertToCustomPageDto(Page<T> page) {
        return new CustomPageDto<>(
            page.getSize(),
            page.getNumber(),
            page.getTotalElements(),
            page.getTotalPages(),
            page.getContent()
        );
    }
}
