package com.sosim.server.common.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Response<T> {
    /**
     * 요청에 대한 응답 메시지
     */
    private String message;

    /**
     * 요청에 대한 응답 데이터
     */
    private T content;

    public static<T> Response<?> createResponse(String message, T content) {
        return Response.builder()
                .message(message)
                .content(content)
                .build();
    }
}
