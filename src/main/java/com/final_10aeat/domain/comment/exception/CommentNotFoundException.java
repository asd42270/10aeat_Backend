package com.final_10aeat.domain.comment.exception;

import static com.final_10aeat.global.exception.ErrorCode.COMMENT_NOT_FOUND;

import com.final_10aeat.global.exception.ApplicationException;
import com.final_10aeat.global.exception.ErrorCode;

public class CommentNotFoundException extends ApplicationException {

    private static final ErrorCode ERROR_CODE = COMMENT_NOT_FOUND;

    public CommentNotFoundException() {
        super(ERROR_CODE);
    }
}
