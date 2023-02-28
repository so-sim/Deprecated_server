package com.sosim.server.type;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum ErrorCodeType {

    //COMMON
    COMMON_BAD_REQUEST("W0000", HttpStatus.BAD_REQUEST, "Bad request"),
    COMMON_NOT_FOUND_ID("W0001", HttpStatus.NOT_FOUND, "Not found Id"),
    COMMON_NO_ELEMENT("w0002", HttpStatus.NOT_FOUND, "No such element"),

    // Auth
    AUTH_INVALID_ACCESS("W2000", HttpStatus.FORBIDDEN, "Invalid auth access"),
    AUTH_VERIFICATION_EXPIRED("w2005", HttpStatus.BAD_REQUEST, "Verification expired"),

    // User
    USER_ALREADY_EXIST("W3003", HttpStatus.BAD_REQUEST, "회원가입 되어 있는 사용자입니다."),
    NOT_FOUND_USER("w0002", HttpStatus.NOT_FOUND, "해당 사용자가 존재하지 않습니다."),

    // Provider
    PROVIDER_LIST("W4001", HttpStatus.BAD_REQUEST, "카카오 로그인만 지원합니다."),
    ;

    private String code;
    private HttpStatus httpStatus;
    private String message;

}
