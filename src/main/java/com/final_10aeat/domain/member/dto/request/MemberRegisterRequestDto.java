package com.final_10aeat.domain.member.dto.request;

public record MemberRegisterRequestDto(
        String email,
        String password
) { }
