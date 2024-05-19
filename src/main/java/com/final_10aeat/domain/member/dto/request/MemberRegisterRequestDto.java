package com.final_10aeat.domain.member.dto.request;

import com.final_10aeat.domain.member.entity.MemberRole;

public record MemberRegisterRequestDto(
        String email,
        String password,
        String dong,
        String ho,
        MemberRole memberRole
) { }
