package com.wzkris.gateway.constants;

import com.wzkris.common.core.utils.ServletUtil;
import jakarta.servlet.http.HttpServletRequest;

public interface GatewayRiskRedisKeys {

    String PASS_KEY_PREFIX = "gateway:risk-pass:";

    String LOCK_KEY_PREFIX = "gateway:risk-lock:";

    static String passKey(String passToken) {
        return PASS_KEY_PREFIX + passToken;
    }

    static String lockKey(String clientKey) {
        return LOCK_KEY_PREFIX + clientKey;
    }

    static String clientKey(HttpServletRequest request) {
        return "ip:" + ServletUtil.getClientIP(request);
    }

}
