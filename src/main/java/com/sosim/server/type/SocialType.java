package com.sosim.server.type;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum SocialType {
    KAKAO("kakao"),
    NAVER("naver"),
    GOOGLE("google");

    private final String socialName;

    public static SocialType getSocialType(String socialType) {
        return SocialType.valueOf(socialType.toUpperCase());
    }
}