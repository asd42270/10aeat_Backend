package com.final_10aeat.domain.member.exception;

import com.final_10aeat.global.exception.ApplicationException;
import com.final_10aeat.global.exception.ErrorCode;

public class VerificationCodeExpiredException extends ApplicationException {

    private static final ErrorCode ERROR_CODE = ErrorCode.EMAIL_VERIFICATION_CODE_EXPIRED;

    public VerificationCodeExpiredException() {

        super(ERROR_CODE);
    }
}
