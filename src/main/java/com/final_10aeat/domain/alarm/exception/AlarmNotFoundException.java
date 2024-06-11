package com.final_10aeat.domain.alarm.exception;

import com.final_10aeat.global.exception.ErrorCode;
import com.final_10aeat.global.exception.ApplicationException;
public class AlarmNotFoundException extends ApplicationException {

    private static final ErrorCode ERROR_CODE = ErrorCode.ALARM_NOT_FOUND;

    public AlarmNotFoundException() { super(ERROR_CODE); }
}
