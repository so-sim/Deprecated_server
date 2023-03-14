package com.sosim.server.config.exception;

import com.sosim.server.type.CodeType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.core.NestedRuntimeException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
public class CustomException extends NestedRuntimeException {

    @Getter
    private CodeType codeType;

    private String field;
    private String fieldMessage;
    private String message;
    private HttpStatus httpStatus;

    private String code;

    public CustomException(CodeType codeType) {
        this(codeType.getMessage(), codeType.getHttpStatus(), codeType.getCode());
    }

    public CustomException(String field, String message, CodeType codeType) {
        this(field, message, codeType.getHttpStatus(), codeType.getCode());
    }

    public CustomException(String message, CodeType codeType) {
        this(message, codeType.getHttpStatus(), codeType.getCode());
    }

    public CustomException(CodeType codeType, String field, String fieldMessage) {
        this(field, fieldMessage, codeType.getMessage(), codeType.getHttpStatus(), codeType.getCode());
    }

    public CustomException(String message, HttpStatus httpStatus, int code) {
        super(message);
        this.message = message;
        this.httpStatus = httpStatus;
        this.code = String.valueOf(code);
    }

    public CustomException(String message, HttpStatus httpStatus, String code) {
        super(message);
        this.message = message;
        this.httpStatus = httpStatus;
        this.code = code;
    }

    public CustomException(String field, String message, HttpStatus httpStatus, String code) {
        super(message);
        this.field = field;
        this.message = message;
        this.httpStatus = httpStatus;
        this.code = code;
    }

    public CustomException(String field, String fieldMessage, String message, HttpStatus httpStatus, String code) {
        super(message);
        this.field = field;
        this.fieldMessage = fieldMessage;
        this.message = message;
        this.httpStatus = httpStatus;
        this.code = code;
    }

    public RestError toRestError() {
        return new RestError(this.field, this.code, this.message, this.httpStatus);
    }

    public ResponseEntity<RestError> toResponseEntity() {
        return new ResponseEntity<>(this.toRestError(), this.httpStatus);
    }

    @AllArgsConstructor
    @Getter
    public static class RestError {
        private String field;
        private String code;
        private String message;
        private HttpStatus status;
    }

}
