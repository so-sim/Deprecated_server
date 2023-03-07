package com.sosim.server.common.response;

import com.sosim.server.type.ErrorCodeType;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.ResponseEntity;

@Data
@Builder
public class ErrorResponse {
    private int status;
    private String code;
    private String message;

    public static ResponseEntity<ErrorResponse> toResponseEntity(ErrorCodeType e){
        return ResponseEntity
            .status(e.getHttpStatus())
            .body(ErrorResponse.builder()
                .status(e.getHttpStatus().value())
                .code(e.name())
                .message(e.getMessage())
                .build()
            );
    }
}
