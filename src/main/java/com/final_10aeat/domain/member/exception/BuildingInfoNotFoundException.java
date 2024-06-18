package com.final_10aeat.domain.member.exception;

import com.final_10aeat.global.exception.ApplicationException;
import com.final_10aeat.global.exception.ErrorCode;

public class BuildingInfoNotFoundException extends ApplicationException {

    private static final ErrorCode ERROR_CODE = ErrorCode.BUILDING_INFO_NOT_FOUND;

    public BuildingInfoNotFoundException() {
        super(ERROR_CODE);
    }
}
