package com.wzkris.auth.remote.impl.loginuser;

import com.wzkris.auth.remote.api.loginuser.LoginUserRemoteApi;
import com.wzkris.auth.remote.api.loginuser.request.LoginUserQueryRequest;
import com.wzkris.auth.remote.api.loginuser.request.OAuth2TokenQueryRequest;
import com.wzkris.auth.remote.api.loginuser.response.LoginUserResponse;
import com.wzkris.auth.service.TokenService;
import com.wzkris.common.core.model.BaseLoginUser;
import com.wzkris.common.core.model.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class LoginUserRemoteApiImpl implements LoginUserRemoteApi {

    private final TokenService tokenService;

    private final OAuth2AuthorizationService authorizationService;

    @Override
    public Result<LoginUserResponse> queryInfo(LoginUserQueryRequest request) {
        final Long uid = request.getUid();
        final String sid = request.getSid();
        final String authType = request.getAuthType();

        if (uid == null || authType == null || sid == null) {
            return Result.unauth("Invalid token: missing subject");
        }

        // 检查 sid 是否不在会话中
        if (tokenService.isRevoked(authType, uid, sid)) {
            return Result.unauth("Token has been revoked");
        }

        // 通过 uid 获取用户信息和权限
        BaseLoginUser loginUser = tokenService.loadLoginUserByUid(authType, uid);
        if (loginUser == null) {
            return Result.unauth("Token has been expired");
        }

        Set<String> permissions = tokenService.loadPermissionsByUid(authType, uid);
        return Result.ok(new LoginUserResponse(loginUser, permissions));
    }

    @Override
    public Result<LoginUserResponse> queryOAuth2(OAuth2TokenQueryRequest request) {
        final String token = request.getToken();

        if (token == null || token.isBlank()) {
            return Result.unauth("Invalid token: token is empty");
        }

        // 通过token查询OAuth2Authorization
        OAuth2Authorization authorization = authorizationService.findByToken(token, OAuth2TokenType.ACCESS_TOKEN);
        if (authorization == null) {
            return Result.unauth("Token not found or expired");
        }

        // 提取权限信息（从authorizedScopes）
        Set<String> permissions = authorization.getAuthorizedScopes() != null
                ? authorization.getAuthorizedScopes()
                : Set.of();

        // 从OAuth2Authorization中提取Principal
        Principal principal = authorization.getAttribute(Principal.class.getName());
        if (principal == null) {
            return Result.unauth("Principal not found in authorization");
        }

        if (principal instanceof BaseLoginUser loginUser) {
            return Result.ok(new LoginUserResponse(loginUser, permissions));
        } else {
            // 尝试从Principal中提取信息构造LoginUser
            // 这里可以根据实际需求扩展，比如从UserDetails转换
            log.warn("Unsupported principal type: {}, principalName: {}", principal.getClass().getName(), authorization.getPrincipalName());
            return Result.unauth("Unsupported principal type: " + principal.getClass().getName());
        }
    }

}
