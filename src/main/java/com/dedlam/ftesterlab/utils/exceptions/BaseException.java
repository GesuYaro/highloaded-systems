package com.dedlam.ftesterlab.utils.exceptions;

import lombok.Getter;

@Getter
public class BaseException extends RuntimeException {
    private final ExceptionType exceptionType;

    public BaseException(String message, ExceptionType exceptionType) {
        super(message);
        this.exceptionType = exceptionType;
    }

    public BaseException(ExceptionType exceptionType) {
        this.exceptionType = exceptionType;
    }
}
