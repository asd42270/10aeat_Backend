package com.final_10aeat.global.security.jwt.exception;

import com.final_10aeat.global.exception.ApplicationException;
import com.final_10aeat.global.exception.ErrorCode;

public class JwtNotFoundException  extends ApplicationException {
    private static final ErrorCode ERROR_CODE = ErrorCode.TOKEN_NOT_FOUND;

    public JwtNotFoundException() {
        super(ERROR_CODE);
    }
}
