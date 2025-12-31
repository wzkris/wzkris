package com.wzkris.common.notifier.dingtalk.domain;

/**
 * Token缓存内部类
 */
public class TokenCache {

    private final String token;

    private final Long expireTime;

    public TokenCache(String token, Long expireTime) {
        this.token = token;
        this.expireTime = expireTime;
    }

    public String getToken() {
        return token;
    }

    public Long getExpireTime() {
        return expireTime;
    }

}
