package com.final_10aeat.domain.admin.dto.request;

import jakarta.validation.constraints.NotBlank;

public record EmailVerificationRequestDto(

    @NotBlank(message = "이메일은 필수 항목입니다.")
    String email,

    @NotBlank(message = "인증 코드는 필수 항목입니다.")
    String code

) {

}
