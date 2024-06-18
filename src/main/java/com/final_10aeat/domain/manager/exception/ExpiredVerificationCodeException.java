package com.final_10aeat.domain.manager.exception;

import com.final_10aeat.global.exception.ApplicationException;
import com.final_10aeat.global.exception.ErrorCode;

public class ExpiredVerificationCodeException extends ApplicationException {

    private static final ErrorCode ERROR_CODE = ErrorCode.EMAIL_VERIFICATION_CODE_EXPIRED;

    public ExpiredVerificationCodeException() {

        super(ERROR_CODE);
    }
}
