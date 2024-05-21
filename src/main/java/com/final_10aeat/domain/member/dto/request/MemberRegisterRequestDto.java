package com.final_10aeat.domain.member.dto.request;

import com.final_10aeat.domain.member.entity.MemberRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record MemberRegisterRequestDto(
        @Pattern(regexp = "^[a-zA-Z0-9+-_.]+@[a-zA-Z0-9-]+.[a-zA-Z0-9-.]+$")
        String email,
        @NotBlank
        String password,
        @NotBlank
        String name,
        @NotBlank
        String dong,
        @NotBlank
        String ho,
        @NotNull
        MemberRole memberRole,
        @NotNull
        Boolean isTermAgreed
) { }
