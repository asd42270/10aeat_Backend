package com.final_10aeat.domain.member.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record MemberLoginRequestDto(
    // 정책이 확정될 때 까지 이메일 정규식 임시 사용
    @Pattern(regexp = "^[a-zA-Z0-9+-_.]+@[a-zA-Z0-9-]+.[a-zA-Z0-9-.]+$")
    String email,
    @NotBlank
    String password
) {

}
