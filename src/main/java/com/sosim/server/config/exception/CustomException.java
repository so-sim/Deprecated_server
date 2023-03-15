package com.sosim.server.config.exception;

import com.sosim.server.type.CodeType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.core.NestedRuntimeException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
public class CustomException extends NestedRuntimeException {

    private CodeType codeType;
    private Content content;

    public CustomException(CodeType codeType) {
        super(codeType.getMessage());
        this.codeType = codeType;
    }

    public CustomException(CodeType codeType, String field, String message) {
        this(codeType);
        content = new Content(field, message);
    }

    @Getter
    @AllArgsConstructor
    private static class Content {
        private String field;
        private String message;
    }
}
