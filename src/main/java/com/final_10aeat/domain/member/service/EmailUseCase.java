package com.final_10aeat.domain.member.service;

import com.final_10aeat.domain.member.dto.response.EmailVerificationResponseDto;
import com.final_10aeat.domain.member.entity.MemberRole;
import com.final_10aeat.global.util.ResponseDTO;
import jakarta.mail.MessagingException;
import java.io.UnsupportedEncodingException;

public interface EmailUseCase {

    String sendVerificationEmail(String to, MemberRole role, String dong, String ho)
        throws MessagingException, UnsupportedEncodingException;

    ResponseDTO<EmailVerificationResponseDto> verifyEmailCode(String email, String code);
}
