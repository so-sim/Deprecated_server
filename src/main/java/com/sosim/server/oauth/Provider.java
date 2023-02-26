package com.sosim.server.oauth;

public enum Provider {
    KAKAO("KAKAO", "id"),
    NAVER("NAVER", "response"),
    GOOGLE("GOOGLE", "sub");

    private String providerName;
    private String attributeKey;

    Provider(String providerName, String attributeKey) {
        this.providerName = providerName;
        this.attributeKey = attributeKey;
    }

    public static Provider getProvider(String providerName) {
        return Provider.valueOf(providerName.toUpperCase());
    }

    public String getAttributeKey() {
        return this.attributeKey;
    }
}
