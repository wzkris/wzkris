package com.wzkris.captcha.risk;

import com.wzkris.common.core.utils.ServletUtil;
import jakarta.servlet.http.HttpServletRequest;

public final class RiskClientKeys {

    private RiskClientKeys() {
    }

    /**
     * 网关风控与通行票绑定使用的默认主体键（当前为客户端 IP）。
     */
    public static String defaultCompositeKey(HttpServletRequest request) {
        return "ip:" + ServletUtil.getClientIP(request);
    }

}
