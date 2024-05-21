package com.final_10aeat.domain.admin.dto.request;

import com.final_10aeat.domain.member.entity.MemberRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record EmailRequestDto(
    @NotBlank(message = "이메일은 필수 항목입니다.")
    @Email(message = "올바른 이메일 형식이어야 합니다.")
    String email,
    @NotNull(message = "가입자가 소유자인지 입주자인지 선택해주세요.")
    MemberRole role,
    String dong,
    @NotBlank(message = "호는 필수 항목입니다.")
    String ho
) {

}
