package com.final_10aeat.global.exception;

import com.final_10aeat.global.util.ResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionRestAdvice {

    @ExceptionHandler
    public ResponseEntity<ResponseDTO<Void>> applicationException(ApplicationException e) {
        log.error(e.getMessage(), e);
        return ResponseEntity
                .status(e.getErrorCode().getHttpStatus())
                .body(ResponseDTO.error(e.getErrorCode()));
    }

    @ExceptionHandler
    public ResponseEntity<ResponseDTO<Void>> bindException(BindException e) {
        log.error(e.getMessage(), e);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ResponseDTO.errorWithMessage(HttpStatus.BAD_REQUEST,
                        e.getBindingResult().getAllErrors().get(0).getDefaultMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<ResponseDTO<Void>> dbException(DataAccessException e) {
        log.error(e.getMessage(), e);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ResponseDTO.errorWithMessage(HttpStatus.INTERNAL_SERVER_ERROR, "db 에러!"));
    }

    @ExceptionHandler
    public ResponseEntity<ResponseDTO<Void>> serverException(RuntimeException e) {
        log.error(e.getMessage(), e);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ResponseDTO.errorWithMessage(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러!"));
    }

    @ExceptionHandler
    public ResponseEntity<ResponseDTO<Void>> serverException(AccessDeniedException e) {
        log.error(e.getMessage(), e);
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(ResponseDTO.errorWithMessage(HttpStatus.FORBIDDEN, "권한이 없습니다!"));
    }
}
