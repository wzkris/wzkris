package com.wzkris.gateway.service;

import com.wzkris.auth.httpclient.token.LoginUserClient;
import com.wzkris.auth.httpclient.token.req.LoginUserReq;
import com.wzkris.auth.httpclient.token.req.OAuth2TokenReq;
import com.wzkris.auth.httpclient.token.resp.LoginUserResp;
import com.wzkris.common.core.constant.JwtClaimConstants;
import com.wzkris.common.core.enums.AuthTypeEnum;
import com.wzkris.common.core.exception.service.ApiResultException;
import com.wzkris.common.core.model.BaseLoginUser;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.core.utils.ResultUtil;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.security.model.ClientLoginUser;
import com.wzkris.common.security.utils.BearerTokenUtil;
import com.wzkris.gateway.properties.PermitAllProperties;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 用户信息提取工具类（Servlet 环境同步实现）
 * @date : 2025/01/29
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class TokenValidateService {

    private final LoginUserClient loginUserClient;

    private final JwtDecoder jwtDecoder;

    private final PermitAllProperties permitAllProperties;

    /**
     * 验证 JWT Token（同步）
     * 1. 本地验证 JWT（使用 Spring OAuth2 JwtDecoder）
     * 2. 解析出 uid
     * 3. 调用 auth 服务获取 BaseLoginUser 并组装 Authentication
     */
    public Authentication check(HttpServletRequest request) {
        String token = extractToken(request);
        if (StringUtil.isBlank(token)) {
            throw new ApiResultException(HttpStatus.UNAUTHORIZED.value(), Result.unauth("Authorization token not found!!"));
        }
        try {
            Jwt jwt = jwtDecoder.decode(token);
            String authType = jwt.getClaimAsString(JwtClaimConstants.AUTH_TYPE);
            AuthTypeEnum authTypeEnum = AuthTypeEnum.fromValue(authType);
            if (authTypeEnum == null) {
                String msg = StringUtil.isBlank(authType) ? "Invalid token: missing auth_type claim"
                        : "Invalid token: invalid auth_type claim";
                throw new ApiResultException(HttpStatus.UNAUTHORIZED.value(), Result.unauth(msg));
            }

            if (authTypeEnum == AuthTypeEnum.CLIENT) {
                return authenticateClient(jwt, token);
            }
            String uidStr = jwt.getSubject();
            if (StringUtil.isBlank(uidStr)) {
                throw new ApiResultException(HttpStatus.UNAUTHORIZED.value(), Result.unauth("Invalid token: missing subject"));
            }
            String sid = jwt.getClaimAsString(JwtClaimConstants.SID);
            if (StringUtil.isBlank(sid)) {
                return introspectOAuth2(token);
            }
            return introspectCustom(authTypeEnum, Long.valueOf(uidStr), token, sid);
        } catch (JwtException e) {
            log.warn("JWT validation failed: {}", e.getMessage());
            throw new ApiResultException(HttpStatus.UNAUTHORIZED.value(), Result.unauth("Invalid token: " + e.getMessage()));
        }
    }

    private String extractToken(HttpServletRequest request) {
        String token = BearerTokenUtil.extractBearerToken(request);
        if (StringUtil.isNotBlank(token) || !permitAllProperties.isWsQueryTokenEnabled()) {
            return token;
        }
        return BearerTokenUtil.extractWsAccessToken(request);
    }

    private Authentication authenticateClient(Jwt jwt, String token) {
        ClientLoginUser clientLoginUser = new ClientLoginUser();
        clientLoginUser.setClientId(jwt.getSubject());
        List<String> scope = jwt.getClaimAsStringList(OAuth2ParameterNames.SCOPE);
        return UsernamePasswordAuthenticationToken.authenticated(
                clientLoginUser, token,
                CollectionUtils.isNotEmpty(scope)
                        ? AuthorityUtils.createAuthorityList(scope)
                        : AuthorityUtils.NO_AUTHORITIES);
    }

    private Authentication introspectCustom(AuthTypeEnum authTypeEnum, Long uid, String token, String sid) {
        LoginUserReq loginUserReq = new LoginUserReq(authTypeEnum.getValue(), uid, sid);
        Result<LoginUserResp> r = loginUserClient.queryInfo(loginUserReq);
        if (!ResultUtil.check(r)) {
            throw new ApiResultException(HttpStatus.UNAUTHORIZED.value(), Result.unauth(r.getMessage()));
        }

        LoginUserResp loginUserResp = r.getData();
        return buildAuthentication(loginUserResp.getLoginUser(), token, loginUserResp.getPermissions());
    }

    /**
     * OAuth2类型token的验证流程（同步）
     * 调用auth服务的新接口，通过OAuth2AuthorizationService.findByToken()查询
     */
    private Authentication introspectOAuth2(String token) {
        OAuth2TokenReq oAuth2TokenReq = new OAuth2TokenReq(token);
        Result<LoginUserResp> r = loginUserClient.queryOAuth2(oAuth2TokenReq);
        if (!ResultUtil.check(r)) {
            throw new ApiResultException(HttpStatus.UNAUTHORIZED.value(), Result.unauth(r.getMessage()));
        }

        LoginUserResp loginUserResp = r.getData();

        return buildAuthentication(loginUserResp.getLoginUser(), token, loginUserResp.getPermissions());
    }

    private Authentication buildAuthentication(BaseLoginUser baseLoginUser, String token, Set<String> permissions) {
        return UsernamePasswordAuthenticationToken.authenticated(
                baseLoginUser, token,
                CollectionUtils.isNotEmpty(permissions)
                        ? AuthorityUtils.createAuthorityList(permissions)
                        : AuthorityUtils.NO_AUTHORITIES);
    }

}
