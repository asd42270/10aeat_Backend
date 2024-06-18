package com.final_10aeat.domain.manager.service;

import com.final_10aeat.domain.manager.dto.response.VerifyEmailResponseDto;
import com.final_10aeat.common.enumclass.MemberRole;
import jakarta.mail.MessagingException;
import java.io.UnsupportedEncodingException;

public interface EmailUseCase {

    String sendVerificationEmail(String to, MemberRole role, String dong, String ho,Long officeId)
        throws MessagingException, UnsupportedEncodingException;

    VerifyEmailResponseDto verifyEmailCode(String email, String code);
}
