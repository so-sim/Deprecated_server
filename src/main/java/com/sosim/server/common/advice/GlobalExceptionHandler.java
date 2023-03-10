package com.sosim.server.common.advice;

import com.sosim.server.common.response.ExceptionResponse;
import com.sosim.server.config.exception.CustomException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = CustomException.class)
    protected ResponseEntity<?> handleCustomException(CustomException exception) {
        return new ResponseEntity<>(ExceptionResponse.create(exception), exception.getHttpStatus());
    }
}
