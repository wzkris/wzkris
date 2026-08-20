package com.wzkris.auth.domain;

/**
 * 令牌对，包含 accessToken 和 refreshToken。
 */
public record TokenPair(String accessToken, String refreshToken) {

}
