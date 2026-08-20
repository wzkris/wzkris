package com.wzkris.auth.security.handler;

import com.wzkris.auth.constants.OAuth2ParameterConstant;
import com.wzkris.auth.domain.TokenPair;
import com.wzkris.auth.enums.LoginTypeEnum;
import com.wzkris.auth.event.LoginEvent;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.core.support.LoginUser;
import com.wzkris.common.core.utils.ServletUtil;
import com.wzkris.common.core.utils.SpringUtil;
import com.wzkris.common.core.utils.TraceIdUtil;
import com.wzkris.common.web.utils.UserAgentUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nl.basjes.parse.useragent.UserAgent;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 登录 / 刷新 / 切换登录态成功后统一写回令牌并记录日志。
 * <p>
 * 依赖上游已签发 JWT：{@code details} 为 {@link com.wzkris.auth.domain.TokenPair}。
 */
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

        UsernamePasswordAuthenticationToken authenticationToken = (UsernamePasswordAuthenticationToken) authentication;
        TokenPair tokenPair = (TokenPair) authenticationToken.getDetails();

        Map<String, Object> parameters = new HashMap<>();
        parameters.put(OAuth2ParameterNames.ACCESS_TOKEN, tokenPair.accessToken());
        parameters.put(OAuth2ParameterNames.REFRESH_TOKEN, tokenPair.refreshToken());

        jsonMessageConverter.write(
                Result.ok(parameters), STRING_OBJECT_MAP.getType(),
                MediaType.APPLICATION_JSON, new ServletServerHttpResponse(response));

        recordLog(request, authenticationToken);
    }

    private void recordLog(HttpServletRequest request, UsernamePasswordAuthenticationToken authenticationToken) {
        LoginTypeEnum loginType = LoginTypeEnum.fromValue(request.getParameter(OAuth2ParameterConstant.LOGIN_TYPE));
        if (loginType == null || loginType == LoginTypeEnum.REFRESH) {
            return;
        }

        UserAgent.ImmutableUserAgent userAgent = UserAgentUtil.INSTANCE.parse(request.getHeader(HttpHeaders.USER_AGENT));
        SpringUtil.getContext()
                .publishEvent(new LoginEvent(
                        (LoginUser) authenticationToken.getPrincipal(),
                        loginType.getValue(),
                        true,
                        "",
                        ServletUtil.getClientIP(request),
                        userAgent.getUserAgentString(),
                        TraceIdUtil.get()));
    }

}
