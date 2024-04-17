package com.thingslink.common.security.feign;

import com.thingslink.common.core.constant.SecurityConstants;
import com.thingslink.common.core.utils.ServletUtil;
import com.thingslink.common.core.utils.StringUtil;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthentication;

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

        HttpServletRequest request = ServletUtil.getRequest();
        if (request != null) {
            String gatewayIp;
            if (request.getHeader(SecurityConstants.GATEWAY_IP) == null) {
                // 如果网关IP请求头为空，则代表该request为网关转发
                gatewayIp = ServletUtil.getClientIP(request);
            }
            else {
                gatewayIp = request.getHeader(SecurityConstants.GATEWAY_IP);
            }

            requestTemplate.header(SecurityConstants.GATEWAY_IP, gatewayIp);
        }

        // 拿不到request，说明是异步调用或者内部调用
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // 未登录则直接返回
        if (authentication == null
                || !authentication.isAuthenticated()
                || authentication instanceof AnonymousAuthenticationToken) {
            return;
        }

        // 判断具体授权票据
        if (authentication instanceof BearerTokenAuthentication authenticationToken) {
            OAuth2AccessToken auth2AccessToken = (OAuth2AccessToken) authenticationToken.getCredentials();
            requestTemplate.header(SecurityConstants.TOKEN_HEADER, auth2AccessToken.getTokenType().getValue() + StringUtil.SPACE + auth2AccessToken.getTokenValue());
        }

    }
}
