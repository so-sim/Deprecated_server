package com.sosim.server.common.advice;

import com.sosim.server.common.response.Response;
import com.sosim.server.config.exception.CustomException;
import com.sosim.server.type.CodeType;
import javax.validation.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
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

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        String field = bindingResult.getFieldError().getField();
        String message = String.format("%s [%s]", bindingResult.getFieldError().getDefaultMessage(), field);
        CustomException customException = new CustomException(CodeType.COMMON_BAD_REQUEST, field, message);
        return handleCustomException(customException);
    }

    @ExceptionHandler({BindException.class})
    public ResponseEntity<?> handleBindException(BindException e) {
        String field = e.getFieldError().getField();
        String message = String.format("%s [%s]", e.getFieldError().getDefaultMessage(), field);
        CustomException customException = new CustomException(CodeType.COMMON_BAD_REQUEST, field, message);
        return handleCustomException(customException);
    }

    @ExceptionHandler({ValidationException.class})
    public ResponseEntity<?> handleValidationException(ValidationException e) {
        Throwable throwable = e.getCause();
        if (throwable instanceof CustomException) {
            CustomException customException = (CustomException) throwable;
            return handleCustomException(customException);
        } else {
            CustomException customException = new CustomException(throwable.getMessage(), CodeType.COMMON_INTERNAL_SERVER_ERROR);
            return customException.toResponseEntity();
        }
    }

    @ExceptionHandler({MissingRequestHeaderException.class})
    public ResponseEntity<?> handleMissingRequestHeaderException(MissingRequestHeaderException e) {
        CustomException customException = new CustomException(e.getMessage(), CodeType.COMMON_BAD_REQUEST);
        return customException.toResponseEntity();
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<?> handleException(Exception e) {
        CustomException customException = new CustomException(e.getMessage(), CodeType.COMMON_INTERNAL_SERVER_ERROR);
        return customException.toResponseEntity();
    }
}
