package com.final_10aeat.domain.admin.service;

import com.final_10aeat.domain.admin.dto.response.EmailVerificationResponseDto;
import com.final_10aeat.domain.member.entity.MemberRole;
import jakarta.mail.MessagingException;
import java.io.UnsupportedEncodingException;

public interface EmailUseCase {

    String sendVerificationEmail(String to, MemberRole role, String dong, String ho)
        throws MessagingException, UnsupportedEncodingException;

    EmailVerificationResponseDto verifyEmailCode(String email, String code);
}
