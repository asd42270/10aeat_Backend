package com.final_10aeat.domain.member.exception;

import com.final_10aeat.global.exception.ApplicationException;
import com.final_10aeat.global.exception.ErrorCode;

public class DisagreementException extends ApplicationException {

    private static final ErrorCode ERROR_CODE = ErrorCode.DISAGREE_TERM;

    public DisagreementException() {
        super(ERROR_CODE);
    }
}
