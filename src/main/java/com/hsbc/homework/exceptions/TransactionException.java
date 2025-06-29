package com.hsbc.homework.exceptions;

import org.springframework.http.HttpStatus;

public class TransactionException extends RuntimeException {
    public HttpStatus status;
    public String errorCode;

    public HttpStatus getStatus() {
        return status;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public TransactionException(String message, Exception e) {
        super(message, e.getCause());
    }

    public TransactionException(HttpStatus status, String errorCode, String message) {
        super(message);
        this.status = status;
        this.errorCode = errorCode;
    }
}
