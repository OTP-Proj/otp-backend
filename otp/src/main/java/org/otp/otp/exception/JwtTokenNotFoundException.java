package org.otp.otp.exception;

import org.springframework.http.HttpStatus;

/**
 * @author Javidan Alizada
 */
public class JwtTokenNotFoundException extends BaseException {
    public JwtTokenNotFoundException() {
        this("No JWT token found in request header", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public JwtTokenNotFoundException(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }
}
