package com.sosim.server.common.advice;

import com.sosim.server.common.response.Response;
import com.sosim.server.config.exception.CustomException;
import com.sosim.server.type.CodeType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = CustomException.class)
    protected ResponseEntity<?> handleCustomException(CustomException exception) {
        return new ResponseEntity<>(Response.create(exception.getCodeType(), exception.getContent()),
                exception.getCodeType().getHttpStatus());
    }

    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    protected ResponseEntity<?> handleCustomException(HttpMessageNotReadableException exception) {
        return new ResponseEntity<>(Response.create(CodeType.BINDING_ERROR, null),
                HttpStatus.BAD_REQUEST);
    }
}
