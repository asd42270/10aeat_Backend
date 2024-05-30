package com.final_10aeat.domain.comment.exception;

import static com.final_10aeat.global.exception.ErrorCode.PARENT_COMMENT_NOT_FOUND;

import com.final_10aeat.global.exception.ApplicationException;
import com.final_10aeat.global.exception.ErrorCode;

public class ParentCommentNotFoundException extends ApplicationException {

    private static final ErrorCode ERROR_CODE = PARENT_COMMENT_NOT_FOUND;

    public ParentCommentNotFoundException() {
        super(ERROR_CODE);
    }
}
