package com.final_10aeat.domain.member.dto.request;

import jakarta.validation.constraints.NotBlank;

public record MemberWithdrawRequestDto(
        @NotBlank
        String email,
        @NotBlank
        String password
) {
}
