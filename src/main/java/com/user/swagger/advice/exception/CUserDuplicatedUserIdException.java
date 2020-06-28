package com.user.swagger.advice.exception;

public class CUserDuplicatedUserIdException extends RuntimeException {
    public CUserDuplicatedUserIdException() {
    }

    public CUserDuplicatedUserIdException(String message) {
        super(message);
    }

    public CUserDuplicatedUserIdException(String message, Throwable cause) {
        super(message, cause);
    }
}
