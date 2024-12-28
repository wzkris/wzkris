package com.wzkris.common.security.oauth2.resolver;

import com.wzkris.common.core.utils.StringUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.security.oauth2.server.resource.web.DefaultBearerTokenResolver;

/**
 * 扩展token解析, 支持从url上提取token
 *
 * @author wzkris
 */
public class CustomBearerTokenResolver implements BearerTokenResolver {

    final DefaultBearerTokenResolver defaultBearerTokenResolver = new DefaultBearerTokenResolver();

    @Override
    public String resolve(HttpServletRequest request) {
        String token = defaultBearerTokenResolver.resolve(request);
        if (StringUtil.isBlank(token)) {
            token = request.getParameter(HttpHeaders.AUTHORIZATION);
        }
        return token;
    }
}
