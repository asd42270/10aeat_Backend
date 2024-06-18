package com.final_10aeat.domain.manageArticle.exception;

import com.final_10aeat.global.exception.ApplicationException;
import com.final_10aeat.global.exception.ErrorCode;

public class ScheduleSizeException extends ApplicationException {

    private static final ErrorCode ERROR_CODE = ErrorCode.SCHEDULE_MUST_HAVE_ONE;

    public ScheduleSizeException() {

        super(ERROR_CODE);
    }
}
