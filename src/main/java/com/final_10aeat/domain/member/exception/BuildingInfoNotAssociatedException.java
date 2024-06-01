package com.final_10aeat.domain.member.exception;

import com.final_10aeat.global.exception.ApplicationException;
import com.final_10aeat.global.exception.ErrorCode;

public class BuildingInfoNotAssociatedException extends ApplicationException {

    private static final ErrorCode ERROR_CODE = ErrorCode.BUILDING_INFO_NOT_ASSOCIATED_WITH_MEMBER;

    public BuildingInfoNotAssociatedException() {
        super(ERROR_CODE);
    }
}
