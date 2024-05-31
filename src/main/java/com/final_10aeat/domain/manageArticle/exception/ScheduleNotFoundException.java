package com.final_10aeat.domain.manageArticle.exception;

import com.final_10aeat.global.exception.ApplicationException;
import com.final_10aeat.global.exception.ErrorCode;

public class ScheduleNotFoundException extends ApplicationException {

    private static final ErrorCode ERROR_CODE = ErrorCode.SCHEDULE_NOT_FOUND;

    public ScheduleNotFoundException() {

        super(ERROR_CODE);
    }
}
