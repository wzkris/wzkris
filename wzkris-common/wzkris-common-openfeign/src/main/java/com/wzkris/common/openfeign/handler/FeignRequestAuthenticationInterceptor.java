package com.wzkris.common.openfeign.handler;

import com.wzkris.common.openfeign.constants.FeignHeaderConstant;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : openfeign拦截器透传spring-security认证信息
 * @date : 2025/7/8 14:46
 */
public class FeignRequestAuthenticationInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (isAuthenticated(authentication)) {
            String serialize = AuthenticationTokenUtil.serialize(authentication);
            template.header(FeignHeaderConstant.X_AUTHENTICATION, serialize);
        }
    }

    public boolean isAuthenticated(Authentication authentication) {
        return authentication != null
                && !AnonymousAuthenticationToken.class.isAssignableFrom(authentication.getClass())
                && authentication.isAuthenticated();
    }

}
