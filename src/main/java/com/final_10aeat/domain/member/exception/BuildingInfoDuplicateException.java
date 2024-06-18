package com.final_10aeat.domain.member.exception;

import com.final_10aeat.global.exception.ApplicationException;
import com.final_10aeat.global.exception.ErrorCode;

public class BuildingInfoDuplicateException extends ApplicationException {

    private static final ErrorCode ERROR_CODE = ErrorCode.DUPLICATED_BUILDING_INFO;

    public BuildingInfoDuplicateException() {
        super(ERROR_CODE);
    }
}
