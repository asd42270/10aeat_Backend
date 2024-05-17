package com.final_10aeat.domain.member.exception;

import com.final_10aeat.global.exception.ApplicationException;
import com.final_10aeat.global.exception.ErrorCode;

public class InvalidVerificationCodeException extends ApplicationException {

    private static final ErrorCode ERROR_CODE = ErrorCode.INVALID_VERIFICATION_CODE;

    public InvalidVerificationCodeException() {

        super(ERROR_CODE);
    }
}
