package com.wzkris.gateway.service.impl;

import com.wzkris.common.core.constant.JwtClaimConstants;
import com.wzkris.common.core.enums.AuthTypeEnum;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.core.model.RoleContext;
import com.wzkris.common.core.model.UserRole;
import com.wzkris.common.core.utils.ResultUtil;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.security.authentication.RoleContextAuthenticationToken;
import com.wzkris.common.security.model.LoginClientUser;
import com.wzkris.common.security.utils.BearerTokenUtil;
import com.wzkris.gateway.properties.PermitUrlProperties;
import com.wzkris.gateway.remote.api.loginuser.ILoginUserRemote;
import com.wzkris.gateway.remote.api.loginuser.request.LoginUserQueryRequest;
import com.wzkris.gateway.remote.api.loginuser.request.OAuth2TokenQueryRequest;
import com.wzkris.gateway.remote.api.loginuser.response.LoginUserResponse;
import com.wzkris.gateway.service.TokenValidateService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 用户信息提取工具类（Servlet 环境同步实现）
 * @date : 2025/01/29
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TokenValidateServiceImpl implements TokenValidateService {

    private final ILoginUserRemote loginUserRemote;

    private final JwtDecoder jwtDecoder;

    private final PermitUrlProperties permitUrlProperties;

    /**
     * 验证 JWT Token（同步）
     * 1. 本地验证 JWT（使用 Spring OAuth2 JwtDecoder）
     * 2. 解析出 uid
     * 3. 调用 auth 服务获取 BaseLoginUser 并组装 Authentication
     */
    public Authentication loadAuthenticationByRequest(HttpServletRequest request) {
        String token = extractToken(request);
        Jwt jwt;
        try {
            jwt = jwtDecoder.decode(token);
        } catch (JwtException e) {
            log.info("JWT validation failed: {}", e.getMessage());
            return UsernamePasswordAuthenticationToken.unauthenticated(null, null);
        }

        AuthTypeEnum authTypeEnum = AuthTypeEnum.fromValue(jwt.getClaimAsString(JwtClaimConstants.AUTH_TYPE));
        if (authTypeEnum == null) {
            return UsernamePasswordAuthenticationToken.unauthenticated(null, null);
        }

        if (authTypeEnum == AuthTypeEnum.CLIENT) {
            return authenticateClient(jwt, token);
        }
        String sid = jwt.getClaimAsString(JwtClaimConstants.SID);
        if (StringUtil.isBlank(sid)) {
            return introspectOAuth2(token);
        } else {
            return introspectCustom(authTypeEnum, Long.valueOf(jwt.getSubject()), token, sid);
        }
    }

    private String extractToken(HttpServletRequest request) {
        String token = BearerTokenUtil.extractHeaderToken(request);
        if (StringUtil.isNotBlank(token)) {
            return token;
        }
        if (permitUrlProperties.isWsQueryTokenEnabled()
                && request.getRequestURI().startsWith(permitUrlProperties.getWsUri())) {
            return BearerTokenUtil.extractQueryToken(request);
        }
        return null;
    }

    private Authentication authenticateClient(Jwt jwt, String token) {
        LoginClientUser clientUser = new LoginClientUser();
        clientUser.setClientId(jwt.getSubject());
        List<String> scope = jwt.getClaimAsStringList(OAuth2ParameterNames.SCOPE);
        RoleContext roleContext = new RoleContext(
                List.of(new UserRole(0L, "client", null, null, new ArrayList<>(scope))));
        return RoleContextAuthenticationToken.authenticated(clientUser, token, roleContext);
    }

    private Authentication introspectCustom(AuthTypeEnum authTypeEnum, Long uid, String token, String sid) {
        LoginUserQueryRequest LoginUserQueryRequest = new LoginUserQueryRequest(authTypeEnum, uid, sid);
        Result<LoginUserResponse> r = loginUserRemote.queryInfo(LoginUserQueryRequest);
        if (!ResultUtil.check(r)) {
            return UsernamePasswordAuthenticationToken.unauthenticated(null, null);
        }

        LoginUserResponse LoginUserResponse = r.getData();
        return RoleContextAuthenticationToken.authenticated(LoginUserResponse.getLoginUser(), token, LoginUserResponse.getRoleContext());
    }

    /**
     * OAuth2类型token的验证流程（同步）
     * 调用auth服务的新接口，通过OAuth2AuthorizationService.findByToken()查询
     */
    private Authentication introspectOAuth2(String token) {
        OAuth2TokenQueryRequest OAuth2TokenQueryRequest = new OAuth2TokenQueryRequest(token);
        Result<LoginUserResponse> r = loginUserRemote.queryOAuth2(OAuth2TokenQueryRequest);
        if (!ResultUtil.check(r)) {
            return UsernamePasswordAuthenticationToken.unauthenticated(null, null);
        }

        LoginUserResponse LoginUserResponse = r.getData();

        return RoleContextAuthenticationToken.authenticated(LoginUserResponse.getLoginUser(), token, LoginUserResponse.getRoleContext());
    }

}
