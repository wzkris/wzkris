package com.wzkris.auth.security.handler;

import com.wzkris.auth.enums.LoginTypeEnum;
import com.wzkris.auth.listener.event.LoginEvent;
import com.wzkris.auth.security.core.CommonAuthenticationToken;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.core.utils.ServletUtil;
import com.wzkris.common.core.utils.SpringUtil;
import com.wzkris.common.web.utils.UserAgentUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class DefaultAuthenticationSuccessHandlerImpl implements AuthenticationSuccessHandler {

    private final MappingJackson2HttpMessageConverter jsonMessageConverter
            = new MappingJackson2HttpMessageConverter();

    private final ParameterizedTypeReference<Map<String, Object>> STRING_OBJECT_MAP
            = new ParameterizedTypeReference<>() {
    };

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {

        CommonAuthenticationToken authenticationToken = (CommonAuthenticationToken) authentication;

        Map<String, Object> parameters = new HashMap<>();
        parameters.put(OAuth2ParameterNames.ACCESS_TOKEN, authenticationToken.getAccessToken());
        parameters.put(OAuth2ParameterNames.REFRESH_TOKEN, authenticationToken.getRefreshToken());

        jsonMessageConverter.write(
                Result.ok(parameters), STRING_OBJECT_MAP.getType(),
                MediaType.APPLICATION_JSON, new ServletServerHttpResponse(response));

        recordLog(request, authenticationToken);
    }

    private static void recordLog(
            HttpServletRequest request, CommonAuthenticationToken authenticationToken) {
        if (authenticationToken.getLoginType() != LoginTypeEnum.REFRESH) {
            SpringUtil.getContext()
                    .publishEvent(new LoginEvent(
                            authenticationToken.getPrincipal(),
                            authenticationToken.getLoginType().getValue(),
                            true,
                            "",
                            ServletUtil.getClientIP(request),
                            UserAgentUtil.INSTANCE.parse(request.getHeader(HttpHeaders.USER_AGENT))));
        }
    }

}
