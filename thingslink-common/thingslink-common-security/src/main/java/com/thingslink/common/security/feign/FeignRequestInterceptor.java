package com.thingslink.common.security.feign;

import com.thingslink.common.core.utils.StringUtil;
import com.thingslink.common.security.constants.SecurityConstants;
import com.thingslink.common.security.utils.LoginUserUtil;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.OAuth2AccessToken;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : feign请求拦截器
 * @date : 2023/8/4 10:46
 */
public class FeignRequestInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        // 通过feign调用的请求加上内部调用标识，否则无法访问feign暴露接口
        String target = requestTemplate.feignTarget().name();
        requestTemplate.header(SecurityConstants.INNER_REQUEST_HEADER, target);

        // 未登录则直接返回
        if (!LoginUserUtil.isLogin()) {
            return;
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 传递token
        Object credentials = authentication.getCredentials();
        if (credentials instanceof OAuth2AccessToken auth2AccessToken) {
            requestTemplate.header(SecurityConstants.TOKEN_HEADER, auth2AccessToken.getTokenType().getValue() + StringUtil.SPACE + auth2AccessToken.getTokenValue());
        }

    }
}
