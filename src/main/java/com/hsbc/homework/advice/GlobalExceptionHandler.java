package com.hsbc.homework.advice;

import com.hsbc.homework.exceptions.TransactionException;
import lombok.Getter;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.http.ResponseEntity;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(TransactionException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(TransactionException ex) {
        ErrorResponse response = new ErrorResponse(
                ex.getStatus().value(),
                ex.getErrorCode(),
                ex.getMessage(),
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(response, ex.getStatus());
    }
}

@Getter
class ErrorResponse {
    public int status;
    public String code;
    public String message;
    public long timestamp;

    public ErrorResponse(int status,String code,String message,long timestamp){
        this.status = status;
        this.code = code;
        this.message = message;
        this.timestamp = timestamp;
    }
}
