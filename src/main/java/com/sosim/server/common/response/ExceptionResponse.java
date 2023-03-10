package com.sosim.server.common.response;

import com.sosim.server.config.exception.CustomException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ExceptionResponse {
    private String code;
    private String message;
    private Content content;

    @Builder
    private ExceptionResponse(String code, String message, String field, String fieldMessage) {
        this.code = code;
        this.message = message;
        if (field != null & fieldMessage != null) {
            this.content = new Content(field, fieldMessage);
        }
    }

    public static ExceptionResponse create(CustomException customException) {
        return ExceptionResponse.builder()
                .code(customException.getCode())
                .message(customException.getMessage())
                .field(customException.getField())
                .fieldMessage(customException.getFieldMessage())
                .build();
    }

    @Getter
    @AllArgsConstructor
    private static class Content {
        private String field;
        private String message;
    }
}
