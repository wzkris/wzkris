package com.thingslink.common.security.feign;

import com.thingslink.common.core.constant.SecurityConstants;
import com.thingslink.common.core.utils.json.JsonUtil;
import com.thingslink.common.security.utils.ClientTokenUtil;
import com.thingslink.common.security.utils.OAuth2Holder;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.AllArgsConstructor;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : OAuth2 feign请求拦截器
 * @date : 2023/8/4 10:46
 */
@AllArgsConstructor
public class OAuth2FeignInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        if (OAuth2Holder.isAuthenticated()) {
            // 认证过了则追加信息
            requestTemplate.header(SecurityConstants.PRINCIPAL_HEADER, JsonUtil.toJsonString(OAuth2Holder.getPrincipal()));
        }
        // 追加client_token，否则无法访问feign接口
        requestTemplate.header(SecurityConstants.TOKEN_HEADER, ClientTokenUtil.getClientToken());
    }
}
