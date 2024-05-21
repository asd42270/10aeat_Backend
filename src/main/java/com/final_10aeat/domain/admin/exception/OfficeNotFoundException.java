package com.final_10aeat.domain.admin.exception;

import com.final_10aeat.global.exception.ApplicationException;
import com.final_10aeat.global.exception.ErrorCode;

public class OfficeNotFoundException extends ApplicationException {

    private static final ErrorCode ERROR_CODE = ErrorCode.OFFICE_NOT_FOUND;

    public OfficeNotFoundException() {
        super(ERROR_CODE);
    }
}
