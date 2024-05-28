package com.final_10aeat.common.exception;

import com.final_10aeat.global.exception.ApplicationException;
import com.final_10aeat.global.exception.ErrorCode;

public class UnexpectedPrincipalException extends ApplicationException {

    private static final ErrorCode ERROR_CODE = ErrorCode.UNEXPECTED_PRINCIPAL;

    public UnexpectedPrincipalException() {

        super(ERROR_CODE);
    }
}
