package com.final_10aeat.domain.repairArticle.dto.response;

import java.time.LocalDateTime;

public record CustomProgressResponseDto(
    Long id,
    String title,
    String content,
    boolean inProgress,
    LocalDateTime startSchedule,
    LocalDateTime endSchedule
) {

}
