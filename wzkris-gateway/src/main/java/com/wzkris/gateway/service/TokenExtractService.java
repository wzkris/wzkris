package com.wzkris.gateway.service;

import com.wzkris.auth.httpclient.token.LoginUserClient;
import com.wzkris.auth.httpclient.token.req.LoginUserReq;
import com.wzkris.auth.httpclient.token.req.OAuth2TokenReq;
import com.wzkris.common.core.constant.CustomHeaderConstants;
import com.wzkris.common.core.constant.QueryParamConstants;
import com.wzkris.common.core.enums.AuthTypeEnum;
import com.wzkris.common.core.exception.service.ResultException;
import com.wzkris.common.core.model.ClientPrincipal;
import com.wzkris.common.core.model.LoginUser;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.core.utils.StringUtil;
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
import java.util.stream.Stream;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 用户信息提取工具类（Servlet 环境同步实现）
 * @date : 2025/01/29
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class TokenExtractService {

    private final LoginUserClient loginUserClient;

    private final JwtDecoder jwtDecoder;

    /**
     * 获取当前请求的用户信息（同步）
     *
     * @param request 请求对象
     * @return 用户认证信息
     */
    public Authentication getAuthentication(HttpServletRequest request) {
        if (!hasAnyToken(request)) {
            throw new ResultException(HttpStatus.UNAUTHORIZED.value(), Result.unauth("Authorization token not found!!"));
        }

        String adminToken = getAdminToken(request);
        if (StringUtil.isNotBlank(adminToken)) {
            return validate(AuthTypeEnum.ADMIN, adminToken);
        }

        String tenantToken = getTenantToken(request);
        if (StringUtil.isNotBlank(tenantToken)) {
            return validate(AuthTypeEnum.TENANT, tenantToken);
        }

        String customerToken = getCustomerToken(request);
        if (StringUtil.isNotBlank(customerToken)) {
            return validate(AuthTypeEnum.CUSTOMER, customerToken);
        }

        String clientToken = getClientToken(request);
        if (StringUtil.isNotBlank(clientToken)) {
            return validate(AuthTypeEnum.CLIENT, clientToken);
        }

        // 理论上不会执行到这里，因为hasAnyToken已经检查过
        throw new ResultException(HttpStatus.UNAUTHORIZED.value(), Result.unauth("Authorization token type not found!!"));
    }

    /**
     * 验证 JWT Token（同步）
     * 1. 本地验证 JWT（使用 Spring OAuth2 JwtDecoder）
     * 2. 解析出 uid
     * 3. 调用 auth 服务获取用户信息（通过 type 和 uid）
     */
    private Authentication validate(AuthTypeEnum authTypeEnum, String token) {
        try {
            Jwt jwt = jwtDecoder.decode(token);
            if (authTypeEnum == AuthTypeEnum.CLIENT) {
                ClientPrincipal clientPrincipal = new ClientPrincipal();
                clientPrincipal.setClientId(jwt.getSubject());

                List<String> scope = jwt.getClaimAsStringList(OAuth2ParameterNames.SCOPE);
                return UsernamePasswordAuthenticationToken.authenticated(
                        clientPrincipal, token,
                        CollectionUtils.isNotEmpty(scope)
                                ? AuthorityUtils.createAuthorityList(scope)
                                : AuthorityUtils.NO_AUTHORITIES);
            } else {
                String uidStr = jwt.getSubject();
                if (StringUtil.isBlank(uidStr)) {
                    throw new ResultException(HttpStatus.UNAUTHORIZED.value(), Result.unauth("Invalid token: missing subject"));
                }
                String sid = jwt.getClaimAsString("sid");

                if (StringUtil.isBlank(sid)) {
                    return introspectOAuth2(token);
                } else {
                    return introspectCustom(authTypeEnum, Long.valueOf(uidStr), token, sid);
                }
            }
        } catch (JwtException e) {
            log.warn("JWT validation failed: {}", e.getMessage());
            throw new ResultException(HttpStatus.UNAUTHORIZED.value(), Result.unauth("Invalid token: " + e.getMessage()));
        }
    }

    private Authentication introspectCustom(AuthTypeEnum authTypeEnum, Long uid, String token, String sid) {
        LoginUserReq loginUserReq = new LoginUserReq(authTypeEnum.getValue(), uid, sid);
        var tokenResponse = loginUserClient.query(loginUserReq);
        if (tokenResponse == null || !tokenResponse.isSuccess()) {
            log.warn("Token validation failed after JWT decode. {}", tokenResponse);
            String errMsg = (tokenResponse != null) ? tokenResponse.getDescription() : "Token validation failed";
            throw new ResultException(HttpStatus.UNAUTHORIZED.value(), Result.unauth(errMsg));
        }

        LoginUser loginUser = tokenResponse.getLoginUser();
        if (loginUser == null) {
            throw new ResultException(HttpStatus.UNAUTHORIZED.value(), Result.unauth("Principal not found"));
        }
        // 从 TokenResponse 获取权限信息
        Set<String> permissions = tokenResponse.getPermissions();
        // 创建 Authentication 对象
        return UsernamePasswordAuthenticationToken.authenticated(
                loginUser, token,
                CollectionUtils.isNotEmpty(permissions)
                        ? AuthorityUtils.createAuthorityList(permissions)
                        : AuthorityUtils.NO_AUTHORITIES);
    }

    /**
     * OAuth2类型token的验证流程（同步）
     * 调用auth服务的新接口，通过OAuth2AuthorizationService.findByToken()查询
     */
    private Authentication introspectOAuth2(String token) {
        OAuth2TokenReq oAuth2TokenReq = new OAuth2TokenReq(token);
        var tokenResponse = loginUserClient.queryByToken(oAuth2TokenReq);
        if (tokenResponse == null || !tokenResponse.isSuccess()) {
            log.warn("OAuth2 token validation failed. {}", tokenResponse);
            String errMsg = (tokenResponse != null) ? tokenResponse.getDescription() : "OAuth2 token validation failed";
            throw new ResultException(HttpStatus.UNAUTHORIZED.value(), Result.unauth(errMsg));
        }

        // 根据返回的Principal类型创建对应的Authentication
        Set<String> permissions = tokenResponse.getPermissions();

        if (tokenResponse.getLoginUser() != null) {
            // 返回LoginUser
            LoginUser loginUser = tokenResponse.getLoginUser();
            return UsernamePasswordAuthenticationToken.authenticated(
                    loginUser, token,
                    CollectionUtils.isNotEmpty(permissions)
                            ? AuthorityUtils.createAuthorityList(permissions)
                            : AuthorityUtils.NO_AUTHORITIES);
        } else {
            throw new ResultException(HttpStatus.UNAUTHORIZED.value(), Result.unauth("Principal not found in OAuth2 token response"));
        }
    }

    /**
     * 检查是否携带任一认证Token
     */
    private boolean hasAnyToken(HttpServletRequest request) {
        return Stream.of(
                        request.getHeader(CustomHeaderConstants.X_ADMIN_TOKEN),
                        request.getHeader(CustomHeaderConstants.X_TENANT_TOKEN),
                        request.getHeader(CustomHeaderConstants.X_CUSTOMER_TOKEN),
                        request.getHeader(CustomHeaderConstants.X_CLIENT_TOKEN),
                        request.getParameter(QueryParamConstants.X_ADMIN_TOKEN),
                        request.getParameter(QueryParamConstants.X_TENANT_TOKEN),
                        request.getParameter(QueryParamConstants.X_CUSTOMER_TOKEN),
                        request.getParameter(QueryParamConstants.X_CLIENT_TOKEN)
                )
                .anyMatch(StringUtil::isNotBlank);
    }

    /**
     * 获取管理员Token（优先Header，其次Query参数）
     */
    private String getAdminToken(HttpServletRequest request) {
        String token = request.getHeader(CustomHeaderConstants.X_ADMIN_TOKEN);
        if (StringUtil.isNotBlank(token)) {
            return token;
        }
        return request.getParameter(QueryParamConstants.X_ADMIN_TOKEN);
    }

    /**
     * 获取租户Token（优先Header，其次Query参数）
     */
    private String getTenantToken(HttpServletRequest request) {
        String token = request.getHeader(CustomHeaderConstants.X_TENANT_TOKEN);
        if (StringUtil.isNotBlank(token)) {
            return token;
        }
        return request.getParameter(QueryParamConstants.X_TENANT_TOKEN);
    }

    /**
     * 获取客户Token（优先Header，其次Query参数）
     */
    private String getCustomerToken(HttpServletRequest request) {
        String token = request.getHeader(CustomHeaderConstants.X_CUSTOMER_TOKEN);
        if (StringUtil.isNotBlank(token)) {
            return token;
        }
        return request.getParameter(QueryParamConstants.X_CUSTOMER_TOKEN);
    }

    private String getClientToken(HttpServletRequest request) {
        String token = request.getHeader(CustomHeaderConstants.X_CLIENT_TOKEN);
        if (StringUtil.isNotBlank(token)) {
            return token;
        }
        return request.getParameter(QueryParamConstants.X_CLIENT_TOKEN);
    }

}
