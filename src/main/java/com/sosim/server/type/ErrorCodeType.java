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
    BINDING_ERROR("1000", HttpStatus.BAD_REQUEST, "입력값 중 검증에 실패한 값이 있습니다."),

    // Auth
    AUTH_INVALID_ACCESS("W2000", HttpStatus.FORBIDDEN, "Invalid auth access"),
    AUTH_VERIFICATION_EXPIRED("w2005", HttpStatus.BAD_REQUEST, "Verification expired"),

    // User
    USER_ALREADY_EXIST("W3001", HttpStatus.BAD_REQUEST, "회원가입 되어 있는 사용자입니다."),
    NOT_FOUND_USER("w3002", HttpStatus.NOT_FOUND, "해당 사용자가 존재하지 않습니다."),

    // Group
    NOT_FOUND_GROUP("1001", HttpStatus.NOT_FOUND, "해당 모임을 찾을 수 없습니다."),
    NONE_ADMIN("1002", HttpStatus.BAD_REQUEST, "관리자 권한이 필요합니다."),
    NONE_PARTICIPANT("1003", HttpStatus.NOT_FOUND, "존재하지 않는 참가자 정보입니다."),
    ALREADY_USE_NICKNAME("1004", HttpStatus.BAD_REQUEST, "모임에서 이미 사용중인 닉네임입니다."),
    NO_MORE_GROUP("1005", HttpStatus.BAD_REQUEST, "더 이상 조회할 모임이 없습니다."),
    ALREADY_INTO_GROUP("1006", HttpStatus.BAD_REQUEST, "이미 참여중인 모임입니다."),

    INVALID_USER("3003", HttpStatus.BAD_REQUEST, "사용자 정보가 일치하지 않습니다."),

    // Provider
    PROVIDER_LIST("W4001", HttpStatus.BAD_REQUEST, "카카오 로그인만 지원합니다."),
    ;

    private String code;
    private HttpStatus httpStatus;
    private String message;

}
