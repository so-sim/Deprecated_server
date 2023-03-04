package com.sosim.server.type;

public enum SocialType {
    KAKAO("KAKAO", "id"),
    NAVER("NAVER", "response"),
    GOOGLE("GOOGLE", "sub");

    private String providerName;
    private String attributeKey;

    SocialType(String providerName, String attributeKey) {
        this.providerName = providerName;
        this.attributeKey = attributeKey;
    }

    public static SocialType getProvider(String providerName) {
        return SocialType.valueOf(providerName.toUpperCase());
    }

    public String getAttributeKey() {
        return this.attributeKey;
    }
}