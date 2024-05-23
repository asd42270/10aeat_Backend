package com.final_10aeat.domain.repairArticle.exception;

import com.final_10aeat.global.exception.ApplicationException;
import com.final_10aeat.global.exception.ErrorCode;

public class CustomProgressNotFoundException extends ApplicationException {

    private static final ErrorCode ERROR_CODE = ErrorCode.CUSTOM_PROGRESS_NOT_FOUND;

    public CustomProgressNotFoundException() {

        super(ERROR_CODE);
    }
}
