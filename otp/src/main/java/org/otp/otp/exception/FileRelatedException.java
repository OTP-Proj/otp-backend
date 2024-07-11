package org.otp.otp.exception;

import org.springframework.http.HttpStatus;

public class FileRelatedException extends BaseException {

    public FileRelatedException() {
        this("Exception occurred in file", HttpStatus.BAD_REQUEST);
    }

    public FileRelatedException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }

    public FileRelatedException(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }
}
