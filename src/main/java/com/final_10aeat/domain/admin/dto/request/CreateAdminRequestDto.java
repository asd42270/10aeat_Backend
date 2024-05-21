package com.final_10aeat.domain.admin.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

public record CreateAdminRequestDto(
    @Email(message = "이메일 형식이 유효하지 않습니다.")
    @NotBlank
    String email,
    @NotBlank(message = "비밀번호는 필수 입력 사항입니다.")
    @Size(min = 8, message = "비밀번호는 최소 8자 이상이어야 합니다.")
    String password,
    @NotBlank(message = "이름은 필수 입력 사항입니다.")
    String name,
    @NotBlank(message = "전화번호는 필수 입력 사항입니다.")
    String phoneNumber,
    LocalDateTime lunchBreakStart,
    LocalDateTime lunchBreakEnd,
    @NotBlank(message = "관리 사무소 위치는 필수 입력 사항입니다.")
    String adminOffice,
    @NotBlank(message = "소속 정보는 필수 입력 사항입니다.")
    String affiliation,
    @NotNull(message = "관리하고 있는 건물 id는 필수 입력 사항입니다.")
    long officeId
) {

}
