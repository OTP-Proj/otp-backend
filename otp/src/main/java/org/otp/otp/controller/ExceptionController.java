package org.otp.otp.controller;

import lombok.extern.slf4j.Slf4j;
import org.otp.otp.exception.BaseException;
import org.otp.otp.exception.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ExceptionController {

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ErrorResponse> handleBaseException(BaseException ex) {
        log.error("Exception is occurred!");
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(), ex.getHttpStatus(), ex.getDate());
        log.error(errorResponse.getMessage());
        return new ResponseEntity<>(errorResponse, ex.getHttpStatus());
    }
}