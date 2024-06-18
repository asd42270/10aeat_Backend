package com.final_10aeat.common.exception;

import com.final_10aeat.global.exception.ApplicationException;
import com.final_10aeat.global.exception.ErrorCode;

public class ImageConvertException extends ApplicationException {

    private static final ErrorCode errorCode = ErrorCode.IMAGE_CONVERT_FAIL;

    public ImageConvertException(){super(errorCode);}
}