package com.wzkris.common.security.interceptor;

import com.wzkris.common.core.constant.SecurityConstants;
import com.wzkris.common.core.utils.json.JsonUtil;
import com.wzkris.common.security.oauth2.config.OAuth2Properties;
import com.wzkris.common.security.utils.OAuth2Holder;
import feign.RequestInterceptor;
import feign.RequestTemplate;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : OAuth2 feign请求拦截器
 * @date : 2023/8/4 10:46
 */
public class OAuth2FeignInterceptor implements RequestInterceptor {

    private final OAuth2Properties oAuth2Properties;

    public OAuth2FeignInterceptor(OAuth2Properties oAuth2Properties) {
        this.oAuth2Properties = oAuth2Properties;
    }

    @Override
    public void apply(RequestTemplate requestTemplate) {
        requestTemplate.header(oAuth2Properties.getIdentityKey(), oAuth2Properties.getIdentityValue());

        if (OAuth2Holder.isAuthenticated()) {
            // 认证过了则追加信息
            requestTemplate.header(SecurityConstants.PRINCIPAL_HEADER, JsonUtil.toJsonString(OAuth2Holder.getPrincipal()));
        }
    }
}
