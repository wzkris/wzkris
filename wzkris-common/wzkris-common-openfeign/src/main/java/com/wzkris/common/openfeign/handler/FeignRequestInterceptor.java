package com.wzkris.common.openfeign.handler;

import com.wzkris.common.core.constant.HeaderConstants;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.openfeign.constants.FeignHeaderConstant;
import com.wzkris.common.openfeign.constants.ServiceIdConstant;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : openfeign拦截器
 * @date : 2023/8/4 10:46
 */
@Slf4j
public class FeignRequestInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {
        template.header(HeaderConstants.X_TRACING_ID, MDC.get(HeaderConstants.X_TRACING_ID));
        template.header(FeignHeaderConstant.X_INNER_REQUEST, "true");
        if (!StringUtil.equals(template.feignTarget().name(), ServiceIdConstant.AUTH)) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (isAuthenticated(authentication)) {
                String serialize = AuthenticationTokenUtil.serialize(authentication);
                template.header(FeignHeaderConstant.X_AUTHENTICATION, serialize);
            }
        }
    }

    public boolean isAuthenticated(Authentication authentication) {
        return authentication != null
                && !AnonymousAuthenticationToken.class.isAssignableFrom(authentication.getClass())
                && authentication.isAuthenticated();
    }

}
