package org.otp.otp.exception;

import org.springframework.http.HttpStatus;

public class UserExistException extends BaseException {

    public UserExistException() {
        this("User is already registered for the given user code", HttpStatus.BAD_REQUEST);
    }

    public UserExistException(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }
}
