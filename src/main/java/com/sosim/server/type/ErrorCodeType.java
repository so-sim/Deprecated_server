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
    COMMON_BAD_REQUEST("0000", HttpStatus.BAD_REQUEST, "Bad request"),
    COMMON_NOT_FOUND_ID("0001", HttpStatus.NOT_FOUND, "Not found Id"),
    COMMON_NO_ELEMENT("0002", HttpStatus.NOT_FOUND, "No such element"),

    // Auth
    AUTH_INVALID_ACCESS("2000", HttpStatus.FORBIDDEN, "Invalid auth access"),
    AUTH_VERIFICATION_EXPIRED("2005", HttpStatus.BAD_REQUEST, "Verification expired"),

    // User
    USER_ALREADY_EXIST("3001", HttpStatus.BAD_REQUEST, "회원가입 되어 있는 사용자입니다."),
    NOT_FOUND_USER("3002", HttpStatus.NOT_FOUND, "해당 사용자가 존재하지 않습니다."),

    // Group
    BINDING_ERROR("1000", HttpStatus.BAD_REQUEST, "입력값 중 검증에 실패한 값이 있습니다."),
    NOT_FOUND_GROUP("1001", HttpStatus.NOT_FOUND, "해당 모임을 찾을 수 없습니다."),
    NONE_ADMIN("1002", HttpStatus.BAD_REQUEST, "관리자 권한이 필요합니다."),
    NONE_PARTICIPANT("1003", HttpStatus.NOT_FOUND, "존재하지 않는 참가자 정보입니다."),

    INVALID_USER("3003", HttpStatus.BAD_REQUEST, "사용자 정보가 일치하지 않습니다."),

    // Provider
    PROVIDER_LIST("4001", HttpStatus.BAD_REQUEST, "카카오 로그인만 지원합니다."),
    ;

    private String code;
    private HttpStatus httpStatus;
    private String message;

}
