package org.otp.otp.exception;

import org.springframework.http.HttpStatus;

public class OtpTokenNotValidException extends BaseException {

    public OtpTokenNotValidException() {
        this("Otp token not valid.", HttpStatus.BAD_REQUEST);
    }

    public OtpTokenNotValidException(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }
}