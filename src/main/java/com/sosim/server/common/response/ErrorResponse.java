package com.sosim.server.common.response;

import com.sosim.server.type.CodeType;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.ResponseEntity;

@Data
@Builder
public class ErrorResponse {
    private int status;
    private String code;
    private String message;

    //TODO 여기서 e가 null이 되어버리는 문제 해결(그전까지는 잘 받음)
    public static ResponseEntity<ErrorResponse> toResponseEntity(CodeType e){
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
