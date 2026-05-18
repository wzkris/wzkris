package com.wzkris.gateway.utils;

import com.wzkris.common.core.utils.ServletUtil;
import jakarta.servlet.http.HttpServletRequest;

public final class GatewayRiskClientKeys {

    private GatewayRiskClientKeys() {
    }

    public static String defaultCompositeKey(HttpServletRequest request) {
        return "ip:" + ServletUtil.getClientIP(request);
    }

}
