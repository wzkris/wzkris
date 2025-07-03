/*
 * Copyright (c) 2020 pig4cloud Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.wzkris.auth.security.handler;

import com.wzkris.auth.listener.event.LoginTokenEvent;
import com.wzkris.auth.listener.event.RefreshTokenEvent;
import com.wzkris.auth.security.core.CommonAuthenticationToken;
import com.wzkris.auth.security.core.refresh.RefreshAuthenticationToken;
import com.wzkris.auth.security.handler.converter.CustomOAuth2AccessTokenResponseHttpMessageConverter;
import com.wzkris.auth.security.handler.converter.CustomOAuth2TokenIntrospectionHttpMessageConverter;
import com.wzkris.common.core.constant.CommonConstants;
import com.wzkris.common.core.utils.ServletUtil;
import com.wzkris.common.core.utils.SpringUtil;
import com.wzkris.common.core.utils.UserAgentUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.*;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenIntrospection;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2TokenIntrospectionAuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.time.temporal.ChronoUnit;
import java.util.Map;

/**
 * @author wzkris
 * @description 登录成功统一处理
 * @date 2024-03-01
 */
@Slf4j
public class AuthenticationSuccessHandlerImpl implements AuthenticationSuccessHandler {

    private final HttpMessageConverter<OAuth2AccessTokenResponse> accessTokenMessageConverter
            = new CustomOAuth2AccessTokenResponseHttpMessageConverter();

    private final HttpMessageConverter<OAuth2TokenIntrospection> introspectionMessageConverter
            = new CustomOAuth2TokenIntrospectionHttpMessageConverter();

    /**
     * Called when a user has been successfully authenticated.
     *
     * @param request        the request which caused the successful authentication
     * @param response       the response
     * @param authentication the <tt>Authentication</tt> object which was created during
     *                       the authentication process.
     */
    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException {

        if (authentication instanceof OAuth2AccessTokenAuthenticationToken accessTokenAuthentication) {

            this.recordSuccessLog(request, accessTokenAuthentication);

            this.sendAccessTokenResponse(response, accessTokenAuthentication);

        } else if (authentication instanceof OAuth2TokenIntrospectionAuthenticationToken introspectionAuthenticationToken) {

            this.sendIntrospectionTokenResponse(response, introspectionAuthenticationToken);

        } else {
            log.error(
                    "not support type {}",
                    authentication.getClass().getName());
            OAuth2Error error = new OAuth2Error(
                    OAuth2ErrorCodes.SERVER_ERROR, "Unable to process the access token response.", null);
            throw new OAuth2AuthenticationException(error);
        }

    }

    private void recordSuccessLog(
            HttpServletRequest request, OAuth2AccessTokenAuthenticationToken authenticationToken) {
        if (authenticationToken.getPrincipal() instanceof CommonAuthenticationToken commonAuthenticationToken) {
            if (commonAuthenticationToken instanceof RefreshAuthenticationToken refreshAuthenticationToken) {
                SpringUtil.getContext()
                        .publishEvent(new RefreshTokenEvent(
                                commonAuthenticationToken.getPrincipal(),
                                authenticationToken.getAccessToken().getTokenValue(),
                                refreshAuthenticationToken.getRefreshToken())
                        );
            } else {
                SpringUtil.getContext()
                        .publishEvent(new LoginTokenEvent(
                                commonAuthenticationToken.getPrincipal(),
                                authenticationToken.getRefreshToken().getTokenValue(),
                                commonAuthenticationToken.getLoginType(),
                                CommonConstants.STATUS_ENABLE,
                                "",
                                ServletUtil.getClientIP(request),
                                UserAgentUtil.INSTANCE.parse(request.getHeader(HttpHeaders.USER_AGENT))));
            }
        }
    }

    private void sendAccessTokenResponse(
            HttpServletResponse response, OAuth2AccessTokenAuthenticationToken authenticationToken)
            throws IOException {
        // 构造响应体
        OAuth2AccessToken accessToken = authenticationToken.getAccessToken();
        OAuth2RefreshToken refreshToken = authenticationToken.getRefreshToken();
        Map<String, Object> additionalParameters = authenticationToken.getAdditionalParameters();

        OAuth2AccessTokenResponse.Builder builder =
                OAuth2AccessTokenResponse.withToken(accessToken.getTokenValue()).tokenType(accessToken.getTokenType());

        if (accessToken.getIssuedAt() != null && accessToken.getExpiresAt() != null) {
            builder.expiresIn(ChronoUnit.SECONDS.between(accessToken.getIssuedAt(), accessToken.getExpiresAt()));
        }

        if (refreshToken != null) {
            builder.refreshToken(refreshToken.getTokenValue());
        }

        if (!CollectionUtils.isEmpty(additionalParameters)) {
            builder.additionalParameters(additionalParameters);
        }

        // 追加输出参数
        builder.additionalParameters(additionalParameters);

        ServletServerHttpResponse httpResponse = new ServletServerHttpResponse(response);
        this.accessTokenMessageConverter.write(builder.build(), null, httpResponse);
    }

    private void sendIntrospectionTokenResponse(
            HttpServletResponse response, OAuth2TokenIntrospectionAuthenticationToken authenticationToken)
            throws IOException {
        ServletServerHttpResponse httpResponse = new ServletServerHttpResponse(response);

        introspectionMessageConverter.write(authenticationToken.getTokenClaims(), null, httpResponse);

    }

}

