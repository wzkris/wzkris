package com.wzkris.auth.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 令牌对，包含 accessToken 和 refreshToken。
 */
@Getter
@AllArgsConstructor
public class TokenPair {

    private final String accessToken;

    private final String refreshToken;
}
