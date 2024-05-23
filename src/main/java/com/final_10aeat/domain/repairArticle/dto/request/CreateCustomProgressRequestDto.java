package com.final_10aeat.domain.repairArticle.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record CreateCustomProgressRequestDto(

    @NotNull(message = "시작 일정은 필수 값 입니다.")
    LocalDateTime startSchedule,
    LocalDateTime endSchedule,
    @NotBlank(message = "제목을 입력해주세요.")
    String title,
    @NotBlank(message = "내용을 입력해주세요.")
    String content
) {

}
