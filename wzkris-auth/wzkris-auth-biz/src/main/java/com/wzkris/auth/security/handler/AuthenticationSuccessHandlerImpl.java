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

import com.wzkris.auth.listener.event.LoginEvent;
import com.wzkris.auth.listener.event.RefreshTokenEvent;
import com.wzkris.auth.security.core.CommonAuthenticationToken;
import com.wzkris.auth.security.core.refresh.RefreshAuthenticationToken;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.core.utils.ServletUtil;
import com.wzkris.common.core.utils.SpringUtil;
import com.wzkris.common.web.utils.UserAgentUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.endpoint.DefaultOAuth2AccessTokenResponseMapConverter;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
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

    private final MappingJackson2HttpMessageConverter jsonMessageConverter
            = new MappingJackson2HttpMessageConverter();

    private final Converter<OAuth2AccessTokenResponse, Map<String, Object>> accessTokenResponseParametersConverter
            = new DefaultOAuth2AccessTokenResponseMapConverter();

    private final ParameterizedTypeReference<Map<String, Object>> STRING_OBJECT_MAP
            = new ParameterizedTypeReference<>() {
    };

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
            log.warn("use default response info, current authentication type :{}",
                    authentication.getClass().getName());
            jsonMessageConverter.write(
                    Result.ok(), STRING_OBJECT_MAP.getType(),
                    MediaType.APPLICATION_JSON, new ServletServerHttpResponse(response));
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
                        .publishEvent(new LoginEvent(
                                commonAuthenticationToken.getPrincipal(),
                                authenticationToken.getRefreshToken().getTokenValue(),
                                commonAuthenticationToken.getLoginType(),
                                true,
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

        Map<String, Object> convert = accessTokenResponseParametersConverter.convert(builder.build());
        jsonMessageConverter.write(
                Result.ok(convert), STRING_OBJECT_MAP.getType(),
                MediaType.APPLICATION_JSON, new ServletServerHttpResponse(response));
    }

    private void sendIntrospectionTokenResponse(
            HttpServletResponse response, OAuth2TokenIntrospectionAuthenticationToken authenticationToken)
            throws IOException {

        jsonMessageConverter.write(
                Result.ok(authenticationToken.getTokenClaims().getClaims()), STRING_OBJECT_MAP.getType(),
                MediaType.APPLICATION_JSON, new ServletServerHttpResponse(response));

    }

}
