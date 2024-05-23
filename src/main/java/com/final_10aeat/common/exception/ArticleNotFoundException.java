package com.final_10aeat.common.exception;

import com.final_10aeat.global.exception.ApplicationException;
import com.final_10aeat.global.exception.ErrorCode;

public class ArticleNotFoundException extends ApplicationException {

    private static final ErrorCode errorCode = ErrorCode.ARTICLE_NOT_FOUND;

    public ArticleNotFoundException(){super(errorCode);}
}
