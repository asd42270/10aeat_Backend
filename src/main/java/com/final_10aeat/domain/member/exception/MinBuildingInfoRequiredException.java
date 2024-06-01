package com.final_10aeat.domain.member.exception;

import com.final_10aeat.global.exception.ApplicationException;
import com.final_10aeat.global.exception.ErrorCode;

public class MinBuildingInfoRequiredException extends ApplicationException {

    private static final ErrorCode ERROR_CODE = ErrorCode.BUILDING_INFO_MINIMUM_ONE_REQUIRED;

    public MinBuildingInfoRequiredException() {
        super(ERROR_CODE);
    }
}
