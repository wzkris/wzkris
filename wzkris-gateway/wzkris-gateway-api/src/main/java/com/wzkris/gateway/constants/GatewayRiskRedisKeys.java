package com.wzkris.gateway.constants;

public interface GatewayRiskRedisKeys {

    String PASS_KEY_PREFIX = "gateway:risk-pass:";

    String LOCK_KEY_PREFIX = "gateway:risk-lock:";

    static String passKey(String passToken) {
        return PASS_KEY_PREFIX + passToken;
    }

    static String lockKey(String clientKey) {
        return LOCK_KEY_PREFIX + clientKey;
    }

}
