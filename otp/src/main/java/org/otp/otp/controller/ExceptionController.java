package org.otp.otp.controller;

import lombok.extern.slf4j.Slf4j;
import org.otp.otp.exception.BaseException;
import org.otp.otp.exception.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.util.Date;

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

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ErrorResponse> handleBaseExceptionMaxUpload(MaxUploadSizeExceededException ex) {
        log.error("Uploaded file size exceeds limit!");
        ErrorResponse errorResponse = new ErrorResponse(ex.getBody().getDetail() + " : size : " + ex.getMaxUploadSize(),
                HttpStatus.PAYLOAD_TOO_LARGE, new Date());
        log.error(errorResponse.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.PAYLOAD_TOO_LARGE);
    }
}